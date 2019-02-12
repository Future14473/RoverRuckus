package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem;

/**
 * A task that waits for a condition (to be notified elsewhere) to become
 * true.
 */
public class GateTask implements Task {
	private boolean canPass = false;
	
	@Override
	public synchronized void run() {
		while (!canPass) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	public synchronized void openGate() {
		this.canPass = true;
		this.notifyAll();
	}
	
	public synchronized void close() {
		this.canPass = false;
	}
}


