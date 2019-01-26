package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Actually handles the running of {@link MoveTask}s, given a
 * {@link MotorSet} to run them on.<br>
 * Tasks are put into a queue and run one at a time. This class
 * also contains several other methods to interact with the
 * queue and the running of the MoveTasks. Running of {@link MoveTask}s
 * are done in a separate thread. <br>
 * Although Object#finalize() is overriden as a backup and the thread
 * will stop itself after 2 minutes of inactivity: <br>
 * IT IS THE RESPONSIBILITY OF THE CLIENT TO CALL {@link #stop()} ON
 * THIS METHOD TO END THE INTERNAL THREAD GRACEFULLY.
 *
 * @see MoveTask
 */
class MoveTaskExecutor {
	private final BlockingQueue<MoveTask> queue = new LinkedBlockingQueue<>();
	private final MotorSet motors;
	//executors are too complex for us to need em.
	private final Thread theThread;
	private final TaskRunner taskRunner;
	
	/**
	 * Construct via motors
	 *
	 * @param motors the motors
	 */
	MoveTaskExecutor(MotorSet motors) {
		this.motors = motors;
		taskRunner = new TaskRunner();
		theThread = new Thread(taskRunner);
		theThread.setName("Move Task Executor");
		theThread.setDaemon(true);
		theThread.start();
	}
	
	/**
	 * Adds a MoveTask to the queue
	 */
	void add(MoveTask task) {
		try {
			if (!theThread.isAlive()) theThread.start();
		} catch (IllegalStateException e) { return;}
		queue.add(task);
	}
	
	/**
	 * Cancels all enqueued MoveTasks and stops
	 * any moveTask currently running
	 */
	void cancel() {
		queue.clear();
		taskRunner.stopTask();
	}
	
	/**
	 * @return true if no tasks are running, false if running
	 */
	boolean isDone() {
		return taskRunner.isDone();
	}
	
	/**
	 * waits (blocks the current thread) until all tasks are finished.
	 *
	 * @throws InterruptedException if the thread is interrupted while waiting
	 */
	void waitUntilDone() throws InterruptedException {
		while (!taskRunner.isDone()) {
			synchronized (taskRunner) {
				taskRunner.wait();
			}
		}
	}
	
	/**
	 * Stops the internal thread and any tasks.
	 */
	void stop() {
		theThread.interrupt();
	}
	
	@Override
	protected void finalize() {
		stop();
	}
	
	/**
	 * The Runnable for the thread to run MoveTasks
	 */
	private class TaskRunner implements Runnable {
		
		private boolean done = true;
		
		private boolean skip = false;
		private MoveTask curTask = null;
		
		boolean isDone() {
			return done;
		}
		
		@Override
		public void run() {
			while (!Thread.interrupted()) try {
				if (queue.isEmpty()) { //if we're done, notify people.
					done = true;
					synchronized (this) {
						this.notifyAll();
					}
				}
				try {
					curTask = queue.poll(2, TimeUnit.MINUTES);
					if (curTask == null) break;
				} catch (InterruptedException e) {
					break;
				}
				//we have a new task
				done = false;
				//run the task
				curTask.start(motors);
				while (!Thread.currentThread().isInterrupted()) {
					if (skip) {
						skip = false;
						break;
					}
					if (curTask.run(motors)) break;
				}
				motors.stop();
				skip = false;
			} finally {
				motors.stop();
				synchronized (this) { //tell waiters to stop waiting: we are done
					this.notifyAll();
				}
			}
		}
		
		void stopTask() {
			skip = true;
		}
	}
}
