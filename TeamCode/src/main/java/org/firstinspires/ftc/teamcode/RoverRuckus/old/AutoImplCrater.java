package org.firstinspires.ftc.teamcode.RoverRuckus.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name = "Auto Next to Crater", group = "Autonomous")
@Disabled
public class AutoImplCrater extends AbstractAuto {
	@Override
	protected void positionForDepot() throws InterruptedException {
		robot.drive.turn(47, 10);
		robot.drive.moveXY(-0.7, 0, 10);
		robot.drive.moveXY(0.05, 0, 10);
		robot.drive.moveXY(0, -1.4, 10);
	}
}
