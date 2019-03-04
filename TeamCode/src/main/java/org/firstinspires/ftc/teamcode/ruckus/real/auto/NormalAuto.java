package org.firstinspires.ftc.teamcode.ruckus.real.auto;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

public abstract class NormalAuto extends BaseAuto {
	protected abstract void goToCrater();
	
	@Override
	public void run() throws InterruptedException {
		unHook();
		knockOffGold();
		putMarkerInDepot();
		goToCrater();
		prepareInitialCollect();
		initialCollect();
		lookAndHook.waitUntilDone();
		driveAndStuff.waitUntilDone();
	}
	
	private void initialCollect() throws InterruptedException {
		startInitialCollect.awaitInterruptibility();
		extendArm();
		try {
			robot.scooper.setPower(1);
			Thread.sleep(3000);
		} finally {
			robot.scooper.setPower(0);
		}
	}
	
	private void knockOffGold() throws InterruptedException {
		int look = getGoldLook();
		driveAndStuff.turn(-5, DEGREES, 10).move(-10 + 16 * look, 16, 10).go()
		             .thenMove(0, 6, 10, true)
		             .thenMove(0, -8, 0.8, true)
		             .thenMove(-20 - 16 * look, 0, 10);
	}
	
	@Override
	protected void prepareInitialCollect() {
		driveAndStuff.thenAdjust()
		             .then(startInitialCollect::signal);
	}
}
