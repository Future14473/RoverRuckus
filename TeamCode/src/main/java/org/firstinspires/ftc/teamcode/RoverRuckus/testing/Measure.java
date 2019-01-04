package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(name = "Measurement", group = "measure")
public class Measure extends OpMode {
	private Robot robot = new Robot();
	
	@Override
	public void init() {
		robot.init(hardwareMap);
	}
	
	@Override
	public void loop() {
		if (gamepad1.a) {
			robot.hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			robot.hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
		if (gamepad1.x) {
			robot.rotation.setPower(0.3);
		} else if (gamepad1.y) {
			robot.rotation.setPower(-0.3);
		} else robot.rotation.setPower(0);
		telemetry.addData("Rot pos:", robot.rotation.getCurrentPosition());
		telemetry.update();
	}
}
