package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(name = "Reset Encoders", group = "real")
public class ResetEncoders extends OpMode {
	
	@Override
	public void init() {
		telemetry.addLine("ENCODERS RESET");
		telemetry.update();
		Robot robot = new Robot(hardwareMap);
		robot.scoreArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.collectArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.hook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		try {
			Thread.sleep(100);
		} catch (InterruptedException ignored) {
		}
		requestOpModeStop();
	}
	
	@Override
	public void loop() {
	
	}
}
