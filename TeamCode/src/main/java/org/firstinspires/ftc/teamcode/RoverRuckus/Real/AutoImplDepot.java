package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Real Auto Next to Depot", group = "Autonomous")
public class AutoImplDepot extends AbstractAuto {
	@Override
	protected void position() throws InterruptedException {
		robot.drive.turn(-135, 10);
		robot.drive.moveXY(0.35, 0, 10);
		robot.drive.moveXY(0, -1.4, 10);
	}
}
