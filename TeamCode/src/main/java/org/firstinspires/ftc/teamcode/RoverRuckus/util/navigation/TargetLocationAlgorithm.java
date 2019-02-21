package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

/**
 * A interface to represents an algorithm that calculates
 * motor powers given target and current positions and angle.
 */
public interface TargetLocationAlgorithm {
	/**
	 * Calculates power to move the robot given target
	 * and current positions and angle.
	 */
	MotorSetPower getPower(XYR targetPosition, XYR currentPosition,
	                       double maxAngularSpeed, double maxTranslationSpeed,
	                       double elapsedTime);
}
