package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

import com.qualcomm.robotcore.util.Range;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

public abstract class UniformMoveTask implements MoveTask {
	private final double speed;
	private final double[] progress = new double[4];
	private MotorSetPower targPower, actualPower;
	private MotorSetPosition targPos, curPos;
	
	public UniformMoveTask(MotorSetPower targPower, double speed, double mult) {
		this.targPower = targPower;
		this.speed = speed;
		this.targPos = new MotorSetPosition(targPower, mult);
		actualPower = new MotorSetPower();
	}
	
	protected void updateCurPos(MotorSet motors) {
		curPos = motors.getCurrentPosition();
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
	public void start(MotorSet motors) {
		motors.setMode(STOP_AND_RESET_ENCODER);
		motors.setMode(RUN_TO_POSITION);
		motors.setTargetPosition(getTargPos());
	}
	
	protected void setPower(double avgProgress, MotorSet motors) {
		for (int i = 0; i < 4; i++) {
			actualPower.power[i] = (targPower.power[i] * (1 - 3 * (progress[i] - avgProgress))) * speed;
		}
		//RobotLog.v("SET POWER: %s", actualPower.toString());
		motors.setPower(actualPower);
	}
	
	public MotorSetPosition getTargPos() {
		return targPos;
	}
	
}
