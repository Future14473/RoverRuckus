package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem;

/**
 * Adapts a task to use start, loop, and close methods.
 * These are executed by  in the following manner:
 * {@link #start} is called once when the task is executed
 * and {@link #loop()} is called continuously until it returns true or the
 * task is stopped early.
 * {@link #stop()} is called at the end of the task, through normal execution
 * or interruption.
 *
 * @see Runnable
 * @see TaskExecutor
 */
public abstract class TaskAdapter implements Task {
	/**
	 * Run once on the Task's start.
	 */
	public abstract void start();
	
	@Override
	public final void run() {
		start();
		try {
			//noinspection StatementWithEmptyBody
			while (!Thread.interrupted() && !loop()) ;
		} finally {
			stop();
		}
	}
	
	/**
	 * Runs at the end of execution.
	 * Keep short for graceful exits.
	 *
	 * @apiNote inside a finally block.
	 */
	public void stop() {}
	
	/**
	 * Called continuously when the Task is running.
	 *
	 * @return if the task is completed or not
	 */
	public boolean loop() {
		return true;
	}
}
