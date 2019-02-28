package org.firstinspires.ftc.teamcode.RoverRuckus.util.timer;

public interface Timer {
	long getNanos();
	
	default double getMillis() {
		return getNanos() / 1e6;
	}
	
	void reset();
	
	default double getSeconds() {
		return getNanos() / 1e9;
	}
	
	default double getSecondsAndReset() {
		double o = getSeconds();
		reset();
		return o;
	}
	
}
