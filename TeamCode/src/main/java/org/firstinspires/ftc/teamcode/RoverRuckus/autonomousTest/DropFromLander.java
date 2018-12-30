package org.firstinspires.ftc.teamcode.RoverRuckus.autonomousTest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "DropFromLander", group = "Test")
@Disabled
public class DropFromLander extends LinearOpMode {
	private Robot robot = new Robot();	
	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);
		robot.drive.addLinearOpMode(this);
		robot.hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		waitForStart();
		
		runTo(robot.hooke.getCurrentPosition() - 31000, robot.hooke);
		
		robot.drive.move(270, 0.5, 0.1);
		robot.drive.move(0, 0.5, 0.1);
		robot.drive.move(90, 0.5, 0.1);
		
	}
	
	public boolean runTo(int encoder, DcMotor motor) {
		int dir = 0;
		
		if (motor.getCurrentPosition() > encoder) dir = 1;
		if (motor.getCurrentPosition() < encoder) dir = -1;
		//dir=1;
		motor.setPower(dir);
		//decreasing
		while (!((-1 * dir * (motor.getCurrentPosition() - encoder)) > 100)) {
			//wait
			
		}
		waitForDone(robot.hooke);
		motor.setPower(0);
		
		return true;
	}
	
	public void waitForDone(DcMotor motor) {
		while (motor.isBusy()) ;
	}
}
