package org.firstinspires.ftc.teamcode.ruckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Crater | 2s delay", group = "Delay Autonomous")
public class AutoCraterDelay extends AutoCrater {
	@Override
	protected void putMarkerInDepot() {
		driveAndStuff.thenAdjust().sleep(2000);
		super.putMarkerInDepot();
	}
	
}
