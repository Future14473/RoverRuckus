package org.firstinspires.ftc.teamcode.RoverRuckus.tasks;

/**
 * Marker interface for our task system.<br>
 * If interrupted while running, exit return as soon as possible
 */
@FunctionalInterface
public interface Task extends Runnable {
	/**
	 * A task that can throw interruptedException
	 * When interrupted, returns
	 */
	@FunctionalInterface
	interface WithInterrupt extends Task {
		void runWithInterrupt() throws InterruptedException;
		
		@Override
		default void run() {
			try {
				runWithInterrupt();
			} catch (InterruptedException ignored) { }
		}
		
	}
}
