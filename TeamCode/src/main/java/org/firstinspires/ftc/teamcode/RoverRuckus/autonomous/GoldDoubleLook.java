package org.firstinspires.ftc.teamcode.RoverRuckus.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Gold Double Look tester", group = "Test")
public class GoldDoubleLook extends LinearOpMode {
	private org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldDoubleLook goldLooker = new org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldDoubleLook();
	
	
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
