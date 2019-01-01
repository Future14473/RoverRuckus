package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Test Auto Next to Depot", group = "Autonomous2")
public class AutoImplDepot extends AbstractAuto {
	@Override
	protected void position() throws InterruptedException {
		robot.drive.turn(-135, 10);
		robot.drive.moveXY(0.2, 0, 10);
	}
}
