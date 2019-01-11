package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(name = "THE ACTUAL TELEOP", group = "teleop")
public class TeleOpReal extends OpMode {
	private Robot robot = new Robot();
	private boolean pastGamepad1y, pastGamepad2x, pastGamepad2leftStick0;
	private boolean reverseDrive = false;
	private static final int ARM_MIN = -5000, ARM_MAX = -10;
	@Override
	public void init() {
		robot.init(hardwareMap);
		robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	}
	
	
	@Override
	public void loop() {
		//GAMEPAD 1
		//REVERSE
		if (gamepad1.y && !pastGamepad1y) {
			reverseDrive = !reverseDrive;
		}
		pastGamepad1y = gamepad1.y;
		//MOVE BOT
		double speedMult = gamepad1.right_bumper ? 10 : (gamepad1.left_bumper ? 1.0 / 3 : 1);
		double angle = Math.atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y);
		if (reverseDrive) angle += Math.PI;
		double speed = Math.pow(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1.7) * speedMult;
		double turnRate = gamepad1.right_stick_x * speedMult * 2; //prioritize turning over moving.
		robot.drive.moveAt(angle, speed, turnRate);
		telemetry.addData("DIRECTION", reverseDrive ? "HOOK FRONT" : "ARM FRONT");
		telemetry.update();
		//TAPE
		if (gamepad1.a) {
			robot.tape.setPower(-1); //out
		} else if (gamepad1.b) {
			robot.tape.setPower(1); //in
		} else {
			robot.tape.setPower(0);
		}
		//HOOK
		if (gamepad2.dpad_down) {
			robot.hooke.setPower(-1); //hook up, robot down
		} else if (gamepad2.dpad_up) {
			robot.hooke.setPower(1); //hook down, robot up
		} else {
			robot.hooke.setPower(0);
		}
		
		//OPEN
		if (gamepad1.x) {
			robot.opener.setPosition(0.5);
		} else if (gamepad1.y) {
			robot.opener.setPosition(-0.5);
		} else {
			robot.opener.setPosition(0);
		}
		
		//GAMEPAD 2
		//ARM: with limits, and smartness
		if (gamepad2.x && !pastGamepad2x) { //just pressed
			robot.arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
		if (!gamepad2.x && pastGamepad2x) { //released
			robot.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		}
		
		boolean gamepad2leftStick0 = Math.abs(gamepad2.left_stick_y) < 1e-5;
		if (gamepad2leftStick0) {
			if (!pastGamepad2leftStick0) {
				//current position.
				robot.arm.setTargetPosition(Range.clip(robot.arm.getCurrentPosition(), ARM_MIN, ARM_MAX));
			}
		} else {
			robot.arm.setTargetPosition(gamepad2.left_stick_y < 0 ? ARM_MIN : ARM_MAX); //go to place with limits.
		}
		robot.arm.setPower(gamepad2.left_stick_y);
		pastGamepad2leftStick0 = gamepad2leftStick0;
		pastGamepad2x = gamepad2.x;
		//ROTATION
		robot.rotation.setPower(gamepad2.right_stick_y / 1.5);
		
		//COLLECTION
		if (gamepad2.left_bumper) {
			robot.collection.setPower(0.8); //in
		} else if (gamepad2.right_bumper) {
			robot.collection.setPower(-0.8); //out
		} else {
			robot.collection.setPower(0);
		}
	}
}
