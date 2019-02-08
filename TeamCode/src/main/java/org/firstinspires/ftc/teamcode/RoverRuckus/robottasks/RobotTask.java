package org.firstinspires.ftc.teamcode.RoverRuckus.robottasks;

/**
 * Represents a task to be run by the robot, autonomously.
 * These are executed by {@link RobotTaskExecutor} in the following manner:
 * {@link #start(MotorSet)} is called once when the task is executed, given a {@link IRobot} interface
 * and {@link #run(MotorSet)} is called continuously until it returns true or the task is stopped early.
 *
 * @apiNote "Customized" Runnable
 */
public interface RobotTask {
	/**
	 * Run once on the RobotTask's start.
	 *
	 * @param motors the set of motors to operate on
	 * @see IRobot
	 */
	default void start(IRobot IRobot) throws InterruptedException {}
	
	/**
	 * Called continuously when the RobotTask is running.
	 *
	 * @return if the task is completed or not
	 * @see IRobot
	 */
	default boolean loop() {
		return true;
	}
	
}
