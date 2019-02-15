package org.firstinspires.ftc.teamcode.RoverRuckus.tasks;

import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OpModeLifetimeRegistrar;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * An interface to represent a class that does the actual running of {@code
 * Task}s
 * Tasks are put into a queue and run one at a time. This class
 * contains several other methods to interact with the
 * queue and the running of the MoveTasks. Running of {@code Task}s
 * are done in a separate thread. <br>
 *
 * @see Task
 */
public class TaskExecutor implements OpModeLifetimeRegistrar.Stoppable {
	//executors are too complicated for us to need em. Simple is faster and
	// simpler.
	private static final String TAG = TaskExecutor.class.getSimpleName();
	
	private final BlockingQueue<Task> queue    = new LinkedBlockingQueue<>();
	private final Thread              theThread;
	private final AtomicBoolean       running  = new AtomicBoolean(false);
	private final Object              doneLock = new Object();
	private final String              name;
	private       boolean             done     = true;
	
	public TaskExecutor(String name) {
		this.name = name;
		theThread = new Thread(this::run);
		theThread.setName(String.format("%s: %s", TAG, name));
		theThread.setDaemon(true);
	}
	
	public TaskExecutor() {
		this.name = TAG;
		theThread = new Thread(this::run);
		theThread.setName(TAG);
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
	public void stop() {
		RobotLog.vv(TAG, "%s: stopping", name);
		if (!running.getAndSet(false)) return;
		theThread.interrupt();
		synchronized (doneLock) {
			done = true;
			doneLock.notifyAll();
		}
	}
	
	@Override
	protected void finalize() {
		stop();
	}
	
	/**
	 * Thread runs this.
	 */
	private void run() {
		try {
			doTasks();
		} finally {
			synchronized (doneLock) {
				done = true;
				doneLock.notifyAll();
			}
		}
	}
	
	private void doTasks() {
		while (running.get()) try {
			Task curTask;
			synchronized (doneLock) { //we can't say done while polling queue.
				if (queue.isEmpty()) { //if we're done, set and notify
					RobotLog.vv(TAG, "%s: notifying done", name);
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
				RobotLog.vv(TAG, "%s: running %s", name, curTask);
				curTask.run();
			}
		} catch (InterruptedException ignored) {}
	}
}
	

