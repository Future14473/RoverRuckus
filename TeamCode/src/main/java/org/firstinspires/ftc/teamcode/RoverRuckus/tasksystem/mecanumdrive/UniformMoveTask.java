package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.mecanumdrive;

import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.MotorSet;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.MotorSetPosition;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.MotorSetPower;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.TaskAdapter;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.IRobot;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

//TODO: cleanup
abstract class UniformMoveTask extends TaskAdapter {
	public final static double TURN_MULT = 1205; //change to tweak "rotate x deg" precisely.   Degrees wheel turn per
	public final static double MOVE_MULT = 4450; //change to tweak "move x meters" precisely. Degrees wheel turn per
	// unit.
	//TODO: Make above protected
	private final double speed;
	private final double[] progress = new double[4];
	protected MotorSet motors;
	private MotorSetPower targPower, actualPower;
	private MotorSetPosition targPos, curPos;
	protected final IRobot robot;
	
	protected UniformMoveTask(IRobot robot, MotorSetPower targPower, double speed, double mult) {
		this.targPower = targPower;
		this.speed = speed;
		this.robot = robot;
		this.targPos = new MotorSetPosition(targPower, mult);
		actualPower = new MotorSetPower();
	}
	
	protected void updateCurPos() {
		curPos = this.motors.getCurrentPosition();
	}
	
	protected int getMaxOff() {
		int maxOff = 0;
		for (int i = 0; i < 4; i++) {
			maxOff = Math.max(maxOff, Math.abs(curPos.get(i) - getTargPos().get(i)));
		}
		return maxOff;
	}
	
	protected double getAvgProgress() {
		double avgProgress = 0;
		for (int i = 0; i < 4; i++) {
			progress[i] = ((double) curPos.get(i)) / getTargPos().get(i);
			if (Double.isNaN(progress[i])) progress[i] = 1;
			avgProgress += progress[i];
		}
		return Range.clip(avgProgress / 4, 0, 1);
	}
	
	//Reset position, set target position.
	@Override
	public void start() {
		this.motors = this.robot.getWheelMotors();
		motors.setMode(STOP_AND_RESET_ENCODER);
		motors.setMode(RUN_TO_POSITION);
		motors.setTargetPosition(getTargPos());
	}
	
	protected void setPower(double avgProgress) {
		for (int i = 0; i < 4; i++) {
			actualPower.power[i] = (targPower.power[i] * (1 - 3 * (progress[i] - avgProgress))) * speed;
		}
		//RobotLog.v("SET POWER: %s", actualPower.toString());
		this.motors.setPower(actualPower);
	}
	
	protected MotorSetPosition getTargPos() {
		return targPos;
	}
	
}
