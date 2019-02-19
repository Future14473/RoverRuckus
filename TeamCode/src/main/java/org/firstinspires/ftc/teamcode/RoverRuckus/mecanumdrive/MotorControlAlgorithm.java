package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

/**
 * A interface to represents an algorithm that calculates
 * motor powers given target and current positions and angle.
 */
public interface MotorControlAlgorithm {
	/**
	 * Calculates power to move the robot given target
	 * and current positions and angle.
	 */
	MotorSetPower getPower(
			XY targetLocation, XY currentLocation,
			double targetAngle, double currentAngle,
			double maxAngularSpeed, double maxTranslationSpeed);
}
