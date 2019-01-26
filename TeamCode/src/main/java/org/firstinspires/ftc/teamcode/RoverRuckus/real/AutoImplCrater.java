package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name = "Crater Auto", group = "Autonomous")
@Disabled
public class AutoImplCrater extends AbstractAuto {
	@Override
	protected void positionForDepot() throws InterruptedException {
		drive.turn(47, 10);
		drive.moveXY(-0.7, 0, 10);
		drive.moveXY(0.05, 0, 10);
		drive.moveXY(0, -1.4, 10);
	}
}
