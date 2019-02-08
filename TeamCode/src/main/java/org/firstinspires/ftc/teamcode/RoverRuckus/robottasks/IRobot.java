package org.firstinspires.ftc.teamcode.RoverRuckus.robottasks;

/**
 * The interface by which {@link org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.RobotTask}s
 * can interact with the robot.
 * Implement on the actual robot, and pass as parameter to RobotTaskExecutor implementation
 */
public interface IRobot {
	MotorSet getWheelMotors();
	
	double getDirection();
	//OTHERS//
}
