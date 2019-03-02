package org.firstinspires.ftc.teamcode.lib.tasks;

/**
 * Adapts a task to use start, loop, and close methods.
 * These are executed by  in the following manner:
 * {@link #start} is called once when the task is executed.
 * Then, {@link #loop()} is called repeatedly until it the method returns true or the
 * the running thread is interrupted.
 * {@link #stop()} is always called at the end of the task, through normal execution
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
	public void run() {
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
	public void stop() {
	}
	
	/**
	 * Called continuously when the Task is running.
	 *
	 * @return if the task is completed or not
	 */
	public boolean loop() {
		return true;
	}
}
