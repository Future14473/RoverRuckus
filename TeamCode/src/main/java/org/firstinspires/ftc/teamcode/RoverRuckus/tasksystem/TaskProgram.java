package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem;

import android.support.annotation.CallSuper;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OpModeLifetimeRegistrar;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * A framework around the base TaskExecutor that provides extra functionality.
 */
public class TaskProgram implements OpModeLifetimeRegistrar.Closeable {
	private final TaskExecutor taskExecutor;
	private final boolean autoStart;
	private boolean started = false;
	
	public TaskProgram(boolean autoStart) {
		this.autoStart = autoStart;
		taskExecutor = new TaskExecutor();
		OpModeLifetimeRegistrar.register(this);
	}
	
	public TaskProgram() {
		this(true);
	}
	
	public void add(Task task) {
		if (autoStart && !started) {
			started = true;
			start();
		}
		taskExecutor.add(task);
	}
	
	public <V> Future<V> call(Callable<V> callable) {
		FutureTask<V> task = new FutureTask<>(callable);
		add(task);
		return task;
	}
	
	public void cancelTasks() {
		taskExecutor.cancelTasks();
	}
	
	public boolean isDone() {
		return taskExecutor.isDone();
	}
	
	public void waitUntilDone() throws InterruptedException {
		taskExecutor.waitUntilDone();
	}
	
	@CallSuper
	@Override
	public void close() {
		taskExecutor.close();
	}
	
	@CallSuper
	public void start() {
		taskExecutor.start();
	}
	
	/**
	 * Waits until all tasks are finished, then sleeps a specified number of
	 * milliseconds
	 */
	public final void sleep(int millis) {
		add(new SleepTask(millis));
	}
	
	public void thenStop() {
		add(this::close);
	}
}
