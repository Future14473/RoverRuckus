package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class MoveTaskExecutor {
	private final BlockingQueue<MoveTask> queue = new LinkedBlockingQueue<>();
	private final MotorSet motors;
	//executors are too complex for us to need em.
	private final Thread theThread;
	private final TaskRunner taskRunner;
	
	public MoveTaskExecutor(MotorSet motors) {
		this.motors = motors;
		taskRunner = new TaskRunner();
		theThread = new Thread(taskRunner);
		theThread.setName("Move Task Executor");
		theThread.setDaemon(true);
		theThread.start();
	}
	
	public void add(MoveTask task) {
		queue.add(task);
	}
	
	public void cancel() {
		queue.clear();
		taskRunner.stopTask();
	}
	
	public boolean isDone() {
		return taskRunner.isDone();
	}
	
	public void pauseUntilDone() throws InterruptedException {
		while (taskRunner.isDone()) {
			synchronized (taskRunner) {
				taskRunner.wait();
			}
		}
	}
	
	public void stop() {
		queue.clear();
		theThread.interrupt();
	}
	
	private class TaskRunner implements Runnable {
		private boolean done = true;
		private boolean skip = false;
		private MoveTask curTask = null;
		
		boolean isDone() {
			return done;
		}
		
		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				if (queue.isEmpty()) { //if we're done, notify people.
					done = true;
					synchronized (this) {
						this.notifyAll();
					}
				}
				try {
					curTask = queue.poll(5, TimeUnit.MINUTES);
					if (curTask == null) break;
				} catch (InterruptedException e) {
					break;
				}
				//we have a new task
				done = false;
				//fencepost
				curTask.start(motors);
				while (!Thread.currentThread().isInterrupted()) {
					if (skip) {
						skip = false;
						break;
					}
					if (curTask.run(motors)) break;
				}
				motors.stop();
				skip = false;
			}
			motors.stop();
		}
		
		public void stopTask() {
			skip = true;
		}
	}
	
}
