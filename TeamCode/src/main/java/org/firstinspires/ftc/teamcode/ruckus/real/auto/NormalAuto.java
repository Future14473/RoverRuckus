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
		parkInCrater();
		
		lookAndHook.waitUntilDone();
		driveAndStuff.waitUntilDone();
	}
	
	private void knockOffGold() throws InterruptedException {
		int look = getGoldLook();
		driveAndStuff.turn(-5, DEGREES, 10).move(-12 + 18 * look, 16, 10).go()
		             .thenMove(0, 6, 10, true)
		             .thenMove(0, -8, 0.8, true)
		             .thenMove(-18 - 18 * look, 0, 10);
	}
	
}
