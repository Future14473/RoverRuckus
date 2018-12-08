package org.firstinspires.ftc.teamcode.RoverRuckus.automonous;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RoverRuckus.Practice.HardwareTestBot;

import java.util.Date;
import java.util.Queue;

/**
 * Separate utility class to handle omnidirectional motion.
 */
public class DriveHandler {
	//FIXME TODO FIXME TODO: we want to tweak these values.
	//AND IMPLEMENT THEM FIRST DUH
	private static final float MOVE_MULT = 1f; //change to tweak "move x meters" precisely
	private static final float TURN_MULT = 1f; //change to tweak "rotate x deg" precisely
	public static final MotorPowerSet ZERO = new MotorPowerSet(0, 0, 0, 0);
	//the motors, stored as a 2x2 grid
	//[ (FL, FR), (BL, BR) ]
	private DcMotor[][] motors;
	
	//NOW THE FUN STUFF, FOR AUTONOMOUS MOTION.
	//A "task" that is put in a queue, for controlling robot motion.
	private static abstract class Task {
		public abstract void process();
		public abstract boolean isDone();
	}
	//a task that tells the robot to uniformly turn its motors a specified number of
	//degrees each.
	private class MoveTask extends Task { //NOT STATIC, to access DCmotors
		private MotorPowerSet motorPowerSet;
		//TODO: constructor.
		@Override
		public void process() {
			//TODO
			//Will read current motor positoin and target position, and possibly adjust motor
			//power levels as to try and even out motion if a motor deviates too far from
			//its expected track
			
			//TODO
			//experiment if getCurrentPosition is accurate. If not, then we can only YOLO/guess
			//it.
		}
		
		@Override
		public boolean isDone() {
			//TODO
			//if motor positions is close enough to target positions.
			return false;
		}
	}
	//This is just a filler method in the queue that just waits.
	private static class WaitTask extends Task { //is static
		private long duration;
		private long stopTime;
		WaitTask(long duration){
			this.duration=duration;
			this.stopTime = -1;
		}
		@Override
		public boolean isDone() {
			return System.currentTimeMillis() > stopTime;
		}
		
		@Override
		public void process() {
			if(stopTime==-1)stopTime=System.currentTimeMillis() + duration;
		}
	}
	
	Queue<Task> moveTasks; //the current moveTasks to do;
	//we have a separate thread handling moveTasks. This is so the robot can still do other stuff
	//while this is going. Or, Benjamin just wants to play with Threads.
	private MoveThread moveThread;
	
	private class MoveThread implements Runnable {
		@Override
		public void run() {
			//runs forever
			//TODO
		}
	}
	
	
	//construct by motors
	DriveHandler(DcMotor leftFront, DcMotor rightFront, DcMotor backLeft, DcMotor backRight) {
		motors[0][0] = leftFront;
		motors[0][1] = rightFront;
		motors[1][0] = backLeft;
		motors[1][1] = backRight;
		
		setup();
	}
	
	//construct by Ben Beilen Code
	DriveHandler(HardwareTestBot r) {
		this(r.leftFront, r.rightFront, r.leftBack, r.rightBack);
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
	
	//Move robot in specified direction.
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
		return new MotorPowerSet(v1 / max, v2 / max, v3 / max, v4 / max);
	}
	
	public void setup() { //make sure omniwheels can work
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++) {
				//make sure brake on stop;
				motors[i][j].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			}
	}
	
	//given power set, set motors to said powerSet.
	public void setTo(MotorPowerSet p) {
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++) {
				motors[i][j].setPower(p.power[i][j]);
			}
	}
	
	public void move(float direction, float speed, float turnRate) {
		setTo(calcPowerSet(direction, speed, turnRate));
	}
	
	//stop robot
	public void stop() {
		setTo(ZERO);
	}
}
