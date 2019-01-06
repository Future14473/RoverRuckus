package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(name = "Measurement", group = "measure")
public class Measure extends OpMode {
	private Robot robot = new Robot();
	private DcMotor motor;
	
	@Override
	public void init() {
		robot.init(hardwareMap);
		motor = robot.arm;
	}
	
	@Override
	public void loop() {
		if (gamepad1.a) {
			motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
		motor.setPower(gamepad1.right_stick_x);
		telemetry.addData("Pos:", motor.getCurrentPosition());
		telemetry.update();
	}
}
