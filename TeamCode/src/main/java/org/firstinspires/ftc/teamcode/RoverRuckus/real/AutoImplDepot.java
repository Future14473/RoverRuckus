package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto next to Depot", group = "Autonomous")
public class AutoImplDepot extends AbstractAuto {
	@Override
	protected void positionForDepot() throws InterruptedException {
		drive.turn(-120, 10);
		drive.moveXY(0.45, 0, 10);
		drive.moveXY(-0.05, 0, 10);
		drive.moveXY(0, -1.3, 10);
	}
}
