package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * A task that runs and waits for a callable to run and finish when executed
 */
public abstract class CallableTask<V> extends TaskAdapter implements Callable<V> {
	private final ExecutorService executorService;
	private Future<V> future;
	
	public CallableTask(ExecutorService executorService) {
		this.executorService = executorService;
	}
	
	@Override
	public final void start() throws InterruptedException {
		future = executorService.submit(this);
		try {
			future.get();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() {
		future.cancel(true);
	}
	
	public final Future<V> getFuture() {
		return future;
	}
}
