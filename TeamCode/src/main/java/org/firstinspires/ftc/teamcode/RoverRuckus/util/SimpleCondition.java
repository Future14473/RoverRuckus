package org.firstinspires.ftc.teamcode.RoverRuckus.util;

/**
 * A simple wait/notify system.
 */
public class SimpleCondition {
	private boolean condition = false;
	
	/**
	 * Waits until some other thread calls signal on this object.
	 * Returns if interrupted.
	 */
	public synchronized void await() {
		try {
			awaitInterruptibility();
		} catch (InterruptedException ignored) {}
	}
	
	/**
	 * Waits until some other thread calls signal on this object.
	 *
	 * @throws InterruptedException if interrupted while waiting.
	 */
	public synchronized void awaitInterruptibility() throws InterruptedException {
		while (condition) this.wait();
	}
	
	public synchronized void signal() {
		condition = true;
		this.notifyAll();
	}
	
}


