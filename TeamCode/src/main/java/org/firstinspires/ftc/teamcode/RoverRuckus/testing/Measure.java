package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OldRobot;

@TeleOp(name = "Measurement", group = "measure")
public class Measure extends OpMode {
	private OldRobot robot = new OldRobot();
	private DcMotor motor;
	private Servo servo;
	
	@Override
	public void init() {
		robot.init(hardwareMap);
		motor = robot.arm;
		servo = robot.opener;
	}
	
	@Override
	public void loop() {
		if (gamepad1.a) {
			motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
		if(gamepad1.x){
			servo.setPosition(0.5);
		} else {
			servo.setPosition(0);
		}
		motor.setPower(gamepad1.right_stick_x);
		telemetry.addData("Motor pos:", motor.getCurrentPosition());
		telemetry.addData("Servo pos:", servo.getPosition());
		telemetry.update();
	}
}
