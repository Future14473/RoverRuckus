package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.INCH;

@Autonomous(name = "Auto Next To Crater", group = "Autonomous")
public class AutoImplCrater extends AbstractAuto {
	@Override
	protected void putMarkerInDepot() throws InterruptedException {
		drive.turn(-45, DEGREES, 10).move(-8, -12, INCH, 10).go()
		     .then(this::openMarkerDoor)
		     .goMove(0, -30, 10, true)
		     .then(this::flickMarkerOut);
	}
	
	@Override
	protected void parkInCrater() {
		drive.goMove(-1, 55, 10);
	}
}
