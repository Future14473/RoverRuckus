package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * A {@link MoveTask} that is simply turning all the wheels
 * at a constant power, uniformly, for a constant time.
 */
public class StraightMoveTask implements MoveTask {
	private MotorPowerSet targetPower, actualPower = new MotorPowerSet();
	private int[] targPos = new int[4];
	private final double mult, speed;
	private double[] progress = new double[4];
	
	StraightMoveTask(MotorPowerSet targPower, double mult, double speed) {
		this.targetPower = targPower;
		this.mult = mult;
		this.speed = speed;
	}
	
	//Reset position, set target position.
	@Override
	public void start(MotorSet motors) {
		for (int i = 0; i < 4; i++) {
			targPos[i] = (int) (mult * targetPower.power[i]);
			motors.get(i).setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			motors.get(i).setMode(DcMotor.RunMode.RUN_TO_POSITION);
			motors.get(i).setTargetPosition(targPos[i]);
		}
		motors.setPower(targetPower);
	}
	
	//read motor positions and adjust them as necessary if they go off track relative to everyone else.
	@Override
	public boolean run(MotorSet motors) {
		double avgProgress = 0;
		int maxOff = 0;
		for (int i = 0; i < 4; i++) {
			int curPos = motors.get(i).getCurrentPosition();
			progress[i] = (double) curPos / targPos[i];
			maxOff = Math.max(maxOff, Math.abs(curPos - targPos[i]));
			if (Double.isNaN(progress[i])) progress[i] = 1;
			avgProgress += progress[i];
		}
		avgProgress /= 4;
		//adjust power as necessary..
		for (int i = 0; i < 4; i++) {
			actualPower.power[i] = (targetPower.power[i] * (1 - 3 * (progress[i] - avgProgress))) * speed;
		}
		motors.setPower(actualPower);
		return maxOff < 100;
	}
	
}
