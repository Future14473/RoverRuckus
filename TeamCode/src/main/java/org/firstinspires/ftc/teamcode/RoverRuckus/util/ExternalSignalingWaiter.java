package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import java.util.function.BooleanSupplier;

public class ExternalSignalingWaiter {
	private final BooleanSupplier condition;
	private int waiters;
	private boolean lastCheck;
	
	public ExternalSignalingWaiter(BooleanSupplier condition) {
		this.condition = condition;
		lastCheck = condition.getAsBoolean();
	}
	
	public synchronized void waitUntilTrue() throws InterruptedException {
		lastCheck = condition.getAsBoolean();
		waiters++;
		while (!lastCheck) {
			this.wait();
		}
		waiters--;
	}
	
	public synchronized void update() {
		if (waiters == 0) return;
		lastCheck = condition.getAsBoolean();
		if (lastCheck) {
			this.notifyAll();
		}
	}
	
}
