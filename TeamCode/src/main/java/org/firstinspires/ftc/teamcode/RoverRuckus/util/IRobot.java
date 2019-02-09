package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.MotorSet;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.TaskAdapter;

/**
 * The interface by which {@link TaskAdapter}s
 * can interact with the robot.
 * Implement on the actual robot, and passed as parameter to TaskExecutor implementation
 */
public interface IRobot {
	MotorSet getWheelMotors();
	
	double getDirection();
}