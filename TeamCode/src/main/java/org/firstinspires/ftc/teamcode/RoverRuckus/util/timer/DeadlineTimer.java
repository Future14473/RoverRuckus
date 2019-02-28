package org.firstinspires.ftc.teamcode.RoverRuckus.util.timer;

public interface DeadlineTimer{
	long nanosToDeadline();
	
	void addToDeadline(long nanos);
	
	void resetDeadline();
	
	default boolean deadlineHit() {
		return nanosToDeadline() <= 0;
	}
	
	default double millisToDeadline() {
		return nanosToDeadline() / 1e6;
	}
	
	default double secondsToDeadline() {
		return nanosToDeadline() / 1e9;
	}
}
