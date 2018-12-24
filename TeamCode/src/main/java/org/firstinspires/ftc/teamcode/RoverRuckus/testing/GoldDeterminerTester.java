package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldDeterminer;

@TeleOp(name = "Gold Finder Tester", group = "Test")
public class GoldDeterminerTester extends OpMode {
	private GoldDeterminer goldDeterminer;
	
	@Override
	public void init() {
		goldDeterminer = new GoldDeterminer(hardwareMap);
	}
	
	@Override
	public void start() {
		goldDeterminer.start();
	}
	
	@Override
	public void loop() {
		if (!goldDeterminer.hasDetected()) return;
		telemetry.addData("Gold location is:", goldDeterminer.goldPosition());
		telemetry.update();
		if(gamepad1.a) goldDeterminer.start();
	}
}