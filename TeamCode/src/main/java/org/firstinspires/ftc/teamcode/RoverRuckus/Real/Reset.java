package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "Reset", group = "reset")
public class Reset extends LinearOpMode {
	private Robot robot = new Robot();
	
	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);
		robot.drive.addLinearOpMode(this);
		waitForStart();
		robot.flicker.setPosition(0);
		robot.marker.setPosition(0);
		
		robot.hooke.setPower(-1);
		int pastPos;
		do {
			pastPos = robot.hooke.getCurrentPosition();
			idle();
			sleep(100);
		} while (robot.hooke.getCurrentPosition() > pastPos && opModeIsActive());
		robot.hooke.setPower(0);
		
		robot.rotation.setPower(-0.001);
		do {
			pastPos = robot.arm.getCurrentPosition();
			idle();
			sleep(100);
		} while (robot.rotation.getCurrentPosition() > pastPos && opModeIsActive());
		robot.arm.setPower(0);
	}
}
