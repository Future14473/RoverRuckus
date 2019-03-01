package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//TODO: CHANGE
@Autonomous(name = "Auto next to Depot 2s delay", group = "Delay Autonomous")
public class AutoImplDepotDelay extends AutoImplDepot {
	@Override
	protected void putMarkerInDepot() throws InterruptedException {
		drive.adjust().sleep(2000);
		super.putMarkerInDepot();
	}
	
}
