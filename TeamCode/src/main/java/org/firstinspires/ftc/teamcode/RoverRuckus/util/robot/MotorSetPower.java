package org.firstinspires.ftc.teamcode.RoverRuckus.util.robot;

import java.util.Arrays;

/**
 * Represents a set of power levels for motors.
 * Contains other calculations regarding motor set powers.
 * Immutable.
 *
 * @see MotorSet
 */
@SuppressWarnings("WeakerAccess")
public final class MotorSetPower {
	public static final MotorSetPower ZERO   = new MotorSetPower();
	public static final double        X_MULT = 1.2;
	
	private final double[] power;
	
	/**
	 * Construct from four power levels.
	 */
	private MotorSetPower(double fl, double fr, double bl, double br) {
		this.power = new double[]{fl, fr, bl, br};
	}
	
	private MotorSetPower() {
		this.power = new double[4];
	}
	
	private MotorSetPower(double[] power) {
		//if (power.length != 4) throw new AssertionError();
		this.power = power;
	}
	
	public double getPower(int i) {
		return power[i];
	}
	
	/**
	 * Returns an new {@code MotorSetPower} that has its power levels scaled
	 * down such that no power level is greater than the given maxPower.
	 */
	public MotorSetPower limitMagnitudeTo(double maxPower) {
		if (maxPower <= 0) throw new IllegalArgumentException();
		double max = getMaxPower();
		if (max < maxPower) return this;
		double[] o = new double[4];
		for (int i = 0; i < 4; i++) {
			o[i] = this.power[i] * maxPower / max;
		}
		return fromArray(o);
	}
	
	public double getMaxPower() {
		double max = 0;
		for (int i = 0; i < 4; i++) {
			max = Math.max(max, Math.abs(power[i]));
		}
		return max;
	}
	
	@Override
	public String toString() {
		return String.format("MotorSetPower: {%s}", Arrays.toString(power));
	}
	
	private static MotorSetPower fromPowers(double fl, double fr, double bl, double br) {
		return new MotorSetPower(fl, fr, bl, br);
	}
	
	/**
	 * Creates a  MotorSetPower that represents moving the robot in the
	 * specified direction, turnRate, and speed;
	 * using non-standard orientation (0 up, positive clockwise).
	 *
	 * @param direction the direction to move the robot, in radians
	 * @param moveSpeed the speed of all these operations
	 * @param turnRate  the rate at which the robot turns
	 * @return the calculated MotorSetPower
	 */
	@Deprecated
	public static MotorSetPower fromPolarNonStandard(
			double direction, double moveSpeed, double turnRate) {
		return fromPolar(Math.PI / 2 - direction, moveSpeed, turnRate);
	}
	
	/**
	 * Creates a MotorSetPower that represents moving the robot in the
	 * specified direction, turnRate, and speed.
	 *
	 * @param direction the direction to move the robot, in radians
	 * @param moveSpeed the speed to move the robot
	 * @param turnRate  the rate at which the robot turns while moving
	 * @return the calculated MotorSetPower
	 */
	public static MotorSetPower fromPolar(
			double direction, double moveSpeed, double turnRate) {
		double robotAngle = direction - Math.PI / 4;
		double v1 = moveSpeed * Math.cos(robotAngle) - turnRate;
		double v2 = moveSpeed * Math.sin(robotAngle) + turnRate;
		double v3 = moveSpeed * Math.sin(robotAngle) - turnRate;
		double v4 = moveSpeed * Math.cos(robotAngle) + turnRate;
		return new MotorSetPower(v1, v2, v3, v4);
	}
	
	/**
	 * Construct from diagonal components: less intermediary math.
	 *
	 * @param leftRate  The rate at which to move on the left diagonal
	 * @param rightRate The rate at which to move on the right diagonal
	 * @param turnRate  The rate at which to turn the robot
	 */
	public static MotorSetPower fromDiagonals(double rightRate, double leftRate, double turnRate) {
		return fromPowers(rightRate - turnRate, leftRate + turnRate, leftRate - turnRate,
		                  rightRate + turnRate);
	}
	
	public static MotorSetPower fromArray(double[] power) {
		if (power.length != 4) throw new IllegalArgumentException();
		return new MotorSetPower(power.clone());
	}
	
	@SuppressWarnings("unused")
	public static MotorSetPower fromXY(double x, double y, double turnRate) {
		x *= X_MULT;
		double direction = Math.atan2(y, x);
		double speed = Math.hypot(x, y);
		return fromPolar(direction, speed, turnRate);
	}
}
