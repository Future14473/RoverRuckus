//package org.firstinspires.ftc.teamcode.RoverRuckus.old;
//
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//
//@TeleOp(name = "THE ACTUAL TELEOP", group = "teleop")
//@Disabled
//public class TeleOpReal extends OpMode {
//	private static final int      ARM_MIN      = 10;
//	private              OldRobot robot        = new OldRobot();
//	private              boolean  pastGamepad1y;
//	private              boolean  reverseDrive = false;
//	private              boolean  pastGamepad2x;
//
//	@Override
//	public void init() {
//		robot.init(hardwareMap);
//	}
//
//	@Override
//	public void loop() {
//		//GAMEPAD 1
//		//REVERSE
//		if (gamepad1.y && !pastGamepad1y) {
//			reverseDrive = !reverseDrive;
//		}
//		pastGamepad1y = gamepad1.y;
//		//MOVE BOT
//		double speedMult = gamepad1.right_bumper ? 10 :
//		                   (gamepad1.left_bumper ? 1.0 / 3 : 1);
//		double angle =
//				Math.atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y);
//		if (reverseDrive) angle += Math.PI;
//		double speed = Math.pow(
//				Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y),
//				1.7) *
//		               speedMult;
//		double turnRate = gamepad1.right_stick_x * speedMult *
//		                  2; //prioritize turning over moving.
//		robot.drive.moveAt(angle, turnRate, speed);
//		telemetry.addData("DIRECTION",
//		                  reverseDrive ? "HOOK FRONT" : "ARM FRONT");
//		//TAPE
//		/*if (gamepad1.a) {
//			robot.tape.setPower(-1); //out
//		} else if (gamepad1.b) {
//			robot.tape.setPower(1); //in
//		} else {
//			robot.tape.setPower(0);
//		}*/
//
//		//GAMEPAD 2
//		//HOOK
//		if (gamepad2.dpad_down) {
//			robot.hooke.setPower(-1); //hook up, robot down
//		} else if (gamepad2.dpad_up) {
//			robot.hooke.setPower(1); //hook down, robot up
//		} else {
//			robot.hooke.setPower(0);
//		}
//
//		if (!gamepad2.x && pastGamepad2x) {
//			robot.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//			robot.arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//		}
//		pastGamepad2x = gamepad2.x;
//		//ARM: with limits, and smartness
//		if (!gamepad2.x && robot.arm.getCurrentPosition() < ARM_MIN &&
//		    gamepad2.left_stick_y > 0) robot.arm.setPower(0);
//		else robot.arm.setPower(gamepad2.left_stick_y / 2);
//
//		//ROTATION
//		robot.rotation.setPower(gamepad2.right_stick_y / 2);
//
//		//COLLECTION
//		if (gamepad2.left_bumper) {
//			robot.collection.setPower(0.8); //in
//		} else if (gamepad2.right_bumper) {
//			robot.collection.setPower(-0.8); //out
//		} else {
//			robot.collection.setPower(0);
//		}
//
//		telemetry.update();
//	}
//}
