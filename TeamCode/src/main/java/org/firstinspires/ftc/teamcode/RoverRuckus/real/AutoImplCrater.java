package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Crater Auto", group = "Autonomous")
public class AutoImplCrater extends AbstractAuto {
	@Override
	protected void positionForDepot() throws InterruptedException {
		drive.turn(60, 10);
		drive.moveXY(-0.65, 0, 10);
		drive.moveXY(0.05, 0, 10);
		drive.moveXY(0, -1.4, 10);
	}
}
