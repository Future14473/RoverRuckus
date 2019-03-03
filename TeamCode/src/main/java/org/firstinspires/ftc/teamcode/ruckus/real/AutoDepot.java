package org.firstinspires.ftc.teamcode.ruckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.ruckus.real.auto.NormalAuto;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@Autonomous(name = "Depot | no delay", group = "Autonomous")
public class AutoDepot extends NormalAuto {
	@Override
	protected void putMarkerInDepot() {
		driveAndStuff.turn(135, DEGREES, 10).move(8, -12, 10).go()
		             .then(this::openMarkerDoor)
		             .thenMove(0, -32, 10, true)
		             .then(this::flickMarkerOut);
	}
	
	@Override
	protected void goToCrater() {
		driveAndStuff.thenMove(1, 50, 10, true);
	}
}
