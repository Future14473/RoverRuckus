package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Example extends OpMode {
	
	DcMotor motor1;
	
	@Override
	public void init() {
		motor1 = hardwareMap.get(DcMotor.class, "motor1");
	}
	
	@Override
	public void loop() {
		motor1.setPower(gamepad1.left_stick_y);
	}
}
