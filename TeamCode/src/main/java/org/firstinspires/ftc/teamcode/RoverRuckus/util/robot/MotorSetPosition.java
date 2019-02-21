package org.firstinspires.ftc.teamcode.RoverRuckus.util.robot;

import java.util.Arrays;

/**
 * Represents a set of power levels for motors, and surrounding
 * calculations on motor power levels.
 * Immutable.
 *
 * @see MotorSet
 */
public final class MotorSetPosition {
	private final static MotorSetPosition ZERO = new MotorSetPosition();
	
	private final int[] position;
	
	private MotorSetPosition() {
		this.position = new int[4];
	}
	
	public MotorSetPosition(MotorSetPower power, double mult) {
		this.position = new int[4];
		for (int i = 0; i < 4; i++) {
			position[i] = (int) Math.round(power.getPower(i) * mult);
		}
	}
	
	private MotorSetPosition(int[] o) {
		this.position = o.clone();
	}
	
	public int getPosition(int i) {
		return position[i];
	}
	
	public int get(int i) {
		return position[i];
	}
	
	@Override
	public String toString() {
		return Arrays.toString(position);
	}
	
	public MotorSetPosition subtract(MotorSetPosition other) {
		if (other == null) return this;
		int[] position = new int[4];
		for (int i = 0; i < 4; i++) {
			position[i] = this.position[i] - other.position[i];
		}
		return fromArray(position);
	}
	
	public static MotorSetPosition fromArray(int[] array) {
		if (Arrays.equals(array, ZERO.position)) return ZERO;
		return new MotorSetPosition(array);
	}
	
	public int[] toArray() {
		return position.clone();
	}
}
