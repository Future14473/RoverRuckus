package org.firstinspires.ftc.teamcode.RoverRuckus.tasks;

/**
 * Marker interface for our task system, simply is an extension
 * of {@link Runnable}<br>
 * To ensure graceful exits, the convention is if interrupted while running, exit return as soon
 * as possible.
 */
@FunctionalInterface
public interface Task extends Runnable {
	/**
	 * This interface is for use for lambdas that throw InterruptedException (i.e {@code Thread
	 * .sleep()}. This also extends Task, and will simply return if InterruptedException is caught.
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
