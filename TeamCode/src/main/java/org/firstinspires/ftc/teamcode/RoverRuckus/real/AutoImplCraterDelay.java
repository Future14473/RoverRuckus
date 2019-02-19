package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//TODO: CHANGE
@Autonomous(name = "Auto next to Crater 2s delay", group = "Delay Autonomous")
public class AutoImplCraterDelay extends AutoImplCrater {
	@Override
	protected void positionForDepot() throws InterruptedException {
		drive.sleep(2000);
		super.positionForDepot();
	}
	
}
