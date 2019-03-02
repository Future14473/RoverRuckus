package org.firstinspires.ftc.teamcode.lib.tasks;

import android.support.annotation.CallSuper;
import org.firstinspires.ftc.teamcode.lib.opmode.OpModeLifetimeRegistrar;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * A framework around the base TaskExecutor that provides extra functionality.
 */
public class TaskProgram implements OpModeLifetimeRegistrar.Stoppable {
	private final TaskExecutor taskExecutor;
	private final boolean      autoStart;
	private       boolean      started = false;
	//private       BlockingQueue<Future<?>> futures = new LinkedBlockingQueue<>();
	
	public TaskProgram(String name) {
		this(name, true);
	}
	
	public TaskProgram(boolean autoStart) {
		this(TaskProgram.class.getSimpleName(), autoStart);
	}
	
	public TaskProgram(String name, boolean autoStart) {
		this.autoStart = autoStart;
		taskExecutor = new TaskExecutor(name);
//		taskExecutor.addOnDoneTask(() -> {
//			futures.forEach(future -> future.cancel(false));
//			futures.clear();
//		});
		OpModeLifetimeRegistrar.register(this);
	}
	
	public TaskProgram() {
		this(true);
	}
	
	/** adds task, returns this. For call chains */
	public TaskProgram then(Task task) {
		add(task);
		return this;
	}
	
	/** adds a task to be run */
	public void add(Task task) {
		if (autoStart && !started) {
			started = true;
			start();
		}
		taskExecutor.add(task);
	}
	
	/** adds a task that calls a callable, and returns a {@link Future} to represent that task */
	public <V> Future<V> submit(Callable<V> callable) {
		FutureTask<V> futureTask = new FutureTask<>(callable);
		//futures.add(futureTask);
		add(futureTask);
		return futureTask;
	}
	
	/** adds a task, and returns a {@link Future} to represent that task. */
	public Future<?> submit(Task task) {
		FutureTask<?> futureTask = new FutureTask<>(task, null);
		//futures.add(futureTask);
		add(futureTask);
		return futureTask;
	}

//	/** cancels any running or queued task */
//	public void cancelTasks() {
//		taskExecutor.cancelTasks();
//	}
	
	/** Adds an on done task. {@link TaskExecutor#addOnDoneTasks(Task)} */
	public void addOnDoneTask(Task task) {
		taskExecutor.addOnDoneTasks(task);
	}
	
	/** returns true if no tasks are running and none in queue */
	public boolean isDone() {
		return taskExecutor.isDone();
	}
	
	/** blocks current thread until all tasks are run */
	public void waitUntilDone() throws InterruptedException {
		taskExecutor.waitUntilDone();
	}
	
	/** Stops task running thread */
	@CallSuper
	@Override
	public void stop() {
		taskExecutor.stop();
	}
	
	/** Starts task running thread */
	@CallSuper
	public void start() {
		taskExecutor.start();
	}
	
	/** Adds a task that sleeps some number of millis. */
	public TaskProgram sleep(long millis) {
		add((Task.WithInterrupt) () -> Thread.sleep(millis));
		return this;
	}
	
	/** Adds a task that stops this TaskProgram when it gets here */
	public void thenStop() {
		add(this::stop);
	}
	
	@Override
	public String toString() {
		return "Unspecific TaskProgram";
	}
}
