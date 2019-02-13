package org.firstinspires.ftc.teamcode.RoverRuckus.util;

/**
 * A simple wait/notify system.
 */
public class SimpleCondition {
	private boolean condition = false;
	
	/**
	 * No interrupt exception variant
	 */
	public synchronized void awaitTrue() {
		try {
			awaitTrue(0);
		} catch (InterruptedException ignored) {}
	}
	
	/**
	 * yes interrupt variant
	 */
	public synchronized void awaitTrue(int dummy) throws InterruptedException {
		while (condition) this.wait();
	}
	
	public synchronized void setTrue() {
		condition = true;
		this.notifyAll();
	}
	
	public synchronized void setFalse() {
		condition = false;
	}
}


