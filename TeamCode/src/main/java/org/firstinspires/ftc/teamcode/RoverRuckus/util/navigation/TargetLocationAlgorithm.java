package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

/**
 * A interface to represents an algorithm that calculates
 * a target movement given current positions and angle.
 */
public interface TargetLocationAlgorithm {
	/**
	 * Calculates power to move the robot given target
	 * and current positions and angle.
	 */
	XYR getPower(XYR targetPosition, XYR currentPosition,
	             Magnitudes maxVelocities,
	             double elapsedTime);
}
