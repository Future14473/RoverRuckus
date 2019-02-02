package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto Crater with Double Sample", group = "Helper")
public class AutoImpCraterHelper extends AutoImplCrater {
	@Override
	public void afterDepot() throws InterruptedException {
		throw new IHaveNotDoneThisYetException("NO DOUBLE SAMPLE YET");
	}
	
	private class IHaveNotDoneThisYetException extends RuntimeException {
		public IHaveNotDoneThisYetException(String s) {
			super(s);
		}
	}
}
