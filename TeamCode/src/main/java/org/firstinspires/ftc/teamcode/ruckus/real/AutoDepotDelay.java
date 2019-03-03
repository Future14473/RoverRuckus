package org.firstinspires.ftc.teamcode.ruckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//TODO: CHANGE
@Autonomous(name = "Depot | 2s delay", group = "Delay Autonomous")
public class AutoDepotDelay extends AutoDepot {
	@Override
	protected void putMarkerInDepot() {
		driveAndStuff.thenAdjust().sleep(2000);
		super.putMarkerInDepot();
	}
	
}
