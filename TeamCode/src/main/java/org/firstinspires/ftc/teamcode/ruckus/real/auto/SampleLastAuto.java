package org.firstinspires.ftc.teamcode.ruckus.real.auto;

public abstract class SampleLastAuto extends BaseAuto {
	private int look = -1;
	
	protected abstract void goToCraterAndSample(int look);
	
	@Override
	protected void run() throws InterruptedException {
		unHook();
		goLikeKnockOffGold();
		putMarkerInDepot();
		goToCraterAndSample(look);
		parkInCrater();
		
		lookAndHook.waitUntilDone();
		driveAndStuff.waitUntilDone();
	}
	
	private void goLikeKnockOffGold() throws InterruptedException {
		getLook();
		driveAndStuff.thenMove(-5, 12, 10)
		             .thenMove(-25, 2, 10);
	}
	
	private void getLook() throws InterruptedException {
		if (look == -1) look = getGoldLook();
	}
}
