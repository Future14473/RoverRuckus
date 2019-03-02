package org.firstinspires.ftc.teamcode.lib.timer;

public interface Timer {
	long getNanos();
	
	void reset();
	
	default double getMillis() {
		return getNanos() / 1e6;
	}
	
	default double getSeconds() {
		return getNanos() / 1e9;
	}
	
	default double getSecondsAndReset() {
		double o = getSeconds();
		reset();
		return o;
	}
	
}
