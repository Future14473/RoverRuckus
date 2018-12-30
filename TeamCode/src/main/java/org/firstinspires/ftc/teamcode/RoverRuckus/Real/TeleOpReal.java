package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(name = "THE ACTUAL TELEOP", group = "teleop")
public class TeleOpReal extends OpMode {
	private Robot robot = new Robot();
	
	@Override
	public void init() {
		robot.init(hardwareMap);
	}
	
	@Override
	public void loop() {
		double angle = Math.atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y);
		double speed = Math.pow(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1.7) * 1.4f;
		double turnRate = gamepad1.right_stick_x * 2; //prioritize turning over moving.
		//telemetry.addData("Stick angle:",angle);
		robot.drive.moveAt(angle, speed, turnRate);
		
		if (gamepad1.left_bumper) {
			robot.tape.setPower(0);
		} else if (gamepad1.right_bumper) {
			robot.tape.setPower(1);
		} else {
			robot.tape.setPower(0.5);
		}
		
		if (gamepad1.x) {
			robot.hooke.setPower(1); //hook up, robot down
		} else if (gamepad1.y) {
			robot.hooke.setPower(-1); //hook down, robot up
		} else {
			robot.hooke.setPower(0);
		}
		
		robot.arm.setPower(gamepad2.left_stick_y);
		
		robot.rotation.setPower(gamepad2.right_stick_y);
		
		if (gamepad2.left_bumper) {
			robot.collection.setPower(1); //in
		} else if (gamepad2.right_bumper) {
			robot.collection.setPower(-1); //out
		} else {
			robot.collection.setPower(0);
		}
		
	}
}
