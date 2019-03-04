package org.firstinspires.ftc.teamcode.ruckus.real.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Crater | 2s delay", group = "Autonomous")
public class AutoCraterDelay extends AutoCrater {
	@Override
	protected void putMarkerInDepot() {
		driveAndStuff.thenAdjust().thenSleep(2000);
		super.putMarkerInDepot();
	}
	
}
