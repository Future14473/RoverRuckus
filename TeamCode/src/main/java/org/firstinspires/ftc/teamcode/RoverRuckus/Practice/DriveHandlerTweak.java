package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.RoverRuckus.assets.HardwareTestBot;

public class DriveHandlerTweak extends OpMode {
	HardwareTestBot robot = new HardwareTestBot();
	private float angle, speed, distance; // = 0
	
	@Override
	public void init() {
		robot.init(hardwareMap);
		telemetry.addLine("Left stick up/down is distance, right/left is speed");
		telemetry.addLine("Right stick is direction");
		telemetry.addLine("left/Right bumpers to change Multiplier");
		telemetry.addLine("Press A to start");
		telemetry.update();
		robot.driveHandler.startMoveThread();
	}
	
	@Override
	public void loop() {
		boolean changed = false;
		if (!robot.driveHandler.noTasks()) {
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
				angle = (float) Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x);
			}
			if (gamepad1.left_bumper) {
				robot.driveHandler.MOVE_MULT -= 0.5;
				changed = true;
			}
			if (gamepad1.right_bumper) {
				robot.driveHandler.MOVE_MULT += 0.5;
				changed = true;
			}
			if (changed) {
				telemetry.addData("Distance (m): ", distance);
				telemetry.addData("speed (0-1):", speed);
				telemetry.addData("Direction (deg):", Math.toDegrees(angle));
				telemetry.addData("CURRENT EFFECTIVE MOVE_MULT:", robot.driveHandler.MOVE_MULT);
				telemetry.update();
			}
			if (gamepad1.a) {
				robot.driveHandler.moveTo(angle, speed, distance);
				telemetry.addLine("Moving....");
				telemetry.addLine("X to cancel");
				telemetry.update();
			}
		} else {
			if(gamepad1.x){
				robot.driveHandler.cancelTasks();
			}
			while (!robot.driveHandler.noTasks()) ;
			telemetry.addLine("Left stick up/down is distance, right/left is speed");
			telemetry.addLine("Right stick is direction");
			telemetry.addLine("left/Right bumpers to change Multiplier");
			telemetry.addLine("Press A to start");
			telemetry.update();
		}
	}
}
