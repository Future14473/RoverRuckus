package org.firstinspires.ftc.teamcode.ruckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//TODO: CHANGE
@Autonomous(name = "Auto next to Crater 2s delay", group = "Delay Autonomous")
public class AutoImplCraterDelay extends AutoImplCrater {
	@Override
	protected void putMarkerInDepot() throws InterruptedException {
		drive.adjust().sleep(2000);
		super.putMarkerInDepot();
	}
	
}
