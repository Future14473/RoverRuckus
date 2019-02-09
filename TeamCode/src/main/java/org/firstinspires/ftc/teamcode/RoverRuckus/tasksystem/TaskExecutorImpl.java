package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * A simple one-at-a-time implementation of TaskExecutor
 * Implementation of TaskExecutor
 */
public class TaskExecutorImpl implements TaskExecutor {
	private final BlockingQueue<Task> queue = new LinkedBlockingQueue<>();
	//executors are too complicated for us to need em. Simple is faster and simpler.
	private final Thread theThread;
	private final AtomicBoolean running = new AtomicBoolean(false);
	private final Object doneLock = new Object();
	private boolean done = true;
	
	/**
	 * Construct via IRobot
	 */
	public TaskExecutorImpl() {
		theThread = new Thread(this::run);
		theThread.setName("Move Task Executor");
		theThread.setDaemon(true);
		theThread.start();
	}
	
	@Override
	public void add(Task task) {
		queue.add(task);
	}
	
	@Override
	public void cancelTasks() {
		queue.clear();
		theThread.interrupt();
	}
	
	@Override
	public boolean isDone() {
		synchronized (doneLock) {
			return done;
		}
	}
	
	@Override
	public void waitUntilDone() throws InterruptedException {
		synchronized (doneLock) {
			while (!done) doneLock.wait();
		}
	}
	
	@Override
	public void start() {
		running.set(true);
		theThread.start();
	}
	
	@Override
	public void stop() {
		running.set(false);
		theThread.interrupt();
		synchronized (doneLock) {
			done = true;
			doneLock.notifyAll();
		}
	}
	
	@Override
	protected void finalize() {
		if (running.get()) stop();
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
					stop();
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
	

