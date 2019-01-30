package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import android.support.annotation.NonNull;

public class CancelableSingleExecutor {
	private final Runner runner = new Runner();
	private boolean running = true;
	private Thread thread;
	
	public void stop() {
		running = false;
		if (thread != null && thread.isAlive()) thread.interrupt();
	}
	
	@Override
	protected void finalize() throws Throwable {
		stop();
	}
	
	public void execute(@NonNull Runnable command) {
		cancel();
		if (thread == null) thread = new Thread(runner);
		synchronized (runner) {
			runner.newRunnable = true;
			runner.runnable = command;
			runner.notifyAll();
		}
	}
	
	public void cancel() {
		if (thread == null) return;
		synchronized (runner) {
			runner.newRunnable = false;
			thread.interrupt();
		}
	}
	
	private class Runner implements Runnable {
		public boolean newRunnable = false;
		public Runnable runnable = null;
		
		@Override
		public void run() {
			while (running) {
				try {
					Runnable curRunnable;
					synchronized (this) {
						while (!newRunnable) {
							this.wait();
						}
						newRunnable = false;
						curRunnable = runnable;
					}
					curRunnable.run();
				} catch (InterruptedException ignored) {
				}
			}
		}
	}
}
