package org.firstinspires.ftc.teamcode.RoverRuckus.util.robot;

import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskAdapter;

/**
 * The interface by which {@link TaskAdapter}s
 * can interact with the robot.
 * Implement on the actual robot, and passed as parameter to TaskExecutor
 * implementation
 */
public interface IRobot {
	MotorSet getWheels();
	
	/**
	 * Gets the orientation of the robot, on a plane parallel to the floor.
	 * In DEGREES, 0 is forward, positive is clockwise.
	 *
	 * @see org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDrive
	 */
	double getAngle();
}