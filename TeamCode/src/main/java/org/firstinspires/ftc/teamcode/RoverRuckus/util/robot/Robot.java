package org.firstinspires.ftc.teamcode.RoverRuckus.util.robot;

import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskAdapter;

/**
 * The interface by which {@link TaskAdapter}s
 * can interact with the robot.
 * Implement on the actual robot, and passed as parameter to TaskExecutor
 * implementation
 */
public interface Robot {
	MotorSet getWheels();
	
	/**
	 * Gets the orientation of the robot, on a plane parallel to the floor.
	 * Normal orientation: radians, to the left is 0, Counterclockwise is
	 * positive.
	 * As long as relative positions are consistent, this angle doesn't have
	 * to mean anything.
	 */
	double getAngle();
}