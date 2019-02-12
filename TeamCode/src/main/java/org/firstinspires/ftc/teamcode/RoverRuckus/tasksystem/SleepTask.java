package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem;

/**
 * A task that just sleeps some amount of time
 */
public class SleepTask implements Task {
	private final long millis;
	
	public SleepTask(long millis) {this.millis = millis;}
	
	@Override
	public void run() {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {}
	}
	
}
