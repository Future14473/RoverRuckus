package org.firstinspires.ftc.teamcode.ruckus.real.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.ruckus.real.auto.NormalAuto;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@Autonomous(name = "Crater", group = "Autonomous")
public class AutoCrater extends NormalAuto {
	@Override
	protected void putMarkerInDepot() {
		driveAndStuff.turn(-45, DEGREES, 10).move(-10, -12, 10).go()
		             .then(this::openMarkerDoor)
		             .thenMove(0, -30, 10, true)
		             .then(this::flickMarkerOut);
	}
	
	@Override
	protected void goToCrater() {
		driveAndStuff.thenMove(-1, 52, 10, true);
	}
}
