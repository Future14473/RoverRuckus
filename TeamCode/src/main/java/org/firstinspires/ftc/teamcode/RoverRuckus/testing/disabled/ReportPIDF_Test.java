package org.firstinspires.ftc.teamcode.RoverRuckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(group = "test")
@Disabled
public class ReportPIDF_Test extends OpMode {
	
	private DcMotorEx motorEx;
	
	@Override
	public void init() {
		motorEx = hardwareMap.get(DcMotorEx.class, "Hook");
		
	}
	
	@Override
	public void loop() {
		telemetry.addData("Run Using Encoder", motorEx.getPIDFCoefficients(
				DcMotor.RunMode.RUN_USING_ENCODER));
		telemetry.addData("Run To Pos", motorEx.getPIDFCoefficients(
				DcMotor.RunMode.RUN_TO_POSITION));
	}
}
