package org.firstinspires.ftc.teamcode.ruckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.lib.opmode.SimpleCondition;
import org.firstinspires.ftc.teamcode.ruckus.real.auto.BaseAuto;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@Autonomous(name = "Crater, The Good One | no delay", group = "1Autonomous")
public class AutoCraterSampleLast extends BaseAuto {
	protected int             look                = -1;
	private   SimpleCondition startInitialCollect = new SimpleCondition();
	
	@Override
	protected void putMarkerInDepot() {
		driveAndStuff.turn(-45, DEGREES, 10).move(-10, -12, 10).go()
		             .then(this::openMarkerDoor)
		             .thenMove(0, -30, 10, true)
		             .then(this::flickMarkerOut);
	}
	
	protected void goToCraterAndSample(int look) {
		driveAndStuff.thenMove(5, 36, 10)
		             .move(5, 6, 10).turn(45, DEGREES, 10).go()
		             .thenMove(25 + 16 * look, -5, 10)
		             .thenMove(-5, 14, 1, true);
	}
	
	@Override
	protected void run() throws InterruptedException {
		unHook();
		goLikeKnockOffGold();
		putMarkerInDepot();
		goToCraterAndSample(look);
		prepareInitialCollect();
		initialCollect(look);
		
		lookAndHook.waitUntilDone();
		driveAndStuff.waitUntilDone();
	}
	
	protected void prepareInitialCollect() {
		driveAndStuff.thenMove(0, -9, 10, true)
		             .thenStopRobot()
		             .then(startInitialCollect::signal);
	}
	
	private void goLikeKnockOffGold() throws InterruptedException {
		getLook();
		driveAndStuff.thenMove(-5, 12, 10)
		             .thenMove(-25, 2, 10);
	}
	
	private void getLook() throws InterruptedException {
		if (look == -1) look = getGoldLook();
	}
	
	protected void initialCollect(int look) throws InterruptedException {
		startInitialCollect.awaitInterruptibility();
		driveAndStuff.thenTurn(25 * (look - 1), DEGREES, 10);
		extendArm();
		driveAndStuff.thenTurn(-10, DEGREES, 20);
		for (int i = 0; i < 2; i++) {
			driveAndStuff.thenTurn(20, DEGREES, 20)
			             .thenTurn(-20, DEGREES, 20);
		}
		try {
			robot.scooper.setPower(1);
			Thread.sleep(2500);
			robot.scooper.setPower(-0.25);
			Thread.sleep(300);
		} finally {
			robot.scooper.setPower(0);
		}
	}
}
