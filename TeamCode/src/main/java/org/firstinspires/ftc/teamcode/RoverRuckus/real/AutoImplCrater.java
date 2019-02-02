package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto Next To Crater", group = "Autonomous")
public class AutoImplCrater extends AbstractAuto {
	@Override
	protected void positionForDepot() throws InterruptedException {
		drive.turn(50, 10);
		drive.moveXY(-0.65, 0, 10);
		drive.moveXY(0.05, 0, 10);
		drive.moveXY(0, -1.4, 10);
	}
	
	@Override
	protected void parkInCrater() {
		drive.moveXY(-0.15, 2, 10);
	}
}
