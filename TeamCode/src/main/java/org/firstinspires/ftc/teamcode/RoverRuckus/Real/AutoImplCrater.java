package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto Next to Crater", group = "Autonomous")
public class AutoImplCrater extends AbstractAuto {
	@Override
	protected void positionForDepot() throws InterruptedException {
		robot.drive.turn(50, 10);
		robot.drive.moveXY(-0.7, 0, 10);
		robot.drive.moveXY(0.05, 0, 10);
		robot.drive.moveXY(0, -1.2, 10);
	}
}
