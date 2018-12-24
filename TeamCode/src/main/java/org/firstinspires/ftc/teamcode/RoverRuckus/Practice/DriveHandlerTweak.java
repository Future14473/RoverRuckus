package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.HardwareTestBot;

@TeleOp(name = "DriveTweak", group = "Test")
public class DriveHandlerTweak extends OpMode {
	HardwareTestBot robot = new HardwareTestBot();
	boolean wait = false;
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
		robot.drive.startMoveThread();
	}
	
	@Override
	public void stop() {
		robot.drive.cancelTasks();
		robot.drive.stopMoveThread();
		super.stop();
	}
	public void showNums(){
		telemetry.addData("Distance, arbitrary units: ", distance);
		telemetry.addData("Speed (0-1):", speed);
		telemetry.addData("Direction (deg):", Math.toDegrees(angle));
		telemetry.addData("MOVE_MULT:", robot.drive.MOVE_MULT);
		telemetry.addData("TURN_MULT:", robot.drive.TURN_MULT);
		telemetry.addLine("Left stick up/down is distance, right/left is speed");
		telemetry.addLine("Right stick is direction");
		telemetry.addLine("Left/right bumpers to change MOVE_MULT");
		telemetry.addLine("Left/right dpad to change TURN_MULT");
		telemetry.addLine("Press A to start");
	}
	@Override
	public void loop() {
		if (wait) {
			if(gamepad1.x){
				robot.drive.cancelTasks();
			}
			if (!robot.drive.hasTasks()) {
				wait = false;
				showNums();
				telemetry.update();
			}
			return;
		}
		boolean changed = false;
		if (!robot.drive.hasTasks()) {
			if (Math.abs(gamepad1.left_stick_y) > 0.4) {
				distance += -(Math.abs(gamepad1.left_stick_y) - 0.4) * Math.signum(gamepad1.left_stick_y) / 200;
				changed = true;
			}
			if (Math.abs(gamepad1.left_stick_x) > 0.4) {
				speed += (Math.abs(gamepad1.left_stick_x) - 0.4) * Math.signum(gamepad1.left_stick_x) / 200;
				changed = true;
			}
			if (Math.hypot(gamepad1.right_stick_x, gamepad1.right_stick_y) > 0.5) {
				angle = (float) Math.atan2(gamepad1.right_stick_x, -gamepad1.right_stick_y);
				changed = true;
			}
			if (gamepad1.left_bumper) {
				robot.drive.MOVE_MULT -= 0.5;
				changed = true;
			}
			if (gamepad1.right_bumper) {
				robot.drive.MOVE_MULT += 0.5;
				changed = true;
			}
			if (gamepad1.dpad_left) {
				robot.drive.TURN_MULT -= 0.02;
				changed = true;
			}
			if (gamepad1.dpad_right) {
				robot.drive.TURN_MULT += 0.02;
				changed = true;
			}
			if (changed) {
				showNums();
				telemetry.update();
			}
			//*
			if (gamepad1.a) {
				robot.drive.move(angle, speed, distance);
				wait = true;
			} else if (gamepad1.b) {
				robot.drive.turn(angle, speed);
				wait = true;
			}
			//*/
		}
	}
}
