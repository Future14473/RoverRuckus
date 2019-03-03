package org.firstinspires.ftc.teamcode.ruckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.ruckus.real.auto.SampleLastAuto;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@Autonomous(name = "Crater, Sample Last", group = "Autonomous")
public class AutoCraterSampleLast extends SampleLastAuto {
	@Override
	protected void putMarkerInDepot() {
		driveAndStuff.turn(-45, DEGREES, 10).move(-8, -12, 10).go()
		             .then(this::openMarkerDoor)
		             .thenMove(0, -30, 10, true)
		             .then(this::flickMarkerOut);
	}
	
	@Override
	protected void goToCraterAndSample(int look) {
		driveAndStuff.thenMove(4, 36, 10)
		             .move(4, 6, 10).turn(45, DEGREES, 10).go()
		             .thenMove(18 + 18 * look, -2, 10, true)
		             .thenMove(0, 11, 0.8, true);
	}
}
