package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookTriple;

@TeleOp(name = "Gold Look Triple Test", group = "Test")
public class GoldLookTripleTest extends OpMode {
	private GoldLookTriple goldLookTriple;
	
	@Override
	public void init() {
		goldLookTriple = new GoldLookTriple(hardwareMap);
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
