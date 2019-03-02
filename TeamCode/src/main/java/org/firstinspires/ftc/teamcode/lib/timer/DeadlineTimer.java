package org.firstinspires.ftc.teamcode.lib.timer;

public interface DeadlineTimer {
	long nanosToDeadline();
	
	void addToDeadline(long nanos);
	
	void resetDeadline();
	
	default boolean deadlineHit() {
		return nanosToDeadline() <= 0;
	}
	
	default void addToDeadlineMillis(double millis) {
		addToDeadline((long) (millis * 1_000_000));
	}
	
	default double millisToDeadline() {
		return nanosToDeadline() / 1e6;
	}
	
	default double secondsToDeadline() {
		return nanosToDeadline() / 1e9;
	}
	
	default void addToDeadlineSeconds(double seconds) {
		addToDeadline((long) (seconds * 1e9));
	}
}
