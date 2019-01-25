package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

/**
 * Represents a task for moving the robot in a specific manner.
 * These are executed by {@link MoveTaskExecutor} in the following manner:
 * {@link #start(MotorSet)} is called once when the task is executed, given a MotorSet,
 * and {@link #run(MotorSet)} is called continuously until it returns true or the task is stopped early.
 */
interface MoveTask {
	
	/**
	 * Run once on the MoveTask's start. Intended for things like setting RunModes.
	 *
	 * @param motors the set of motors to operate on
	 * @see MotorSet
	 */
	void start(MotorSet motors);
	
	/**
	 * Called continuously when the MoveTask is running.
	 *
	 * @param motors the set of motors to operate on
	 * @return if the task is completed or not
	 * @see MotorSet
	 */
	boolean run(MotorSet motors);
}
