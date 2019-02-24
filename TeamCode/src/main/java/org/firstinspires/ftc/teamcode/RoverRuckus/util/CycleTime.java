package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * An extension of ElapsedTime that adds a single method,
 * getSecondsAndReset, which is self explanatory
 */
public class CycleTime extends ElapsedTime {
	
	public double getSecondsAndReset() {
		double o = seconds();
		reset();
		return o;
	}
}
