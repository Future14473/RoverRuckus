package org.firstinspires.ftc.teamcode.RoverRuckus.robottasks;

public interface RobotTaskExecutor {
	/**
	 * Adds a RobotTask to the queue
	 *
	 * @throws IllegalStateException if the thread has already stopped.
	 */
	void add(RobotTask robotTask);
	
	/**
	 * Cancels all enqueued MoveTasks and stops
	 * any moveTask currently running
	 */
	void cancel();
	
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
	 * Pauses this executor, if running
	 *
	 * @return if previously was running
	 */
	void pause();
	
	/**
	 * Resumes this RobotTaskExecutor, if was paused
	 */
	void resume();
	
	/**
	 * Returns if this RobotTaskExecutor is current paused
	 */
	boolean isPaused();
	
	/**
	 * Stops any currently running or queued tasks, and does necessary
	 * cleanup
	 */
	void stop();
	
}
