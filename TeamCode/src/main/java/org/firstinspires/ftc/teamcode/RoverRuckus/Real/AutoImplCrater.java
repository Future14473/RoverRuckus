package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto Next to Crater", group = "Autonomous")
public class AutoImplCrater extends AbstractAuto {
	@Override
	protected void positionForDepot() throws InterruptedException {
		robot.drive.turn(45, 10);
		robot.drive.moveXY(-0.5, 0, 10);
		robot.drive.moveXY(0, -1.2, 10);
	}
}
