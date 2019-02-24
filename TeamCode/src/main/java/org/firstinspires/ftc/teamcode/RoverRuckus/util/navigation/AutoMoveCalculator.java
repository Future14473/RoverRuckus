package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public interface AutoMoveCalculator {
	/**
	 * Sets the target position for calculations.
	 * Default target position is {(0,0),0}
	 */
	void setTargetPosition(@NotNull XYR targetPosition);
	
	/**
	 * Sets the maximum allowed translational and angular velocities.
	 * A magnitude of 0 represents infinity.
	 * Default max velocities are 0 (which is infinity).
	 */
	void setMaxVelocities(@NotNull Magnitudes maxVelocities);
	
	/**
	 * Gives a XYR representing the direction to move and turnRate, given the current
	 * position and elapsed time. Actual calculation depends on implementation.
	 */
	@NotNull XYR getMovement(XYR currentPosition, double elapsedTime);
}
