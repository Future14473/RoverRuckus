package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto next to Depot", group = "Autonomous")
public class AutoImplDepot extends AbstractAuto2 {
	@Override
	protected void positionForDepot() throws InterruptedException {
		drive.rotate(-125, 10)
		     .moveXY(0.45, 0, 10)
		     .moveXY(-0.07, 0, 10)
		     .moveXY(0, -1.3, 10);
	}
	
	@Override
	protected void parkInCrater() {
		drive.moveXY(0.1, 2, 10);
	}
}
