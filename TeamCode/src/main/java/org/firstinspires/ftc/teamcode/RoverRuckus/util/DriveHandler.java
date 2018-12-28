package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Separate utility class to handle omnidirectional motion on mecanum wheels.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DriveHandler {
	private static final MotorPowerSet ZERO = new MotorPowerSet(0, 0, 0, 0);
	private static final Object lock = new Object();
	public static double MOVE_MULT = 4450; //change to tweak "move x meters" precisely. Degrees wheel turn per unit.
	public static double TURN_MULT = 1205; //change to tweak "rotate x deg" precisely.   Degrees wheel turn per
	//we have a separate thread handling moveTasks. This is so the robot can still do other stuff
	//while this is happening at the same time.
	private static MoveThread moveThread;
	public boolean moveEndFlag = false;
	// radians robot turn
	private Queue<MoveTask> moveTasks; //the currentPos moveTasks to do;
	//NOW THE FUN STUFF, FOR AUTONOMOUS MOTION.
	//the motors
	//[ FL, FR, BL, BR ]
	private DcMotor[] motors;
	
	/**
	 * construct by motors
	 */
	private DriveHandler(DcMotor leftFront, DcMotor rightFront, DcMotor backLeft, DcMotor backRight) {
		motors = new DcMotor[4];
		motors[0] = leftFront;
		motors[1] = rightFront;
		motors[2] = backLeft;
		motors[3] = backRight;
		moveThread = null;
		moveTasks = new ConcurrentLinkedQueue<>();
		for (int i = 0; i < 4; i++) {
			motors[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		}
		startMoveThread();
	}
	
	/**
	 * construct by Ben Bielin Code
	 */
	DriveHandler(HardwareTestBot r) {
		this(r.leftFront, r.rightFront, r.leftBack, r.rightBack);
	}
	
	/**
	 * Generates a MotorPowerSet that corresponds to turning the robot in the specified direction
	 */
	private static MotorPowerSet calcPowerSet(double direction, double speed, double turnRate) {
		//DO THE MATH
       /* assert (speed >= 0 && speed <= 1);
        speed = Math.abs(speed);
        if(speed > 1f) {
            speed = 1f;
        }
        */
		double robotAngle = direction + Math.PI / 4;
		double v1 = speed * Math.sin(robotAngle) + turnRate;
		double v2 = speed * Math.cos(robotAngle) - turnRate;
		double v3 = speed * Math.cos(robotAngle) + turnRate;
		double v4 = speed * Math.sin(robotAngle) - turnRate;
		//if any level is greater than 1, scale down to prevent robot going off course
		//if turnRate is 0 and speed <1, this wont be a problem.
		double max = Math.max(Math.max(Math.abs(v1), Math.abs(v2)), Math.max(Math.abs(v3), Math.abs(v4)));
		if (max < 1) max = 1;
		return new MotorPowerSet(v1 / max, v2 / max, v3 / max, v4 / max);
	}
	
	public void setModeEncoder() {
		for (int i = 0; i < 4; i++) {
			motors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
	}
	
	/**
	 * starts the MoveTasks handling thread. A new thread might be created.
	 */
	public void startMoveThread() {
		if (moveThread == null) {
			moveThread = new MoveThread();
			moveThread.start();
		}
	}
	
	/**
	 * returns if there are any move tasks currently
	 */
	public boolean hasTasks() {
		return !moveTasks.isEmpty();
	}
	
	/**
	 * adds a MoveTask to move in a straight line a specified direction and distance.
	 */
	public void move(double direction, double speed, double distance) { // maybe has some problems here
		direction = Math.toRadians(direction);
		addTask(new MoveTask(calcPowerSet(direction, speed, 0),
			distance * MOVE_MULT / speed, distance,
			direction));
	}
	
	/**
	 * ads a move task to rotate in place a specified number of degrees, positive or negative.
	 */
	public void turn(double degrees, double speed) {
		degrees = Math.toRadians(degrees);
		addTask(new MoveTask(calcPowerSet(0, 0, speed * Math.signum(degrees)),
			degrees * TURN_MULT / speed,
			0, 0));
	}
	
	private void addTask(MoveTask task) {
		moveTasks.add(task);
		synchronized (lock) {
			lock.notifyAll();
		}
	}
	
	/**
	 * cancels all tasks and stops robot.
	 */
	public void cancelTasks() {
		moveTasks.clear();
		stopRobot();
	}
	
	/**
	 * set motors to given powerSet.
	 */
	private void setPower(MotorPowerSet p) {
		for (int i = 0; i < 4; i++)
			motors[i].setPower(p.power[i]);
	}
	
	/**
	 * Tells the robot to move in a specied direction and turnRate.
	 * Can handle motor power levels greater than 1 -- it will scale them down.
	 * If you're not turning and moving at the same time, and speed <=1, that wont be a problem
	 */
	public void moveAt(double direction, double speed, double turnRate) {
		setPower(calcPowerSet(direction, speed, turnRate));
	}
	
	/**
	 * Stops robot, i.e., set motor power levels to zero.
	 */
	public void stopRobot() {
		setPower(ZERO);
	}
	
	@SuppressWarnings("StatementWithEmptyBody")
	public void waitForDone() {
		while (hasTasks()) ;
	}
	
	/**
	 * a set of power levels for algit rm --cached -r .ideal 4 motors;
	 * just a container around double[][]
	 */
	public static class MotorPowerSet {
		double[] power;
		
		MotorPowerSet(double leftFront, double rightFront, double backLeft, double backRight) {
			double[] power = new double[4];
			power[0] = leftFront;
			power[1] = rightFront;
			power[2] = backLeft;
			power[3] = backRight;
			this.power = power;
		}
		
		MotorPowerSet() {
			this.power = new double[4];
		}
	}
	
	private class MoveTask { //NOT STATIC, to access DCmotors
		//dont want to spam new, so I have fields instead of vars.
		private MotorPowerSet targetPower, actualPower;
		private double multiplier;
		private double[] progress = new double[4];
		private double[] curPos = new double[4];
		
		MoveTask(MotorPowerSet targetPower, double multiplier, double distance, double angle) {
			this.targetPower = targetPower;
			this.multiplier = multiplier;
			this.actualPower = new MotorPowerSet();
		}
		
		void start() {
			for (int i = 0; i < 4; i++) {
				motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				motors[i].setMode(DcMotor.RunMode.RUN_TO_POSITION);
				motors[i].setTargetPosition((int) (multiplier * targetPower.power[i]));
			}
			setPower(targetPower);
		}
		
		//read motor positions and adjust them as necessary if they go off track.
		//supposed to make all the motors turn in unison.
		boolean process() {
			double avgProgress = 0, totalOff = 0;
			for (int i = 0; i < 4; i++) {
				progress[i] = (double) motors[i].getCurrentPosition() / motors[i].getTargetPosition();
				totalOff = Math.abs(motors[i].getCurrentPosition() - motors[i].getTargetPosition());
				if (Double.isNaN(progress[i])) progress[i] = 1;
				avgProgress += progress[i];
			}
			avgProgress /= 4;
			//adjust power as necessary..
			for (int i = 0; i < 4; i++) {
				actualPower.power[i] = Math.abs(targetPower.power[i] * (1 - 3 * (progress[i] - avgProgress)));
			}
			setPower(actualPower);
			return totalOff < 100;
		}
		
	}
	
	private class MoveThread extends Thread {
		private boolean isFirstTime;
		private boolean exitFlag;
		
		MoveThread() {
			exitFlag = false;
			this.setName("MoveThread");
		}
		
		//continually run moveTasks;
		@Override
		public void run() {
			isFirstTime = true;
			while (true) {
				if (exitFlag) return;
				try {
					synchronized (lock) {
						while (moveTasks.isEmpty()) {
							lock.wait();
						}
					}
					MoveTask curTask = moveTasks.element();
					if (exitFlag) return;
					if (isFirstTime) {
						isFirstTime = false;
						curTask.start();
					}
					if (exitFlag) return;
					if (curTask.process()) {
						stopRobot();
						moveTasks.remove();
						isFirstTime = true;
						moveEndFlag = true;
					}
				} catch (NullPointerException | InterruptedException ignored) {
					return;
				}
			}
		}
	}
}
