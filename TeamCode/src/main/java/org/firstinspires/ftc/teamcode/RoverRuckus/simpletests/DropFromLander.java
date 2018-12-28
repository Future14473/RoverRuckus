package org.firstinspires.ftc.teamcode.RoverRuckus.simpletests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "DropFromLander", group = "Test")
public class DropFromLander extends LinearOpMode {
	private Robot robot = new Robot();
	
	@Override
	public void runOpMode() throws InterruptedException {
		waitForStart();
		robot.init(hardwareMap);
		robot.Hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.Hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		
		runto(robot.Hooke.getCurrentPosition() - 31000, robot.Hooke);
	}
	
	public static boolean runto(int encoder, DcMotor motor) {
		int dir = 0;
		
		if (motor.getCurrentPosition() > encoder) dir = 1;
		if (motor.getCurrentPosition() < encoder) dir = -1;
		//dir=1;
		motor.setPower(dir);
		//decreasing
		while (!((-1 * dir * (motor.getCurrentPosition() - encoder)) > 100)) {
			//wait
		}
		
		motor.setPower(0);
		
		return true;
	}
}
