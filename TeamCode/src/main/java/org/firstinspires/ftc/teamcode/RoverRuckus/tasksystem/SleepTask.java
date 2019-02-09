package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem;

public class SleepTask extends TaskAdapter {
	private final long millis;
	
	public SleepTask(long millis) {this.millis = millis;}
	
	@Override
	public void start() throws InterruptedException {
		Thread.sleep(millis);
	}
	
}
