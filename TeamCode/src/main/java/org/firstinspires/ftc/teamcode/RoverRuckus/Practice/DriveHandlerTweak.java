package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

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
	}
	
	@Override
	public void stop() {
		robot.driveHandler.stopMoveThread();
		super.stop();
	}
	
	@Override
	public void loop() {
		boolean changed = false;
		if (!robot.driveHandler.hasTasks()) {
			if (Math.abs(gamepad1.left_stick_y) > 0.4) {
				distance += (Math.abs(gamepad1.left_stick_y) - 0.4) * Math.signum(gamepad1.left_stick_y);
				changed = true;
			}
			if (Math.abs(gamepad1.left_stick_x) > 0.4) {
				speed += (Math.abs(gamepad1.left_stick_x) - 0.4) * Math.signum(gamepad1.left_stick_x);
				changed = true;
			}
			if (Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y) > 0.5) {
				changed = true;
				angle = (float) Math.atan2(gamepad1.left_stick_x, gamepad1.left_stick_y); //0 is up, + right, - left, theoretically.
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
				robot.driveHandler.TURN_MULT -= 0.2;
				changed = true;
			}
			if (gamepad1.dpad_right) {
				robot.driveHandler.TURN_MULT += 0.2;
				changed = true;
			}
			if (changed) {
				telemetry.addData("Distance (m): ", distance);
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
			} else if (gamepad1.b) {
				robot.driveHandler.turn(angle, speed);
				telemetry.addLine("Moving....");
				telemetry.addLine("X to cancel");
				telemetry.update();
			}
			//*/
		} else {
			if (gamepad1.x) {
				robot.driveHandler.cancelTasks();
			}
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
