package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Separate utility class to handle omnidirectional motion on mecanum wheels.
 */
public class DriveHandler {
	private static final MotorPowerSet ZERO = new MotorPowerSet(0, 0, 0, 0);
	private static final int DEFAULT_WAITTIME = 200;
	//FIXME TODO FIXME TODO: we want to tweak these values.
	public static float MOVE_MULT = 4450f; //change to tweak "move x meters" precisely. Degrees wheel turn per unit.
	public static float TURN_MULT = 1205f; //change to tweak "rotate x deg" precisely.   Degrees wheel turn per
	// radians robot turn
	/**
	 * a task that handles making the robot uniformly turn its motors a specified number of
	 * degrees.
	 */
	private Telemetry telemetry;
	private Queue<MoveTask> moveTasks; //the currentPos moveTasks to do;
	//NOW THE FUN STUFF, FOR AUTONOMOUS MOTION.
	//the motors
	//[ FL, FR, BL, BR ]
	private DcMotor[] motors;
	//we have a separate thread handling moveTasks. This is so the robot can still do other stuff
	//while this is happening at the same time.
	private MoveThread moveThread;
	private final Object moveLock = new Object();
	public boolean moveEndFlag = false;
	//keeping track of location.
	private float curX, curY;
	
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
	}
	
	/**
	 * construct by Ben Bielin Code
	 */
	public DriveHandler(HardwareTestBot r) {
		this(r.leftFront, r.rightFront, r.leftBack, r.rightBack);
	}
	
	/**
	 * Generates a MotorPowerSet that corresponds to turning the robot in the specified direction
	 */
	private static MotorPowerSet calcPowerSet(float direction, float speed, float turnRate) {
		//DO THE MATH
		float robotAngle = (float) (direction + Math.PI / 4);
		float v1 = (float) (speed * Math.sin(robotAngle) + turnRate);
		float v2 = (float) (speed * Math.cos(robotAngle) - turnRate);
		float v3 = (float) (speed * Math.cos(robotAngle) + turnRate);
		float v4 = (float) (speed * Math.sin(robotAngle) - turnRate);
		//if any level is greater than 1, scale down to prevent robot going off course
		//if turnRate is 0 and speed <1, this wont be a problem.
		float max = Math.max(Math.max(Math.abs(v1), Math.abs(v2)), Math.max(Math.abs(v3), Math.abs(v4)));
		if (max < 1) max = 1;
		return new MotorPowerSet(v1 / max, v2 / max, v3 / max, v4 / max);
	}
	
	public void resetCoords() {
		curX = curY = 0;
	}
	
	public float getCurX() {
		return curX;
	}
	
	public float getCurY() {
		return curY;
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
	 * stops the MoveTasks handling thread.
	 */
	public void stopMoveThread() {
		cancelTasks();
		moveThread.exit();
		moveThread = null;
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
	public void move(float direction, float speed, float distance) {
		move(direction, speed, distance, DEFAULT_WAITTIME);
	}
	
	public void move(float direction, float speed, float distance, int waitTime) {
		moveTasks.add(new MoveTask(calcPowerSet(direction, speed, 0), distance * MOVE_MULT / speed, distance,
			direction, waitTime));
		synchronized (moveLock) {
			moveLock.notify();
		}
	}
	
	/**
	 * ads a move task to rotate in place a specified number of degrees, positive or negative.
	 */
	public void turn(float degrees, float speed) {
		turn(degrees, speed, DEFAULT_WAITTIME);
	}
	
	public void turn(float degrees, float speed, int waitTime) {
		moveTasks.add(new MoveTask(calcPowerSet(0, 0, speed * Math.signum(degrees)),
			degrees * TURN_MULT / speed,
			0, 0, waitTime));
		synchronized (moveLock) {
			moveLock.notify();
		}
	}
	
	//measured in given arbitrary units
	public void moveTo(float x, float y, float speed) {
		float dx = curX - x, dy = curY - y;
		float angle = (float) Math.atan2(x, y);
		float distance = (float) Math.hypot(dx, dy);
		move(angle, speed, distance);
	}
	
	public void waitForDone() {
		while (hasTasks()) ;
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
	public void moveAt(float direction, float speed, float turnRate) {
		setPower(calcPowerSet(direction, speed, turnRate));
	}
	
	/**
	 * Stops robot, i.e., set motor power levels to zero.
	 */
	public void stopRobot() {
		setPower(ZERO);
	}
	
	/**
	 * a set of power levels for algit rm --cached -r .ideal 4 motors;
	 * just a container around float[][]
	 */
	public static class MotorPowerSet {
		float[] power;
		
		MotorPowerSet(float leftFront, float rightFront, float backLeft, float backRight) {
			float[] power = new float[4];
			power[0] = leftFront;
			power[1] = rightFront;
			power[2] = backLeft;
			power[3] = backRight;
			this.power = power;
		}
		
		MotorPowerSet() {
			this.power = new float[4];
		}
	}
	
	private class MoveTask { //NOT STATIC, to access DCmotors
		//dont want to spam new, so I have fields instead of vars.
		private MotorPowerSet targetPower, actualPower;
		private float multiplier;
		private float[] progress;
		private int waitTime;
		private float dx, dy;
		
		MoveTask(MotorPowerSet targetPower, float multiplier, float distance, float angle, int waitTime) {
			this.targetPower = targetPower;
			this.multiplier = multiplier;
			this.waitTime = waitTime;
			this.dx = (float) (distance * Math.sin(angle));
			this.dy = (float) (distance * Math.cos(angle));
			this.actualPower = new MotorPowerSet();
			progress = new float[4];
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
			float avgProgress = 0;
			for (int i = 0; i < 4; i++) {
				progress[i] = (float) motors[i].getCurrentPosition() / motors[i].getTargetPosition();
				avgProgress += progress[i];
				telemetry.addData("", "Motor %d: currentPos: %d, targetPos: %d, progress: %f",
					i, motors[i].getCurrentPosition(), motors[i].getTargetPosition(), progress[i]);
			}
			avgProgress /= 4.000000000000000001; //so it cant be exactly equal to 1, so no divide by 0.
			telemetry.addData("Average Progress:", avgProgress);
			//adjust power as necessary..
			for (int i = 0; i < 4; i++) {
				actualPower.power[i] = targetPower.power[i] * (1 - progress[i]) / (1 - avgProgress);
				telemetry.addData("", "Motor %d: Target power: %f, Actual power: %f",
					i, targetPower.power[i], actualPower.power[i]);
			}
			telemetry.update();
			setPower(actualPower);
			return Math.abs(avgProgress - 1) < 0.02;
		}
		
	}
	
	private class MoveThread extends Thread {
		private boolean isFirstTime;
		private boolean exitFlag;
		
		void exit() {
			exitFlag = true;
		}
		
		MoveThread() {
			exitFlag = false;
		}
		
		//continually run moveTasks;
		@Override
		public void run() {
			isFirstTime = true;
			while (true) {
				if (exitFlag) return;
				try {
					synchronized (moveLock) {
						while (moveTasks.isEmpty()) {
							moveLock.wait();
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
						int waitTime = curTask.waitTime;
						curX += curTask.dx;
						curY += curTask.dy;
						moveTasks.remove();
						isFirstTime = true;
						moveEndFlag = true;
						if (moveTasks.isEmpty()) {
							setModeEncoder();
						}
						Thread.sleep(waitTime);
					}
				} catch (NullPointerException | InterruptedException ignored) { }
			}
		}
	}
}