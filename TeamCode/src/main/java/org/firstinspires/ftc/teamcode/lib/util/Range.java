package org.firstinspires.ftc.teamcode.lib.util;

public class Range {
	private Range() {
	}
	
	public static double limit(double v, double min, double max) {
		return v < min ? min : v > max ? max : v;
	}
	
	public static double ramp(double current, double target, double ramp) {
		return limit(target, current - ramp, current + ramp);
	}
}
