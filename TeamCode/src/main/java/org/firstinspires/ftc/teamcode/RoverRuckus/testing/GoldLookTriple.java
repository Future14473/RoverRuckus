package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Gold Finder Tester", group = "Test")
public class GoldLookTriple extends OpMode {
	private org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookTriple goldLookTriple;
	
	@Override
	public void init() {
		goldLookTriple = new org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookTriple(hardwareMap);
	}
	
	@Override
	public void start() {
		goldLookTriple.start();
	}
	
	@Override
	public void loop() {
		if (!goldLookTriple.hasDetected()) return;
		telemetry.addData("Gold location is:", goldLookTriple.goldPosition());
		telemetry.update();
		if (gamepad1.a) goldLookTriple.start();
	}
}
