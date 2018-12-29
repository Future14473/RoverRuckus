package org.firstinspires.ftc.teamcode.RoverRuckus.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Gold Finder Tester", group = "Test")
public class GoldTripleLook extends OpMode {
	private org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldTripleLook goldTripleLook;
	
	@Override
	public void init() {
		goldTripleLook = new org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldTripleLook(hardwareMap);
	}
	
	@Override
	public void start() {
		goldTripleLook.start();
	}
	
	@Override
	public void loop() {
		if (!goldTripleLook.hasDetected()) return;
		telemetry.addData("Gold location is:", goldTripleLook.goldPosition());
		telemetry.update();
		if (gamepad1.a) goldTripleLook.start();
	}
}
