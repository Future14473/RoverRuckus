package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.assets.GoldFinder;
@TeleOp(name = "Gold Finder Tester", group = "Test")
public class GoldFinderTester extends OpMode {
	private GoldFinder goldFinder;
	@Override
	public void init() {
		goldFinder = new GoldFinder(hardwareMap);
	}
	
	@Override
	public void start() {
		goldFinder.start();
	}
	
	@Override
	public void loop() {
		if(!goldFinder.hasDetected())return;
		telemetry.addData("Gold location is:",goldFinder.goldPosition());
		telemetry.update();
	}
}
