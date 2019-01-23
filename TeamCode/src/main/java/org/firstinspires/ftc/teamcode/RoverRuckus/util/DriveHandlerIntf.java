package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public interface DriveHandlerIntf {
	
	double MOVE_MULT = 4450; //change to tweak "move x meters" precisely. Degrees wheel turn per unit.
	double TURN_MULT = 1205; //change to tweak "rotate x deg" precisely.   Degrees wheel turn per
	
	void addLinearOpMode(LinearOpMode opMode);
	
	void cancelTasks();
	
	boolean isDone();
	
	void move(double direction, double angle, double speed) throws InterruptedException;
	
	void moveAt(double direction, double angle, double speed);
	
	void moveXY(double x, double y, double speed) throws InterruptedException;
	
	void stop();
	
	void turn(double degrees, double direction) throws InterruptedException;
	
	void waitForDone() throws InterruptedException;
}
