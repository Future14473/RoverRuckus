package org.firstinspires.ftc.teamcode.ruckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@Autonomous(name = "Auto Next to Depot", group = "Autonomous")
public class AutoImplDepot extends AbstractAuto {
	@Override
	protected void putMarkerInDepot() throws InterruptedException {
		drive.turn(135, DEGREES, 10).move(8, 12, 10).go()
		     .then(this::openMarkerDoor)
		     .goMove(0, -36, 10, true)
		     .then(this::flickMarkerOut);
	}
	
	@Override
	protected void parkInCrater() {
		drive.goMove(1, 50, 10);
	}
}
