package org.firstinspires.ftc.teamcode.RoverRuckus.util.robot;

import java.util.function.DoubleSupplier;

import static java.lang.Math.PI;

/**
 * A utility wrapper, used for angle measurement, that keeps track of
 * differences
 * in angles as opposed to direct angle measurement.
 * position cumulatively (can do multiple revolutions).
 */
class CumulativeDirection implements DoubleSupplier {
	private final DoubleSupplier rawDirection;
	private       double         direction = 0;
	
	/**
	 * Construct via DoubleSupplier for direction, IN RADIANS.
	 */
	public CumulativeDirection(DoubleSupplier rawDirection) {
		//paranoia!!!
		this.rawDirection =
				rawDirection instanceof CumulativeDirection ?
				((CumulativeDirection) rawDirection).rawDirection :
				rawDirection;
	}
	
	@Override
	public double getAsDouble() {
		double curRawDirection = rawDirection.getAsDouble();
		double diffDirection =
				((curRawDirection - direction) % (2 * PI) + 3 * PI) % (2 * PI) - PI;
		//RobotLog.dd("CumulativeDir", "DiffDirection: %.5f", diffDirection);
		direction += diffDirection;
		return direction;
	}
	
}
