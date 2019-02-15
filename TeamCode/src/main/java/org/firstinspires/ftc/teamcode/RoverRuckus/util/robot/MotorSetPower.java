package org.firstinspires.ftc.teamcode.RoverRuckus.util.robot;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.XY;

import java.util.Arrays;

/**
 * Represents a set of power levels for motors.
 * Contains other calculations regarding motor set powers.
 * Immutable.
 *
 * @see MotorSet
 */
public final class MotorSetPower {
	public static final MotorSetPower ZERO = new MotorSetPower();
	public static final MotorSetPower TURN = new MotorSetPower(1, -1, 1, -1);
	private final       double[]      power;
	
	private MotorSetPower(double fl, double fr, double bl, double br) {
		this.power = new double[]{fl, fr, bl, br};
	}
	
	private MotorSetPower() {
		this.power = new double[4];
	}
	
	private MotorSetPower(double[] power) {
		if (power.length != 4) throw new AssertionError();
		this.power = power.clone();
	}
	
	public double getPower(int i) {
		return power[i];
	}
	
	/**
	 * Returns an new {@code MotorSetPower} that has its power levels scaled
	 * down such that no power level is greater than the given maxPower.
	 *
	 * @param maxPower
	 */
	
	public MotorSetPower scaleDownTo(double maxPower) {
		if (maxPower <= 0) throw new IllegalArgumentException();
		if (this == ZERO) return this;
		double max = maxPower;
		for (int i = 0; i < 4; i++) {
			max = Math.max(max, Math.abs(power[i]));
		}
		double[] o = new double[4];
		for (int i = 0; i < 4; i++) {
			o[i] = this.power[i] * maxPower / max;
		}
		return fromArray(o);
	}
	
	/**
	 * Creates a new MotorSetPower that adjusts the power such that the
	 * difference between this power and given power is no greater
	 * than maxAcceleration.
	 */
	public MotorSetPower rampFrom(MotorSetPower pastPower, double rampRate) {
		if (rampRate <= 0) throw new IllegalArgumentException();
		double[] o = new double[4];
		for (int i = 0; i < 4; i++) {
			if (Math.abs(this.power[i]-pastPower.power[i]) <= rampRate) o[i] = this.power[i];
			else o[i] = this.power[i] < pastPower.power[i] ? pastPower.power[i]-rampRate :
			            pastPower.power[i]+rampRate;
		}
		return fromArray(o);
	}
	
	/**
	 * Return a new MotorSetPower which is this MotorSetPower's powers
	 * multiplied by mult
	 *
	 * @param mult the multiplier
	 * @return a new MotorSetPower which is this's power multiplied by mult
	 */
	
	public MotorSetPower scaleTo(double mult) {
		if (this == ZERO) return ZERO;
		return new MotorSetPower(power[0] * mult,
		                         power[1] * mult,
		                         power[2] * mult,
		                         power[3] * mult);
	}
	
	@Override
	public String toString() {
		return String.format("MotorSetPower: {%s}", Arrays.toString(power));
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
	public static MotorSetPower calcPolarNonstandard(
		double direction, double moveSpeed, double turnRate) {
		return calcPolar(Math.PI / 2-direction, moveSpeed, turnRate);
	}
	
	/**
	 * Creates a MotorSetPower that represents moving the robot in the
	 * specified direction, turnRate, and speed.
	 *
	 * @param direction the direction to move the robot, in radians
	 * @param moveSpeed the speed of all these operations
	 * @param turnRate  the rate at which the robot turns
	 * @return the calculated MotorSetPower
	 */
	public static MotorSetPower calcPolar(
		double direction, double moveSpeed, double turnRate) {
		if (moveSpeed == 0) {
			if (turnRate == 0) return ZERO;
			if (turnRate == 1) return TURN;
		}
		double robotAngle = direction-Math.PI / 4;
		double v1 = moveSpeed * Math.cos(robotAngle)+turnRate;
		double v2 = moveSpeed * Math.sin(robotAngle)-turnRate;
		double v3 = moveSpeed * Math.sin(robotAngle)+turnRate;
		double v4 = moveSpeed * Math.cos(robotAngle)-turnRate;
		return new MotorSetPower(v1, v2, v3, v4);
	}
	
	/**
	 * Creates a new MotorSetPower that represents moving the robot in the
	 * specified direction in rectilinear coordinates.
	 *
	 * @param direction the direction to move the robot
	 * @param moveSpeed the speed of all these operations
	 * @param turnRate  the wait at which the robot turns
	 * @return the calculated MotorSetPower
	 */
	public static MotorSetPower calcRectilinear(
		double x, double y, double turnRate) {
		return calcPolar(Math.atan2(y, x), Math.hypot(x, y), turnRate);
	}
	
	public static MotorSetPower fromArray(double[] power) {
		if (power.length != 4) throw new IllegalArgumentException();
		if (Arrays.equals(power, ZERO.power)) return ZERO;
		if (Arrays.equals(power, TURN.power)) return TURN;
		return new MotorSetPower(power);
	}
	
	public static MotorSetPower fromXY(XY curMovement, double turnRate) {
		return calcRectilinear(curMovement.x, curMovement.y, turnRate);
	}
}
