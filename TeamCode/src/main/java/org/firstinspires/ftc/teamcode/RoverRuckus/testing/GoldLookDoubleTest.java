package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDouble;

@TeleOp(group = "test")
@Disabled
public class GoldLookDoubleTest extends LinearOpMode {
	private GoldLookDouble goldLooker = new GoldLookDouble();
	
	
	@Override
	public void runOpMode() {
		goldLooker.init(hardwareMap);
		waitForStart();
		goldLooker.start();
		int look;
		while (opModeIsActive()) {
			do look = goldLooker.look(); while (look == -1);
			telemetry.addData("Gold is at", look);
			telemetry.update();
		}
	}
}
