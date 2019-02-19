package org.firstinspires.ftc.teamcode.RoverRuckus.tasks;

import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Reflections;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.OpModeLifetimeRegistrar;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MINUTES;

class TaskExecutor implements OpModeLifetimeRegistrar.Stoppable {
	private static final String        TAG     = TaskExecutor.class.getSimpleName();
	private final        String        name;
	private final        AtomicBoolean running = new AtomicBoolean(true);
	
	private final    Object              isDoneLock  = new Object();
	private final    Thread              theThread;
	private final    BlockingQueue<Task> queue       = new LinkedBlockingQueue<>();
	private          List<Task>          onDoneTasks = new LinkedList<>();
	private volatile Task                curTask;
	
	public TaskExecutor(String name) {
		this.name = name;
		theThread = new Thread(this::run);
		theThread.setName(String.format("%s: %s", TAG, name));
		theThread.setDaemon(true);
	}
	
	public TaskExecutor() {
		this(TAG);
	}
	
	/**
	 * Adds a Task to the queue
	 */
	public void add(Task task) {
		if (task == null) throw new NullPointerException();
		if (!running.get()) throw new IllegalStateException();
		queue.add(task);
	}

//	@Override
//	public void cancelTasks() {
//		queue.clear();
//		theThread.interrupt();
//	}
	
	/**
	 * @return true if no tasks are running, false otherwise
	 */
	public boolean isDone() {
		return !running.get() && curTask == null && queue.isEmpty();
	}
	
	/**
	 * waits until all current tasks are finished.
	 *
	 * @throws InterruptedException if the thread is interrupted while waiting
	 */
	public void waitUntilDone() throws InterruptedException {
		synchronized (isDoneLock) {
			while (!isDone()) isDoneLock.wait();
		}
	}
	
	/**
	 * Starts internal thread.
	 */
	public void start() {
		theThread.start();
	}
	
	/**
	 * Adds a task to a list of tasks to be run, when all normal tasks
	 * are done. (isDone returns true).
	 * Should ideally be fast/small tasks.
	 */
	public void addOnDoneTasks(Task task) {
		onDoneTasks.add(task);
	}
	
	/**
	 * Removes an on done task.
	 */
	public void removeOnDoneTask(Task task) {
		onDoneTasks.remove(task);
	}
	
	@Override
	public void stop() {
		if (!running.getAndSet(false)) return;
		RobotLog.vv(TAG, "%s: stopping", name);
		theThread.interrupt();
		cleanup();
	}
	
	private void cleanup() {
		running.set(false);
		synchronized (isDoneLock) {
			isDoneLock.notifyAll();
		}
		queue.clear();
		curTask = null;
	}
	
	/**
	 * Thread runs this.
	 */
	private void run() {
		try {
			doTasks();
		} finally {
			cleanup();
		}
	}
	
	private void doTasks() {
		while (running.get()) try {
			if (queue.isEmpty()) {
				RobotLog.vv(TAG, "%s: Done", name);
				synchronized (isDoneLock) {
					isDoneLock.notifyAll();
				}
				for (Task onDoneTask : onDoneTasks) {
					RobotLog.vv(TAG, "%s: running onDoneTask %s", name,
					            Reflections.getInformativeName(onDoneTask));
					onDoneTask.run();
				}
			}
			curTask = queue.poll(2, MINUTES);
			if (curTask == null) {
				running.set(false);
				return;
			}
			if (!Thread.interrupted()) {
				RobotLog.vv(TAG, "%s: running %s", name, Reflections.getInformativeName(curTask));
				curTask.run();
			}
		} catch (InterruptedException ignored) {}
	}
}