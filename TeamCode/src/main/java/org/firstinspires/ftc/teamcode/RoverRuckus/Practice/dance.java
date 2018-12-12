package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RoverRuckus.assets.HardwareTestBot;

@TeleOp(name = "DANCE!!!", group = "Test")
public class dance extends OpMode {
	HardwareTestBot robot = new HardwareTestBot();
	boolean wait = false;
	private float angle, speed, distance; // = 0
	
	@Override
	public void init() {
		robot.init(hardwareMap);
		robot.drive.startMoveThread();
		robot.drive.setStuff(telemetry, gamepad1);
	}
	
	@Override
	public void stop() {
		robot.drive.cancelTasks();
		robot.drive.stopMoveThread();
		super.stop();
	}
	
	@Override
	public void start() {
		super.start();
		robot.drive.move(0,1,1.4f);
		robot.drive.move(-90f,1, 0.5f);
		robot.drive.turn(45,1);
		robot.drive.move(135,1,1.4f);
		robot.drive.move(45,1,0.4f);
		robot.drive.turn(315,1);
	}
	
	@Override
	public void loop() {
	
	}
}
