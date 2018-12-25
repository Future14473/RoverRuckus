package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.HardwareTestBot;

@TeleOp(name = "Forward Test", group = "Test")
public class SimpleForward extends OpMode {
	private HardwareTestBot bot = new HardwareTestBot();
	
	@Override
	public void start() {
		bot.init(hardwareMap);
		bot.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		bot.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		bot.rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		bot.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		bot.rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		bot.leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		bot.rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		bot.leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		bot.rightFront.setTargetPosition(3000);
		bot.leftFront.setTargetPosition(3000);
		bot.rightBack.setTargetPosition(3000);
		bot.leftBack.setTargetPosition(3000);
		bot.rightFront.setPower(0.7);
		bot.leftFront.setPower(0.7);
		bot.rightBack.setPower(0.7);
		bot.leftBack.setPower(0.7);
	}
	
	@Override
	public void init() {
	
	}
	
	@Override
	public void loop() {
	
	}
}
