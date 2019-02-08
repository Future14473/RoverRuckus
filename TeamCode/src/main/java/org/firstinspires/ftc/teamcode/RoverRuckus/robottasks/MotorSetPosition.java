package org.firstinspires.ftc.teamcode.RoverRuckus.robottasks;

import java.util.Arrays;

/**
 * Represents a set of power levels for motors, and surrounding
 * calculations on motor power levels
 *
 * @see MotorSet
 */
public class MotorSetPosition {
	private final static MotorSetPosition ZERO = new MotorSetPosition();
	public final int[] position;
	
	/**
	 * Construct via 4 power levels
	 */
	public MotorSetPosition(int fl, int fr, int bl, int br) {
		this.position = new int[]{fl, fr, bl, br};
	}
	
	public MotorSetPosition() {
		this.position = new int[4];
	}
	
	public MotorSetPosition(MotorSetPower power, double mult) {
		this.position = new int[4];
		for (int i = 0; i < 4; i++) {
			position[i] = (int) Math.round(power.power[i] * mult);
		}
	}
	
	public int get(int i) {
		return position[i];
	}
	
	@Override
	public String toString() {
		return Arrays.toString(position);
	}
}
