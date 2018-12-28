package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Separate utility class to handle omnidirectional motion on mecanum wheels.
 */
public class DriveHandler {
	private static final MotorPowerSet ZERO = new MotorPowerSet(0, 0, 0, 0);
	private static final Object movingLock = new Object();
	private static final Object doneLock = new Object();
	//change to tweak "move x meters" precisely. Degrees wheel turn per unit.
	public static double MOVE_MULT = 4450;
	//change to tweak "rotate x deg" precisely. Degrees wheel turn per degrees robot turn.
	public static double TURN_MULT = 1205;
	/**
	 * we have a separate thread handling moveTasks. This is so the robot can still do other stuff
	 * while this is happening at the same time.
	 */
	private static MoveThread moveThread;
	private Queue<MoveTask> moveTasks = new ConcurrentLinkedQueue<>(); //current MoveTasks
	//the motors: [ FL, FR, BL, BR ]
	private DcMotor[] motors;
	
	/**
	 * construct by motors
	 */
	private DriveHandler(DcMotor leftFront, DcMotor rightFront, DcMotor backLeft, DcMotor backRight) {
		motors = new DcMotor[]{leftFront, rightFront, backLeft, backRight};
		for (int i = 0; i < 4; i++) {
			motors[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		}
		startMoveThread();
	}
	
	/**
	 * construct by Ben Bielin Code
	 */
	DriveHandler(Robot r) {
		this(r.leftFront, r.rightFront, r.leftBack, r.rightBack);
	}
	
	/**
	 * Generates a MotorPowerSet that corresponds to turning the robot in the specified direction
	 */
	private static MotorPowerSet calcPowerSet(double direction, double speed, double turnRate) {
		//DO THE MATH
		double robotAngle = direction + Math.PI / 4;
		double v1 = speed * Math.sin(robotAngle) + turnRate;
		double v2 = speed * Math.cos(robotAngle) - turnRate;
		double v3 = speed * Math.cos(robotAngle) + turnRate;
		double v4 = speed * Math.sin(robotAngle) - turnRate;
		//NOTE: scaling is now done separately, as a method of MotorPowerSet
		return new MotorPowerSet(v1, v2, v3, v4);
	}
	
	public void setModeEncoder() {
		for (int i = 0; i < 4; i++) {
			motors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
	}
	
	/**
	 * starts the MoveTasks handling thread. A new thread might be created.
	 */
	private void startMoveThread() {
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
	public void move(double direction, double speed, double distance) {
		direction = Math.toRadians(direction);
		addTask(new MoveTask(calcPowerSet(direction, speed, 0), distance * MOVE_MULT / speed));
	}
	
	/**
	 * adds a move task to rotate in place a specified number of degrees, positive or negative.
	 */
	public void turn(double degrees, double speed) {
		degrees = Math.toRadians(degrees);
		addTask(new MoveTask(calcPowerSet(0, 0, speed * Math.signum(degrees)), degrees * TURN_MULT / speed));
	}
	
	private void addTask(MoveTask task) {
		synchronized (movingLock) {
			moveTasks.add(task);
			movingLock.notifyAll();
		}
	}
	
	/**
	 * cancels all tasks and stops robot.
	 */
	public void cancelTasks() {
		synchronized (movingLock) {
			moveTasks.clear();
		}
		synchronized (doneLock) {
			doneLock.notifyAll();
		}
		stopRobot();
	}
	
	/**
	 * set motors to given powerSet.
	 */
	private void setPower(MotorPowerSet p) {
		for (int i = 0; i < 4; i++) {
			motors[i].setPower(p.power[i]);
		}
	}
	
	/**
	 * Tells the robot to move in a specified direction and turnRate.
	 * Can handle motor powers greater than 1 -- it will scale them down.
	 */
	public void moveAt(double direction, double speed, double turnRate) {
		MotorPowerSet set = calcPowerSet(direction, speed, turnRate);
		set.scale();
		setPower(set);
	}
	
	/**
	 * Stops robot, i.e., set motor powers to zero.
	 */
	private void stopRobot() {
		setPower(ZERO);
	}
	
	public void waitForDone() {
		synchronized (doneLock) {
			while (hasTasks()) {
				try {
					doneLock.wait();
				} catch (InterruptedException ignored) {
				}
			}
		}
	}
	
	/**
	 * a set of powers for all 4 motors;
	 * just a container around double[4]
	 */
	public static class MotorPowerSet {
		double[] power;
		
		MotorPowerSet(double leftFront, double rightFront, double backLeft, double backRight) {
			this.power = new double[]{leftFront, rightFront, backLeft, backRight};
		}
		
		MotorPowerSet() {
			this.power = new double[4];
		}
		
		void scale() {
			double max = 1;
			for (int i = 0; i < 4; i++) {
				max = Math.max(max, power[i]);
			}
			for (int i = 0; i < 4; i++) {
				power[i] /= max;
			}
		}
	}
	
	private class MoveTask { //NOT STATIC, to access DC motors
		//don't want to spam new, so I have fields instead of vars.
		private MotorPowerSet targetPower, actualPower;
		private double multiplier;
		private double[] progress = new double[4];
		
		MoveTask(MotorPowerSet targetPower, double multiplier) {
			this.targetPower = targetPower;
			this.multiplier = multiplier;
		}
		
		private void start() {
			for (int i = 0; i < 4; i++) {
				motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				motors[i].setMode(DcMotor.RunMode.RUN_TO_POSITION);
				motors[i].setTargetPosition((int) (multiplier * targetPower.power[i]));
			}
			//scale only after target pos set
			targetPower.scale();
			setPower(targetPower);
		}
		
		//read motor positions and adjust them as necessary if they go off track.
		//supposed to make all the motors turn in unison.
		boolean process() {
			if (actualPower == null) {
				actualPower = new MotorPowerSet();
				start();
			}
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
			return totalOff < 50;
		}
		
	}
	
	private class MoveThread extends Thread {
		private boolean exitFlag;
		
		MoveThread() {
			exitFlag = false;
			this.setName("MoveThread");
			this.setPriority(Thread.currentThread().getPriority() - 1);
		}
		
		//continually run moveTasks;
		@Override
		public void run() {
			while (!exitFlag) try {
				synchronized (movingLock) {
					while (moveTasks.isEmpty()) {
						movingLock.wait();
					}
					MoveTask curTask = moveTasks.element();
					if (curTask.process()) {
						moveTasks.remove();
						if (moveTasks.isEmpty()) {
							synchronized (doneLock) {
								doneLock.notifyAll();
							}
							stopRobot();
						}
					}
				}
				Thread.sleep(1);
			} catch (InterruptedException ignored) {
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}
}
