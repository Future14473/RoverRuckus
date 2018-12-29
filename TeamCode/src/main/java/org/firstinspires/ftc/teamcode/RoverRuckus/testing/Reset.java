package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "Reset", group = "reset")
public class Reset extends LinearOpMode {
	private Robot robot = new Robot();
	
	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);
		robot.drive.addLinearOpMode(this);
		waitForStart();
		robot.hooke.setPower(-1);
		int pastPos;
		do {
			pastPos = robot.hooke.getCurrentPosition();
			sleep(100);
		} while (robot.hooke.getCurrentPosition() < pastPos);
		robot.hooke.setPower(0);
		robot.hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.hooke.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.hooke.setTargetPosition(10000);
		robot.hooke.setPower(1);
		//*/
	}
}
