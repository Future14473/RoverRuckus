package org.firstinspires.ftc.teamcode.RoverRuckus.tasks;

import android.support.annotation.CallSuper;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OpModeLifetimeRegistrar;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * A framework around the base TaskExecutor that provides extra functionality.
 */
public class TaskProgram implements OpModeLifetimeRegistrar.Stoppable {
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
	
	public TaskProgram then(Task task) {
		add(task);
		return this;
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
	public void stop() {
		taskExecutor.stop();
	}
	
	@CallSuper
	public void start() {
		taskExecutor.start();
	}
	
	/**
	 * Sleeps some number of millis when it gets here.
	 */
	public final void sleep(long millis) {
		add((Task.WithInterrupt) () -> Thread.sleep(millis));
	}
	
	/**
	 * adds a task that terminates this TaskProgram when it gets here.
	 */
	public void thenStop() {
		add(this::stop);
	}
	
}
