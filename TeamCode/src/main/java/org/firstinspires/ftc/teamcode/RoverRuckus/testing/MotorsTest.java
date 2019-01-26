package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(group = "test")
public class MotorsTest extends OpMode {
	private DcMotor motor;
	private Servo servo;
	private CRServo crServo;
	
	@Override
	public void init() {
		motor = hardwareMap.get(DcMotor.class, "Collect");
		//servo = hardwareMap.get(Servo.class, "Door");
		//crServo = hardwareMap.get(CRServo.class, "Collector");
	}
	
	@Override
	public void loop() {
		if (gamepad1.a) {
			motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
		motor.setPower(-gamepad1.right_stick_y);
		telemetry.addData("Motor pos:", motor.getCurrentPosition());
//		if (gamepad1.x) {
//			servo.setPosition(0.5);
//		} else {
//			servo.setPosition(0);
//		}
//		telemetry.addData("Servo pos:", servo.getPosition());
//		crServo.setPower(0.5 - gamepad1.left_stick_y / 2);
	}
}
