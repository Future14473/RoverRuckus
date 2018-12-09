package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "DriveTweak", group = "Test")
public class DriveHandlerTweak extends OpMode {
	HardwareTestBot robot = new HardwareTestBot();
	private float angle, speed, distance; // = 0
	
	@Override
	public void init() {
		robot.init(hardwareMap);
		telemetry.addLine("Left stick up/down is distance, right/left is speed");
		telemetry.addLine("Right stick is direction");
		telemetry.addLine("Left/right bumpers to change MOVE_MULT");
		telemetry.addLine("Left/right dpad to change TURN_MULT");
		telemetry.addLine("Press A to start");
		telemetry.update();
		robot.driveHandler.startMoveThread();
		robot.driveHandler.setTelemetry(telemetry);
	}
	
	@Override
	public void stop() {
		robot.driveHandler.stopMoveThread();
		super.stop();
	}
	boolean wait = false;
	@Override
	public void loop() {
		if(wait){
			if(!robot.driveHandler.hasTasks()) wait = false;
			 return;
		}
		boolean changed = false;
		if (!robot.driveHandler.hasTasks()) {
			if (Math.abs(gamepad1.left_stick_y) > 0.4) {
				distance += -(Math.abs(gamepad1.left_stick_y) - 0.4) * Math.signum(gamepad1.left_stick_y) / 200;
				changed = true;
			}
			if (Math.abs(gamepad1.left_stick_x) > 0.4) {
				speed += (Math.abs(gamepad1.left_stick_x) - 0.4) * Math.signum(gamepad1.left_stick_x) / 200;
				changed = true;
			}
			if (Math.hypot(gamepad1.right_stick_x, gamepad1.right_stick_y) > 0.5) {
				changed = true;
				angle = (float) Math.atan2(gamepad1.right_stick_x, gamepad1.right_stick_y); //0 is up, + right, - left, theoretically.
			}
			if (gamepad1.left_bumper) {
				robot.driveHandler.MOVE_MULT -= 0.5;
				changed = true;
			}
			if (gamepad1.right_bumper) {
				robot.driveHandler.MOVE_MULT += 0.5;
				changed = true;
			}
			if (gamepad1.dpad_left) {
				robot.driveHandler.TURN_MULT -= 0.02;
				changed = true;
			}
			if (gamepad1.dpad_right) {
				robot.driveHandler.TURN_MULT += 0.02;
				changed = true;
			}
			if (changed) {
				telemetry.addData("Distance, arbitrary units: ", distance);
				telemetry.addData("Speed (0-1):", speed);
				telemetry.addData("Direction (deg):", Math.toDegrees(angle));
				telemetry.addData("CURRENT EFFECTIVE MOVE_MULT:", robot.driveHandler.MOVE_MULT);
				telemetry.addData("CURRENT EFFECTIVE TURN_MULT:", robot.driveHandler.TURN_MULT);
				telemetry.update();
			}
			//*
			if (gamepad1.a) {
				robot.driveHandler.move(angle, speed, distance);
				telemetry.addLine("Moving....");
				telemetry.addLine("X to cancel");
				telemetry.update();
				wait = true;
			} else if (gamepad1.b) {
				robot.driveHandler.turn(angle, speed);
				telemetry.addLine("Moving....");
				telemetry.addLine("X to cancel");
				telemetry.update();
				wait = true;
			}
			//*/
		} else {
			while (robot.driveHandler.hasTasks()) ; //wait to finish;
			telemetry.addLine("Left stick up/down is distance, right/left is speed");
			telemetry.addLine("Right stick is direction");
			telemetry.addLine("Left/right bumpers to change MOVE_MULT");
			telemetry.addLine("Left/right dpad to change TURN_MULT");
			telemetry.addLine("Press A to start");
			telemetry.update();
		}
	}
}
