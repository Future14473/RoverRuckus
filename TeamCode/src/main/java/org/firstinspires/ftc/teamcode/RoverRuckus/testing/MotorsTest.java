package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(group = "test")
public class MotorsTest extends OpMode {
	private double pos;
	private DcMotor motor;
	private Servo servo;
	private CRServo crServo;
	private Robot robot;
	
	@Override
	public void init() {
		robot = new Robot(hardwareMap);
		//motor = robot.scoreArm;
		//motor.setDirection(DcMotorSimple.Direction.REVERSE);
		servo = hardwareMap.get(Servo.class, "CollectDoor");
		//crServo = hardwareMap.get(CRServo.class, "Collector");
	}
	
	@Override
	public void loop() {
//		if (gamepad1.a) {
//			motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//			motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//		}
//		motor.setPower(-gamepad1.right_stick_y);
//		telemetry.addData("Gamepad1 Right stick Y(negated):", -gamepad1.right_stick_y);
//		telemetry.addData("Motor pos:", motor.getCurrentPosition());
		
		if (gamepad1.dpad_up) {
			if (pos < 0.995) pos += 0.002;
		} else if (gamepad1.dpad_down) {
			if (pos > 0.005) pos -= 0.002;
		}
		servo.setPosition(pos);
		telemetry.addData("POS", pos);
		telemetry.addData("Servo pos:", servo.getPosition());

//		crServo.setPower(0.5 - gamepad1.left_stick_y / 2);
	}
}
