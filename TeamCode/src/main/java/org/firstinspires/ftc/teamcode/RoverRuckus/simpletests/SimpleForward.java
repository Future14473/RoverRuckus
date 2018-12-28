package org.firstinspires.ftc.teamcode.RoverRuckus.simpletests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(name = "Forward Test", group = "Test")
public class SimpleForward extends OpMode {
	private Robot robot = new Robot();
	
	@Override
	public void start() {
		robot.init(hardwareMap);
		robot.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.rightFront.setTargetPosition(3000);
		robot.leftFront.setTargetPosition(3000);
		robot.rightBack.setTargetPosition(3000);
		robot.leftBack.setTargetPosition(3000);
		robot.rightFront.setPower(0.7);
		robot.leftFront.setPower(0.7);
		robot.rightBack.setPower(0.7);
		robot.leftBack.setPower(0.7);
	}
	
	@Override
	public void init() {
	
	}
	
	@Override
	public void loop() {
	
	}
}
