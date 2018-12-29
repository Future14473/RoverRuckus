package org.firstinspires.ftc.teamcode.RoverRuckus.simpletests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "ParkInCrater", group = "Test")
public class ParkInCrater extends LinearOpMode {
	private Robot robot = new Robot();
	
	@Override
	public void runOpMode() throws InterruptedException {
		robot.drive.addLinearOpMode(this);
		waitForStart();
		robot.init(hardwareMap);
		robot.drive.move(0, 1f, 2f);
		robot.Arm.setPower(-1);
		sleep(1000);
		robot.Arm.setPower(0);
		robot.Rotation.setPower(1);
		sleep(1000);
		robot.Rotation.setPower(0);
	}
}
