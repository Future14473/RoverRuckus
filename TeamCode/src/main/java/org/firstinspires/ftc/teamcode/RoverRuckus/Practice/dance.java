package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "DANCE!!!", group = "Test")
public class dance extends OpMode {
	private HardwareTestBot robot = new HardwareTestBot();
	
	@Override
	public void init() {
		robot.init(hardwareMap);
	}
	
	@Override
	public void stop() {
		robot.drive.cancelTasks();
		super.stop();
	}
	
	@Override
	public void start() {
		super.start();
		robot.drive.move(0, 1, 1.4f);
		robot.drive.move(-90f, 1, 0.5f);
		robot.drive.turn(45, 1);
		robot.drive.move(135, 1, 1.4f);
		robot.drive.move(45, 1, 0.4f);
		robot.drive.turn(315, 1);
	}
	
	@Override
	public void loop() {
	
	}
}
