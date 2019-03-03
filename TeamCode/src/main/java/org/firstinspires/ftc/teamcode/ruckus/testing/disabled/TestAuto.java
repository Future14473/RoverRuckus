package org.firstinspires.ftc.teamcode.ruckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.ruckus.real.AutoCraterSampleLast;

@TeleOp
@Disabled
public class TestAuto extends AutoCraterSampleLast {
	
	@Override
	protected void run() throws InterruptedException {
		prepareInitialCollect();
		initialCollect(look);
	}
	
	@Override
	protected void putMarkerInDepot() {
	
	}
	
	@Override
	protected void goToCraterAndSample(int look) {
	
	}
}
