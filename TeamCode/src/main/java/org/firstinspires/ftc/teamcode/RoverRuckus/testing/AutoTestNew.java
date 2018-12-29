package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "AutoTestNew", group = "test")
public class AutoTestNew extends LinearOpMode {
	Robot robot = new Robot();
	
	//GoldLooker goldLooker = new GoldLooker(hardwareMap);
	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);
		waitForStart();
		robot.Hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.Hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		
		runTo(- 32000, robot.Hooke);
		
		robot.drive.move(270, 0.5, 0.07);
		robot.drive.move(0, 0.5, 0.05);
		robot.drive.move(90, 0.5, 0.07);
		robot.drive.waitForDone();
	}
	
	public boolean runTo(int encoder, DcMotor motor) {
		int dir;
		
		if (motor.getCurrentPosition() >= encoder) dir = 1;
		if (motor.getCurrentPosition() < encoder) dir = -1;
		motor.setPower(1);
		//decreasing
		while (Math.abs(encoder - motor.getCurrentPosition()) > 100){
			//wait
		}
		motor.setPower(0);
		
		return true;
	}
	
}
