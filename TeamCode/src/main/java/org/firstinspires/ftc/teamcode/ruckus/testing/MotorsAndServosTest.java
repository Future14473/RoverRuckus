package org.firstinspires.ftc.teamcode.ruckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.lib.robot.CurRobot;

@SuppressWarnings("unused")
@TeleOp(group = "test")
public class MotorsAndServosTest extends OpMode {
	private double  pos;
	private DcMotor motor;
	private Servo   servo;
	private CRServo crServo;
	
	@Override
	public void init() {
		CurRobot robot = new CurRobot(hardwareMap);
		motor = robot.collectArm;
		//motor.setDirection(DcMotorSimple.Direction.REVERSE);
		//servo = robot.scoreDump;
		//crServo = hardwareMap.get(CRServo.class, "Collector");
	}
	
	@Override
	public void loop() {
		//*motor
		motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		if (gamepad1.a) {
			motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		}
		motor.setPower(-gamepad1.right_stick_y);
		telemetry.addData("Gamepad1 Right stick Y(negated):", -gamepad1.right_stick_y);
		telemetry.addData("Motor pos:", motor.getCurrentPosition());
		
		telemetry.update();
		telemetry.clear();
		//*/
		/*
		if    (gamepad1.dpad_up) {
			if (pos < 0.998) pos += 0.002;
		} else if (gamepad1.dpad_down) {
			if (pos > 0.002) pos -= 0.002;
		}
		servo.setPosition(pos);
		telemetry.addData("Set pos", pos);
		//*/
		/*
		crServo.setPower(0.5 - gamepad1.left_stick_y / 2);
		//*/
	}
}
