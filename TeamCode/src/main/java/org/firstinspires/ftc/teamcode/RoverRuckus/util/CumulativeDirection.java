package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import java.util.function.DoubleSupplier;

/**
 * A utility wrapper, used for angle measurement, that keeps track of differences
 * in angles as opposed to direct angle measurement.
 * position cumulatively (can do multiple revolutions).
 */
class CumulativeDirection implements DoubleSupplier {
	private final DoubleSupplier directionInDegrees;
	private double curDirection;
	
	public CumulativeDirection(DoubleSupplier directionInRadians) {
		//paranoia!!!
		this.directionInDegrees = directionInRadians instanceof CumulativeDirection ?
				((CumulativeDirection) directionInRadians).directionInDegrees : directionInRadians;
		this.curDirection = directionInRadians.getAsDouble();
	}
	
	@Override
	public double getAsDouble() {
		double newDirection = directionInDegrees.getAsDouble();
		curDirection += ((newDirection - curDirection) + Math.PI) % (2 * Math.PI) - Math.PI;
		return curDirection;
	}
	
}
