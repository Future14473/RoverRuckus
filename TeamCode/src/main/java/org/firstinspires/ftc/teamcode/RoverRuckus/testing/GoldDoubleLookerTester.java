package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldDoubleLooker;

@TeleOp(name = "Gold Double Look tester", group = "Test")
public class GoldDoubleLookerTester extends OpMode {
	private GoldDoubleLooker goldLooker = new GoldDoubleLooker();
	
	@Override
	public void init() {
		goldLooker.init(hardwareMap);
	}
	
	@Override
	public void start() {
		goldLooker.start();
		int look;
		do look = goldLooker.look(); while( look == -1);
		telemetry.addData("Gold is at", look);
		telemetry.update();
		while(!gamepad1.x);
		requestOpModeStop();
	}
	
	@Override
	public void loop() {
	}
	
	@Override
	public void stop() {
		goldLooker.stop();
	}
}
