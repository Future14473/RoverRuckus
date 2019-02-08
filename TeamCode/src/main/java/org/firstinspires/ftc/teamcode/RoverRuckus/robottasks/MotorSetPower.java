package org.firstinspires.ftc.teamcode.RoverRuckus.robottasks;

import java.util.Arrays;

/**
 * Represents a set of power levels for motors, and surrounding
 * calculations on motor power levels
 *
 * @see MotorSet
 */
@SuppressWarnings("Duplicates")
public class MotorSetPower {
	static final MotorSetPower ZERO = new MotorSetPower();
	private static final double MAX_POWER = 0.95;
	public final double[] power;
	
	/**
	 * Construct via 4 power levels
	 */
	private MotorSetPower(double fl, double fr, double bl, double br) {
		this.power = new double[]{fl, fr, bl, br};
	}
	
	public MotorSetPower() {
		this.power = new double[4];
	}
	
	
	/**
	 * Returns an new {@code MotorSetPower} that has its power levels scaled down such that
	 * no power level is greater than {@link #MAX_POWER}
	 */
	MotorSetPower scaled() {
		double max = MAX_POWER;
		for (int i = 0; i < 4; i++) {
			max = Math.max(max, Math.abs(power[i]));
		}
		MotorSetPower set = new MotorSetPower();
		for (int i = 0; i < 4; i++) {
			set.power[i] = this.power[i] * 0.95 / max;
		}
		return set;
	}
	
	/**
	 * adjusts the current power so the acceleration from pastPower
	 * to currentPower is no greater than maxAcceleration.
	 */
	public void smoothPower(MotorSetPower pastPower, double maxAcceleration) {
		for (int i = 0; i < 4; i++) {
			if (Math.abs(this.power[i] - pastPower.power[i]) <= maxAcceleration) continue;
			if (this.power[i] > pastPower.power[i]) {
				this.power[i] = this.power[i] - maxAcceleration;
			} else {
				this.power[i] = this.power[i] + maxAcceleration;
			}
		}
	}
	
	@Override
	public String toString() {
		return Arrays.toString(power);
	}
	
	/**
	 * Creates a new MotorSetPower that represents moving the robot in the specified direction, turnRate, and speed.
	 *
	 * @param direction the direction to move the robot
	 * @param moveSpeed the speed of all these operations
	 * @param turnRate  the wait at which the robot turns
	 * @return the calculated MotorSetPower
	 */
	public static MotorSetPower calcPower(double direction, double moveSpeed, double turnRate) {
		double robotAngle = direction + Math.PI / 4;
		double v1 = moveSpeed * Math.sin(robotAngle) + turnRate;
		double v2 = moveSpeed * Math.cos(robotAngle) - turnRate;
		double v3 = moveSpeed * Math.cos(robotAngle) + turnRate;
		double v4 = moveSpeed * Math.sin(robotAngle) - turnRate;
		return new MotorSetPower(v1, v2, v3, v4);
	}
}
