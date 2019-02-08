package org.firstinspires.ftc.teamcode.RoverRuckus.robottasks;

public class SleepTask implements RobotTask {
	private final long millis;
	
	public SleepTask(long millis) {this.millis = millis;}
	
	@Override
	public void start(IRobot IRobot) throws InterruptedException {
		Thread.sleep(millis);
	}
	
}
