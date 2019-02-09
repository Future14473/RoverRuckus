package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.OpModeLifetimeRegistrar;

/**
 * An interface to represent a class that does the actual running of {@code Task}s
 * Tasks are put into a queue and loop one at a time. This class
 * contains several other methods to interact with the
 * queue and the running of the MoveTasks. Running of {@code Task}s
 * are done in a separate thread. <br>
 *
 * @see Task
 */
public interface TaskExecutor extends OpModeLifetimeRegistrar.Stoppable {
	/**
	 * Adds a Task to the queue
	 *
	 * @throws IllegalStateException if the thread has already stopped.
	 */
	void add(Task task);
	
	/**
	 * Cancels all enqueued MoveTasks and stops
	 * any moveTask currently running
	 */
	void cancelTasks();
	
	/**
	 * @return true if no tasks are running, false otherwise
	 */
	boolean isDone();
	
	/**
	 * waits until all current tasks are finished.
	 *
	 * @throws InterruptedException if the thread is interrupted while waiting
	 */
	void waitUntilDone() throws InterruptedException;
	
	/**
	 * Starts internal threads.
	 */
	void start();
	
	/**
	 * Stops any currently running or queued tasks, and does necessary
	 * cleanup
	 */
	@Override
	void stop();
	
}
