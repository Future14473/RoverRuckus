package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

/**
 * Represents a set of power levels for motors, and surrounding
 * calculations on motor power levels
 * @see MotorSet
 */
class MotorPowerSet {
	static final MotorPowerSet ZERO = new MotorPowerSet();
	private static final double MAX_POWER = 0.95;
	static double MOVE_MULT = 4450; //change to tweak "move x meters" precisely. Degrees wheel turn per unit.
	static double TURN_MULT = 1205; //change to tweak "rotate x deg" precisely.   Degrees wheel turn per
	double[] power;
	
	/**
	 * Construct via 4 power levels
	 */
	MotorPowerSet(double fl, double fr, double bl, double br) {
		this.power = new double[]{fl, fr, bl, br};
	}
	
	MotorPowerSet() {
		this.power = new double[4];
	}
	
	/**
	 * Returns an new {@code MotorPowerSet} that has its power levels scaled down such that
	 * no power level is greater than {@link #MAX_POWER}
	 */
	MotorPowerSet scaled() {
		double max = MAX_POWER;
		for (int i = 0; i < 4; i++) {
			max = Math.max(max, Math.abs(power[i]));
		}
		MotorPowerSet set = new MotorPowerSet();
		for (int i = 0; i < 4; i++) {
			set.power[i] = this.power[i] * 0.95 / max;
		}
		return set;
	}
	
	/**
	 * Creates a new MotorPowerSet that represents moving the robot in the specified direction, turnRate, and speed.
	 * @param direction the direction to move the robot
	 * @param turnRate the wait at which the robot turns
	 * @param speed the speed of all these operations
	 * @return the calculated MotorPowerSet
	 */
	static MotorPowerSet calcPowerSet(double direction, double turnRate, double speed) {
		double robotAngle = direction + Math.PI / 4;
		double v1 = speed * Math.sin(robotAngle) + turnRate;
		double v2 = speed * Math.cos(robotAngle) - turnRate;
		double v3 = speed * Math.cos(robotAngle) + turnRate;
		double v4 = speed * Math.sin(robotAngle) - turnRate;
		return new MotorPowerSet(v1, v2, v3, v4);
	}
	
}
