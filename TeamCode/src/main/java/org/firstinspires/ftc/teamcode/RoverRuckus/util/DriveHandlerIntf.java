package org.firstinspires.ftc.teamcode.RoverRuckus.util;

public interface DriveHandlerIntf {
	
	void moveAt(double direction, double angle, double speed);
	
	void move(double direction, double angle, double speed) throws InterruptedException;
	
	void moveXY(double x, double y, double speed) throws InterruptedException;
	
	void turn(double degrees, double direction) throws InterruptedException;
	
	boolean hasTasks();
	
	void waitForDone() throws InterruptedException;
	
	void cancelTasks();
	
	void stop();
}
