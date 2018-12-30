package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(name = "TeleOp Real", group = "Real")
public class TeleOpReal extends OpMode {
	private Robot robot = new Robot();
	//boolean a = false;
	
	@Override
	public void init() {
		robot.init(hardwareMap);
		//robot.drive.setModeEncoder();
	}
	
	@Override
	public void loop() {
		//replaced with drive Handler.
		float angle = (float) Math.atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y); //Up is 0, positive is
		// clockwise
		float speed = (float) Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y) * 1.4f; //allow going a bit
		// faster, if possible.
		float turnRate = gamepad1.right_stick_x * 2; //prioritize turning over moving.
		//telemetry.addData("Stick angle:",angle);
		robot.drive.moveAt(angle, speed, turnRate);

		/*
		if (gamepad1.right_bumper) {
			a = true;
		}


		while (a) {
			robot.Hooke.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			robot.Hooke.setTargetPosition(35500);
			robot.Hooke.setPower(1);
			if (robot.Hooke.getCurrentPosition() > 35500 || gamepad1.x) {
				robot.Hooke.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
				robot.Hooke.setPower(0);
				a = false;
				break;
			}
			telemetry.addData("current position", robot.Hooke.getCurrentPosition());
			telemetry.update();
		}
*/
		if (gamepad2.dpad_up) {
			robot.arm.setPower(1);
		} else if (gamepad2.dpad_down) {
			robot.arm.setPower(-1);
		} else {
			robot.arm.setPower(0);
		}
		
		if (gamepad2.x) {
			robot.hooke.setPower(1);
		} else if (gamepad2.y) {
			robot.hooke.setPower(-1);
		} else {
			robot.hooke.setPower(0);
		}
		
		if (gamepad2.dpad_up) {
			robot.arm.setPower(1);
		} else if (gamepad2.dpad_down) {
			robot.arm.setPower(-1);
		} else {
			robot.arm.setPower(0);
		}
		
		if (gamepad2.left_bumper) {
			robot.rotation.setPower(1);
		} else if (gamepad2.right_bumper) {
			robot.rotation.setPower(-1);
		} else {
			robot.rotation.setPower(0);
		}
		
		if (gamepad2.a) {
			robot.collection.setPower(1);
		} else if (gamepad2.b) {
			robot.collection.setPower(-1);
		} else {
			robot.collection.setPower(0);
		}
		
	}
	
	@Override
	public void stop() {
		robot.drive.stop();
	}
}
