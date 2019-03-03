package org.firstinspires.ftc.teamcode.ruckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Depot | 2s delay", group = "Autonomous")
public class AutoDepotDelay extends AutoDepot {
	@Override
	protected void putMarkerInDepot() {
		driveAndStuff.thenAdjust().thenSleep(2000);
		super.putMarkerInDepot();
	}
	
}
