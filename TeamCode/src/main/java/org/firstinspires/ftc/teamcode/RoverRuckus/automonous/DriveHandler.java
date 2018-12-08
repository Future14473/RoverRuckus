package org.firstinspires.ftc.teamcode.RoverRuckus.automonous;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RoverRuckus.Practice.HardwareTestBot;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Separate utility class to handle omnidirectional motion.
 */
public class DriveHandler {
	public static final MotorPowerSet ZERO = new MotorPowerSet(0, 0, 0, 0);
	//FIXME TODO FIXME TODO: we want to tweak these values.
	//AND IMPLEMENT THEM FIRST DUH
	public static  float MOVE_MULT = 3000f; //change to tweak "move x meters" precisely. Degrees wheel turn per meter.
	private static final float TURN_MULT = 10f; //change to tweak "rotate x deg" precisely.   Degrees wheel turn per Degrees robot turn
	private Queue<MoveTask> moveTasks; //the currentPos moveTasks to do;
	
	//NOW THE FUN STUFF, FOR AUTONOMOUS MOTION.
	//the motors, stored as a 2x2 grid
	//[ (FL, FR), (BL, BR) ]
	private DcMotor[][] motors;
	//we have a separate thread handling moveTasks. This is so the robot can still do other stuff
	//while this is happening at the same time. Or, Benjamin just wants to play with Threads.
	private boolean runThread;
	private Thread moveThread;
	
	//construct by motors
	public DriveHandler(DcMotor leftFront, DcMotor rightFront, DcMotor backLeft, DcMotor backRight) {
		motors = new DcMotor[2][2];
		motors[0][0] = leftFront;
		motors[0][1] = rightFront;
		motors[1][0] = backLeft;
		motors[1][1] = backRight;
		runThread = false;
		moveThread = null;
		moveTasks = new ConcurrentLinkedQueue<>();
	}
	
	//construct by Ben Beilen Code
	public DriveHandler(HardwareTestBot r) {
		this(r.leftFront, r.rightFront, r.leftBack, r.rightBack);
	}
	
	//Turn wheels as to move robot in specified direction.
	//FIXME: have to test to make sure things go in the right direction
	public static MotorPowerSet calcPowerSet(float direction, float speed, float turnRate) {
		//DO THE MATH
		float robotAngle = (float) (direction - Math.PI / 4);
		float v1 = (float) (speed * Math.cos(robotAngle) - turnRate);
		float v2 = (float) (speed * Math.sin(robotAngle) + turnRate);
		float v3 = (float) (speed * Math.sin(robotAngle) - turnRate);
		float v4 = (float) (speed * Math.cos(robotAngle) + turnRate);
		//if any level is greater than 1, scale down to prevent robot going off course
		//if turnRate is 0 and speed <1, this wont be a problem.
		float max = Math.max(Math.max(Math.abs(v1), Math.abs(v2)), Math.max(Math.abs(v3), Math.abs(v4)));
		if (max < 1) max = 1;
		return new MotorPowerSet(v1 / max, v2 / max, v3 / max, v4 / max);
	}
	
	public void startMoveThread() {
		runThread = true;
		if (moveThread == null)
			moveThread = new Thread(new MoveThread());
	}
	
	public void stopMoveThread() {
		runThread = false;
		moveThread = null;
	}
	public boolean noTasks(){
		return moveTasks.isEmpty();
	}
	//adds a MoveTask to move in a straight line a specified direction and distance.
	public void moveTo(float direction, float speed, float distance) {
		moveTasks.add(new MoveTask(calcPowerSet(direction, speed, 0), distance * MOVE_MULT));
	}
	
	//ads a move task to rotate in place a specified number of degrees, positive or negative.
	public void turnTo(float degrees) {
		moveTasks.add(new MoveTask(calcPowerSet(0, 0, Math.signum(degrees)), degrees * TURN_MULT));
	}
	public void cancelTasks(){
		stop();
		moveTasks.clear();
	}
	//adds a move task to move the robot in such a curve as to Rotate the specified number of degrees
	//AND land in the correct notation. Moves in a curved path.
	//maybe i'm too ambitions lets not do this unless we need it.
	public void curveTo(float direction, float distance, float degrees) {
		//TODO: actually do this.
		
	}
	
	//given power set, set motors to said powerSet.
	public void setPower(MotorPowerSet p) {
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++) {
				motors[i][j].setPower(p.power[i][j]);
			}
	}
	
	public void moveAt(float direction, float speed, float turnRate) {
		setPower(calcPowerSet(direction, speed, turnRate));
	}
	
	//stop robot
	public void stop() {
		setPower(ZERO);
	}
	
	//a set of power levels for all 4 motors;
	//just a container around float[][]
	public static class MotorPowerSet {
		float[][] power;
		
		public MotorPowerSet(float leftFront, float rightFront, float backLeft, float backRight) {
			float[][] power = new float[2][2];
			power[0][0] = leftFront;
			power[0][1] = rightFront;
			power[1][0] = backLeft;
			power[0][1] = backRight;
			this.power = power;
		}
		
	}
	
	//a task that tells the robot to uniformly turn its motors a specified number of
	//degrees.
	//TODO: CURRENTLY USES ENCODER, IF NOT, THEN WE'LL MAKE IT TIME BASED INSTEAD OF ENCODER BASED
	private class MoveTask { //NOT STATIC, to access DCmotors
		//dont want to spam new, so I have fields instead of vars.
		private MotorPowerSet targetPower, actualPower;
		private float multiplier;
		private float[][] progress;
		
		MoveTask(MotorPowerSet targetPower, float multiplier) {
			this.targetPower = targetPower;
			this.multiplier = multiplier;
			this.actualPower = new MotorPowerSet(0, 0, 0, 0);
			progress = new float[2][2];
		}
		
		public void start() {
			for (int i = 0; i < 2; i++)
				for (int j = 0; j < 2; j++) {
					motors[i][j].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
					motors[i][j].setMode(DcMotor.RunMode.RUN_TO_POSITION);
					motors[i][j].setTargetPosition((int) (multiplier * targetPower.power[i][j]));
					setPower(targetPower);
				}
		}
		
		//read motor positions and adjust them as necessary if they go off track.
		//supposed to make all the motors turn in unison.
		public boolean process() {
			float avgProgress = 0;
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					progress[i][j] = (float) motors[i][j].getCurrentPosition() / motors[i][j].getTargetPosition();
					avgProgress += progress[i][j];
				}
			}
			avgProgress /= 4.000000000000000001; //so it cant be exactly equal to 1, so no divide by 0.
			//adjust power as necessary..
			for (int i = 0; i < 2; i++)
				for (int j = 0; j < 2; j++) {
					actualPower.power[i][j] = targetPower.power[i][j] * (1 - progress[i][j]) / (1 - avgProgress);
				}
			setPower(actualPower);
			return Math.abs(avgProgress - 1) < 0.01;
		}
		
	}
	
	private class MoveThread implements Runnable {
		private boolean isFirstTime;
		
		//continually run moveTasks;
		@Override
		public void run() {
			isFirstTime = true;
			while (true) {
				if (!runThread) return;
				if (!moveTasks.isEmpty()) {
					if (isFirstTime) {
						isFirstTime = false;
						moveTasks.peek().start();
					}
					if (moveTasks.peek().process()) {
						moveTasks.remove();
						isFirstTime = true;
					}
				}
			}
		}
	}
	
	
}
