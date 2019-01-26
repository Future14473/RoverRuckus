package org.firstinspires.ftc.teamcode.RoverRuckus.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.old.OldRobot;

@Autonomous(name = "ParkInCrater", group = "Test")
@Disabled
public class ParkInCrater extends LinearOpMode {
	private OldRobot robot = new OldRobot();
	
	@Override
	public void runOpMode() throws InterruptedException {
		robot.drive.addLinearOpMode(this);
		waitForStart();
		robot.init(hardwareMap);
		robot.drive.move(0, 2f, 1f);
		robot.rotation.setPower(-1);
		sleep(1000);
		robot.rotation.setPower(0);
		robot.arm.setPower(1);
		sleep(1000);
		robot.arm.setPower(0);
	}
}
