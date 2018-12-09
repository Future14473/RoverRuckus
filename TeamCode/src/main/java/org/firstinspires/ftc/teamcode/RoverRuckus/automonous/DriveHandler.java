package org.firstinspires.ftc.teamcode.RoverRuckus.automonous;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RoverRuckus.Practice.HardwareTestBot;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Separate utility class to handle omnidirectional motion on mecanum wheels.
 */
public class DriveHandler {
	private static final MotorPowerSet ZERO = new MotorPowerSet(0, 0, 0, 0);
	//FIXME TODO FIXME TODO: we want to tweak these values.
	//AND IMPLEMENT THEM FIRST DUH
	public static float MOVE_MULT = 3000f; //change to tweak "move x meters" precisely. Degrees wheel turn per meter.
	public static float TURN_MULT = 10f; //change to tweak "rotate x deg" precisely.   Degrees wheel turn per Degrees robot turn
	private Queue<MoveTask> moveTasks; //the currentPos moveTasks to do;
	
	//NOW THE FUN STUFF, FOR AUTONOMOUS MOTION.
	//the motors
	//[ FL, FR, BL, BR ]
	private DcMotor[] motors;
	//we have a separate thread handling moveTasks. This is so the robot can still do other stuff
	//while this is happening at the same time.
	private boolean runThread;
	private Thread moveThread;
	
	/**
	 * construct by motors
	 */
	private DriveHandler(DcMotor leftFront, DcMotor rightFront, DcMotor backLeft, DcMotor backRight) {
		motors = new DcMotor[4];
		motors[0] = leftFront;
		motors[1] = rightFront;
		motors[2] = backLeft;
		motors[3] = backRight;
		runThread = false;
		moveThread = null;
		moveTasks = new ConcurrentLinkedQueue<>();
		for (int i = 0; i < 4; i++) {
			motors[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		}
	}
	public void setModeEncoder(){
		for (int i = 0; i < 4; i++) {
			motors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
	}
	/**
	 * construct by Ben Beilen Code
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
	
	/**
	 * starts the MoveTasks handling thread. A new thread might be created.
	 */
	public void startMoveThread() {
		runThread = true;
		if (moveThread == null) {
			moveThread = new Thread(new MoveThread());
			moveThread.start();
		}
	}
	
	/**
	 * stops the MoveTasks handling thread.
	 */
	public void stopMoveThread() {
		runThread = false;
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
		moveTasks.add(new MoveTask(calcPowerSet(direction, speed, 0), distance * MOVE_MULT));
	}
	
	/**
	 * ads a move task to rotate in place a specified number of degrees, positive or negative.
	 */
	public void turn(float degrees, float speed) {
		moveTasks.add(new MoveTask(calcPowerSet(0, speed, Math.signum(degrees)), degrees * TURN_MULT));
	}
	
	/**
	 * cancels all tasks and stops robot.
	 */
	public void cancelTasks() {
		stop();
		moveTasks.clear();
	}
	
	/*
	adds a move task to move the robot in such a curve as to Rotate the specified number of degrees
	AND land in the correct notation. Moves in a curved path.
	maybe i'm too ambitions lets not do this unless we need it.
	*/
//	public void curveTo(float direction, float distance, float degrees) {
//		//TODO: actually do this.
//
//	}
//
	
	/**
	 * set motors to given powerSet.
	 */
	public void setPower(MotorPowerSet p) {
		for (int i = 0; i < 4; i++)
			motors[i].setPower(p.power[i]);
		
	}
	
	/**
	 * Tells the robot to move in a specied direction and turnRate.
	 */
	public void moveAt(float direction, float speed, float turnRate) {
		setPower(calcPowerSet(direction, speed, turnRate));
	}
	
	/**
	 * Stops robot, i.e., set motor power levels to zero.
	 */
	public void stop() {
		setPower(ZERO);
	}
	
	/**
	 * a set of power levels for all 4 motors;
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
		MotorPowerSet(){
			this.power = new float[4];
		}
	}
	
	/**
	 * a task that handles making the robot uniformly turn its motors a specified number of
	 * degrees.
	 */
	public Telemetry telemetry;
	public void setTelemetry(Telemetry telemetry){
		this.telemetry= telemetry;
	}
	private class MoveTask { //NOT STATIC, to access DCmotors
		//dont want to spam new, so I have fields instead of vars.
		private MotorPowerSet targetPower, actualPower;
		private float multiplier;
		private float[] progress;
		
		MoveTask(MotorPowerSet targetPower, float multiplier) {
			this.targetPower = targetPower;
			this.multiplier = multiplier;
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
			telemetry.addLine("STARTED!!!");
			telemetry.addData("Target Power:", Arrays.toString(targetPower.power));
		}
		
		//read motor positions and adjust them as necessary if they go off track.
		//supposed to make all the motors turn in unison.
		boolean process() {
			float avgProgress = 0;
			for (int i = 0; i < 4; i++) {
				progress[i] = (float) motors[i].getCurrentPosition() / motors[i].getTargetPosition();
				avgProgress += progress[i];
				telemetry.addData("","Motor %d: currentPos: %d, targetPos: %d, progress: %f",i, motors[i].getCurrentPosition(),motors[i].getTargetPosition(), progress[i]);
			}
			avgProgress /= 4.000000000000000001; //so it cant be exactly equal to 1, so no divide by 0.
			telemetry.addData("Average Progress:",avgProgress);
			//adjust power as necessary..
			for (int i = 0; i < 4; i++) {
				actualPower.power[i] = targetPower.power[i] * (1 - progress[i]) / (1 - avgProgress);
				telemetry.addData("","Motor %d: Target power: %f, Actual power: %f", i, targetPower.power[i], actualPower.power[i]);
			}
			
			setPower(actualPower);
			return Math.abs(avgProgress - 1) < 0.02;
		}
		
	}
	
	private class MoveThread implements Runnable {
		private boolean isFirstTime;
		
		
		
		//continually run moveTasks;
		@Override
		public void run() {
			isFirstTime = true;
			telemetry.addLine("I STARTED!!!!");
			telemetry.update();
			while (true) {
				if (!runThread) return;
				if (!moveTasks.isEmpty()) {
					if (isFirstTime) {
						telemetry.addLine("FIRST TIME!!!!!");
						telemetry.update();
						isFirstTime = false;
						moveTasks.element().start();
					}
					if (moveTasks.element().process()) {
						telemetry.addLine("DONE");
						telemetry.update();
						moveTasks.remove();
						isFirstTime = true;
						if (moveTasks.isEmpty()) {
							for (int i = 0; i < 4; i++) {
								motors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
							}
						}
					}
				}
			}
		}
	}
}
