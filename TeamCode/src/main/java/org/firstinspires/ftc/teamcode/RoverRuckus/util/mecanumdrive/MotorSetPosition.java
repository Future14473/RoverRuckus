package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

import java.util.Arrays;

/**
 * Represents a set of power levels for motors, and surrounding
 * calculations on motor power levels
 *
 * @see MotorSet
 */
public class MotorSetPosition {
	final static MotorSetPosition ZERO = new MotorSetPosition();
	final static double MOVE_MULT = 4450; //change to tweak "move x meters" precisely. Degrees wheel turn per unit.
	final static double TURN_MULT = 1205; //change to tweak "rotate x deg" precisely.   Degrees wheel turn per
	final int[] position;
	
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
	
	int get(int i) {
		return position[i];
	}
	
	@Override
	public String toString() {
		return Arrays.toString(position);
	}
}
