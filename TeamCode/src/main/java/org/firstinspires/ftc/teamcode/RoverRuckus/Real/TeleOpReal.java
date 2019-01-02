package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(name = "THE ACTUAL TELEOP", group = "teleop")
public class TeleOpReal extends OpMode {
	private Robot robot = new Robot();
	
	@Override
	public void init() {
		robot.init(hardwareMap);
	}
	
	private boolean pastGamepad1X = false;
	
	@Override
	public void loop() {
		//GAMEPAD 1
		//MOVE BOT
		double speedMult = gamepad1.right_bumper ? 10 : (gamepad1.left_bumper ? 0.4 : 1);
		double angle = Math.atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y);
		double speed = Math.pow(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1.7) * speedMult;
		double turnRate = gamepad1.right_stick_x * 2; //prioritize turning over moving.
		robot.drive.moveAt(angle, speed, turnRate);
		//TAPE
		if (gamepad1.a) {
			robot.tape.setPower(-1);
		} else if (gamepad1.b) {
			robot.tape.setPower(1);
		} else {
			robot.tape.setPower(0);
		}
		//HOOK
		if (gamepad1.x) {
			robot.hooke.setPower(1); //hook up, robot down
		} else if (gamepad1.y) {
			robot.hooke.setPower(-1); //hook down, robot up
		} else {
			robot.hooke.setPower(0);
		}
		
		//GAMEPAD 2
		//ARM
		robot.arm.setPower(gamepad2.left_stick_y);
		//ROTATION, WITH LIMITS.
		if (gamepad2.x || !(gamepad2.right_stick_y > 0 && robot.rotation.getCurrentPosition() < -6680) //
				&& !(gamepad2.right_stick_y < 0 && robot.rotation.getCurrentPosition() > 20)) {
			robot.rotation.setPower(gamepad2.right_stick_y / 2);
		} else {
			robot.rotation.setPower(0);
		}
		//reseting limits.
		if (pastGamepad1X && !gamepad1.x) {
			robot.rotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			robot.rotation.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
		pastGamepad1X = gamepad1.x;
		//COLLECTION
		if (gamepad2.left_bumper) {
			robot.collection.setPower(0.5); //in
		} else if (gamepad2.right_bumper) {
			robot.collection.setPower(-0.5); //out
		} else {
			robot.collection.setPower(0);
		}
		
	}
}
