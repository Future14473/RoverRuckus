package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Crater With Double Sampling", group = "Autonomous")
public class AutoImpCraterHelper extends AbstractAuto {
	@Override
	protected void positionForDepot() throws InterruptedException {
		drive.turn(50, 10);
		drive.moveXY(-0.7, 0, 10);
		drive.moveXY(0.05, 0, 10);
		drive.moveXY(0, -1.3, 10);
	}
	
	@Override
	public void afterDepot() throws InterruptedException {
		drive.turn(40, 10);
		drive.moveXY(0, -0.1, 10);
		knockOffGold();
		drive.turn(-40, 10);
	}
}
