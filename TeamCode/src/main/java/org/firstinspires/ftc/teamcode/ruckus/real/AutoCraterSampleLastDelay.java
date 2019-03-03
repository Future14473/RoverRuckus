package org.firstinspires.ftc.teamcode.ruckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Crater, The Good one | 2s delay", group = "1Autonomous")
public class AutoCraterSampleLastDelay extends AutoCraterSampleLast {
	@Override
	protected void putMarkerInDepot() {
		driveAndStuff.thenAdjust().thenSleep(2000);
		super.putMarkerInDepot();
	}
}
