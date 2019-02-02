package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDouble;

@TeleOp(group = "test")
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
