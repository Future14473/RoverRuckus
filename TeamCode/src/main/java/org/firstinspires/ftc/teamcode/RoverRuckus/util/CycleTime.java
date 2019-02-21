package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * A simple time tracking deal that resets time everyTime time is polled.
 */
public class CycleTime extends ElapsedTime {
	
	public double getSecondsAndReset() {
		double o = seconds();
		reset();
		return o;
	}
}
