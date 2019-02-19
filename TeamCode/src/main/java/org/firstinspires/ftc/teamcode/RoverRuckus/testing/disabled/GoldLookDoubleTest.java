package org.firstinspires.ftc.teamcode.RoverRuckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.GoldLookDouble;

@TeleOp(group = "test")
@Disabled
public class GoldLookDoubleTest extends LinearOpMode {
	
	private GoldLookDouble goldLooker = new GoldLookDouble();
	
	@Override
	public void runOpMode() {
		goldLooker.init(hardwareMap);
		waitForStart();
		goldLooker.activate();
		int look;
		int cycles = 0;
		while (opModeIsActive()) {
			telemetry.addData("Cycles", cycles++);
			telemetry.update();
			look = goldLooker.detect();
			if (look == -1) continue;
			telemetry.addData("Gold is at", look);
			telemetry.update();
			cycles = 0;
		}
	}
}
