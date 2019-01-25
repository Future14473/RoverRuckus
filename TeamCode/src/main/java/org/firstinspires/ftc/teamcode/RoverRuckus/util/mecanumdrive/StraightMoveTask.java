package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

import com.qualcomm.robotcore.hardware.DcMotor;

public class StraightMoveTask implements MoveTask {
	private MotorPowerSet targetPower, actualPower = new MotorPowerSet();
	private double multiplier;
	private double[] progress = new double[4];
	
	StraightMoveTask(MotorPowerSet targetPower, double multiplier) {
		this.targetPower = targetPower;
		this.multiplier = multiplier;
	}
	
	@Override
	public void start(MotorSet motors) {
		for (int i = 0; i < 4; i++) {
			motors.get(i).setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			motors.get(i).setMode(DcMotor.RunMode.RUN_TO_POSITION);
			motors.get(i).setTargetPosition((int) (multiplier * targetPower.power[i]));
		}
		motors.setPowerTo(targetPower);
	}
	
	//read motor positions and adjust them as necessary if they go off track.
	//supposed to make all the motors turn in unison.
	@Override
	public boolean run(MotorSet motors) {
		double avgProgress = 0;
		int maxOff = 0;
		for (int i = 0; i < 4; i++) {
			progress[i] = (double) motors.get(i).getCurrentPosition() / motors.get(i).getTargetPosition();
			maxOff = Math.max(maxOff, Math.abs(motors.get(i).getCurrentPosition() - motors.get(i).getTargetPosition()));
			if (Double.isNaN(progress[i])) progress[i] = 1;
			avgProgress += progress[i];
		}
		avgProgress /= 4;
		//adjust power as necessary..
		for (int i = 0; i < 4; i++) {
			actualPower.power[i] = targetPower.power[i] * (1 - 3 * (progress[i] - avgProgress));
		}
		motors.setPowerTo(actualPower);
		return maxOff < 100;
	}
	
}
