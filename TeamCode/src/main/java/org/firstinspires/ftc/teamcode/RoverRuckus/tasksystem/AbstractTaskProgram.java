package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem;

import android.support.annotation.CallSuper;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OpModeLifetimeRegistrar;

/**
 * A framework around the base TaskExecutor that provides extra functionality.
 */
public class AbstractTaskProgram implements OpModeLifetimeRegistrar.Stoppable {
	
	private final TaskExecutor taskExecutor;
	
	protected AbstractTaskProgram() {
		taskExecutor = new TaskExecutorImpl();
		OpModeLifetimeRegistrar.register(this);
	}
	
	protected void add(Task task) {
		taskExecutor.add(task);
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
	public void start() {
		taskExecutor.start();
	}
	
	@CallSuper
	@Override
	public void stop() {
		taskExecutor.stop();
	}
	
	/**
	 * Waits until all tasks are finished, then sleeps a specified number of milliseconds
	 */
	public final void sleep(int millis) {
		add(new SleepTask(millis));
	}
	
}
