package org.firstinspires.ftc.teamcode.ruckus.real;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.lib.robot.CurRobot;

@TeleOp(name = "Reset Encoders", group = "reset")
public class ResetEncoders extends OpMode {
	
	@Override
	public void init() {
		CurRobot robot = new CurRobot(hardwareMap);
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
