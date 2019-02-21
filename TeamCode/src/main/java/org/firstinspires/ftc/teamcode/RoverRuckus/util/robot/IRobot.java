package org.firstinspires.ftc.teamcode.RoverRuckus.util.robot;

/**
 * The interface by which our library can interact with the robot.
 * To avoid "inadvertent snooping".
 */
public interface IRobot {
	/**
	 * Gets the MotorSet that represents the robot's wheels.
	 */
	MotorSet getWheels();
	
	/**
	 * Gets the orientation of the robot, on a plane parallel to the floor.
	 * Normal orientation: radians, to the left is 0, Counterclockwise is
	 * positive.
	 * As long as relative positions are consistent, specific angles do not have to mean much.
	 */
	double getAngle();
}