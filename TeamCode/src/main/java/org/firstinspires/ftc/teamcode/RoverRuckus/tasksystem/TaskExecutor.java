package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.OpModeLifetimeRegistrar;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * An interface to represent a class that does the actual running of {@code
 * Task}s
 * Tasks are put into a queue and loop one at a time. This class
 * contains several other methods to interact with the
 * queue and the running of the MoveTasks. Running of {@code Task}s
 * are done in a separate thread. <br>
 *
 * @see Task
 */
public class TaskExecutor implements OpModeLifetimeRegistrar.Closeable {
	private final BlockingQueue<Task> queue = new LinkedBlockingQueue<>();
	//executors are too complicated for us to need em. Simple is faster and
	// simpler.
	
	private final Thread theThread;
	private final AtomicBoolean running = new AtomicBoolean(false);
	private final Object doneLock = new Object();
	private boolean done = true;
	
	/**
	 * Construct via IRobot
	 */
	public TaskExecutor() {
		theThread = new Thread(this::run);
		theThread.setName("Move Task Executor");
		theThread.setDaemon(true);
	}
	
	/**
	 * Adds a Task to the queue
	 */
	public void add(Task task) {
		if (task == null) throw new NullPointerException();
		queue.add(task);
	}
	
	/**
	 * Cancels all enqueued MoveTasks and stops
	 * any moveTask currently running
	 */
	public void cancelTasks() {
		queue.clear();
		theThread.interrupt();
	}
	
	/**
	 * @return true if no tasks are running, false otherwise
	 */
	public boolean isDone() {
		synchronized (doneLock) {
			return done;
		}
	}
	
	/**
	 * waits until all current tasks are finished.
	 *
	 * @throws InterruptedException if the thread is interrupted while waiting
	 */
	public void waitUntilDone() throws InterruptedException {
		synchronized (doneLock) {
			while (!done) doneLock.wait();
		}
	}
	
	/**
	 * Starts internal thread.
	 */
	public void start() {
		running.set(true);
		theThread.start();
	}
	
	@Override
	public void close() {
		if (running.compareAndSet(false, false)) return;
		theThread.interrupt();
		synchronized (doneLock) {
			done = true;
			doneLock.notifyAll();
		}
	}
	
	@Override
	protected void finalize() {
		close();
	}
	
	private void run() {
		while (running.get()) try {
			Task curTask;
			synchronized (doneLock) { //we can't say done while polling queue.
				if (queue.isEmpty()) { //if we're done, set and notify
					done = true;
					doneLock.notifyAll();
				}
				curTask = queue.poll(2, MINUTES);
				if (curTask == null) {
					close();
					break;
				}
				done = false;
			}
			if (!Thread.interrupted()) {
				curTask.run();
			}
		} catch (InterruptedException ignored) {}
		synchronized (doneLock) {
			done = true;
			doneLock.notifyAll();
		}
	}
}
	

