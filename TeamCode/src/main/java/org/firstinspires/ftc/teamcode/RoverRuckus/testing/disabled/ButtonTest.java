package org.firstinspires.ftc.teamcode.RoverRuckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(group = "test")
@Disabled
public class ButtonTest extends OpMode {
	private TouchSensor button;
	private DcMotor     motor;
	
	@Override
	public void init() {
		button = hardwareMap.get(TouchSensor.class, "Button");
		motor = hardwareMap.get(DcMotor.class, "Scooper");
	}
	
	@Override
	public void loop() {
		motor.setPower(button.isPressed() ? 0.2 : 0);
	}
}
