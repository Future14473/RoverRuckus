package org.firstinspires.ftc.teamcode.ruckus.real.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.ruckus.real.disabled.AutoCrater;

@Autonomous(name = "Crater | 2s delay", group = "Autonomous")
@Disabled
public class AutoCraterDelay extends AutoCrater {
	@Override
	protected void putMarkerInDepot() {
		driveAndStuff.thenAdjust().thenSleep(2000);
		super.putMarkerInDepot();
	}
	
}
