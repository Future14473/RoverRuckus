package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto Crater with Helper", group = "Autonomous")
public class AutoImpCraterHelper extends AbstractAuto {
	@Override
	protected void positionForDepot() throws InterruptedException {
		robot.drive.turn(47, 10);
		robot.drive.moveXY(-0.4, 0, 10);
		robot.drive.moveXY(0, -1.3, 10);
	}
	
	@Override
	public void extra() throws InterruptedException {
		robot.drive.turn(40, 10);
		robot.drive.moveXY(0, -0.1, 10);
		knockOffGold();
		robot.drive.turn(-40, 10);
	}
}
