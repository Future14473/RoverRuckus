package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(name = "Reset", group = "reset")
public class Reset extends LinearOpMode {
	private Robot robot = new Robot();
	
	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);
		robot.drive.addLinearOpMode(this);
		waitForStart();
		
		robot.hooke.setPower(1);
		int pastPos;
		do {
			pastPos = robot.hooke.getCurrentPosition();
			sleep(100);
		} while (robot.hooke.getCurrentPosition() < pastPos && opModeIsActive());
		robot.hooke.setPower(0);
	}
}
