package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.ExternalSignalingWaiter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MINUTES;

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
 * THIS METHOD TO END THE INTERNAL THREAD GRACEFULLY. ELSE IT MAY NEVER
 * STOP WHEN ITS SUPPOSED TO.
 *
 * @see MoveTask
 */
class MoveTaskExecutor {
	private final BlockingQueue<MoveTask> queue = new LinkedBlockingQueue<>();
	private final MotorSet motors;
	//executors are too complicated for us to need em. Simple is faster and simpler.
	private final Thread theThread;
	
	private final AtomicBoolean done = new AtomicBoolean(true);
	private final ExternalSignalingWaiter isDone = new ExternalSignalingWaiter(done::get);
	private final AtomicBoolean skip = new AtomicBoolean(false);
	
	/**
	 * Construct via motors
	 *
	 * @param motors the motors
	 */
	MoveTaskExecutor(MotorSet motors) {
		this.motors = motors;
		theThread = new Thread(new TaskRunner());
		theThread.setName("Move Task Executor");
		theThread.setDaemon(true);
		theThread.start();
	}
	
	/**
	 * Adds a MoveTask to the queue
	 *
	 * @throws IllegalStateException if the thread has already stopped.
	 */
	void add(MoveTask task) {
		if (!theThread.isAlive()) theThread.start();
		queue.add(task);
	}
	
	/**
	 * Cancels all enqueued MoveTasks and stops
	 * any moveTask currently running
	 */
	void cancel() {
		queue.clear();
		skip.set(true);
	}
	
	/**
	 * @return true if no tasks are running, false otherwise
	 */
	boolean isDone() {
		return done.get();
	}
	
	/**
	 * waits until all current tasks are finished.
	 *
	 * @throws InterruptedException if the thread is interrupted while waiting
	 */
	void waitUntilDone() throws InterruptedException {
		isDone.waitUntilTrue();
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
	 * The Runnable that is sent to the thread.
	 * No fields for simplicity
	 */
	private class TaskRunner implements Runnable {
		
		@Override
		public void run() {
			try {
				while (!Thread.currentThread().isInterrupted()) {
					if (queue.isEmpty()) { //if we're done, set and notify
						done.set(true);
						isDone.update();
					}
					MoveTask curTask;
					try {
						curTask = queue.poll(2, MINUTES);
						if (curTask == null) break;
					} catch (InterruptedException e) {
						break;
					}
					//we have a new task, NOT DONE.
					done.set(false);
					//run the task
					curTask.start(motors);
					while (!Thread.currentThread().isInterrupted()) {
						if (skip.compareAndSet(true, false)) break; //skip this task.
						if (curTask.run(motors)) break; //task is actually done
					}
					skip.set(false);
					motors.stop();
				}
			} finally {
				done.set(true);
				isDone.update();
			}
		}
	}
}
