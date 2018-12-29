package org.firstinspires.ftc.teamcode.RoverRuckus.simpletests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

public class RunToPos extends LinearOpMode {
	private Robot robot = new Robot();
	
	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);
		robot.Hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.Hooke.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		waitForStart();
		robot.Hooke.setTargetPosition(-2000);
		while (robot.Hooke.isBusy()) ;
	}
}
