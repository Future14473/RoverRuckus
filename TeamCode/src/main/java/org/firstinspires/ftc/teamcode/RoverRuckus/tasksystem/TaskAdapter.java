package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.IRobot;

/**
 * Adapts a task to use start, loop, and stop methods.
 * These are executed by  in the following manner:
 * {@link #start(IRobot)} is called once when the task is executed, given a {@link IRobot} interface
 * and {@link #loop()} is called continuously until it returns true or the task is stopped early.
 * {@link #stop()} is called at the end of the task, through normal execution or interruption.
 *
 * @see Task
 * @see TaskExecutor
 */
public abstract class TaskAdapter implements Task {
	/**
	 * Run once on the Task's start.
	 */
	public abstract void start() throws InterruptedException;
	
	@Override
	public final void run() throws InterruptedException {
		start();
		try {
			while (!loop()) {
				if (Thread.interrupted()) throw new InterruptedException();
			}
		} finally {
			stop();
		}
	}
	
	/**
	 * Runs at the end of execution.
	 *
	 * @apiNote inside a finally block.
	 */
	public void stop() {}
	
	/**
	 * Called continuously when the Task is running.
	 *
	 * @return if the task is completed or not
	 */
	public boolean loop() throws InterruptedException {
		return true;
	}
}
