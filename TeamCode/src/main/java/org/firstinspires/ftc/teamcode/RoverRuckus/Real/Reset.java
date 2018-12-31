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
		telemetry.addLine("Doing Hook...");
		telemetry.update();
		robot.hooke.setPower(-1);
		int pastPos;
		do {
			pastPos = robot.hooke.getCurrentPosition();
			idle();
			sleep(100);
		} while (robot.hooke.getCurrentPosition() > pastPos && opModeIsActive());
		robot.hooke.setPower(0);
		telemetry.addLine("Doing Rotation...");
		telemetry.update();
		robot.rotation.setPower(-0.2);
		do {
			pastPos = robot.arm.getCurrentPosition();
			idle();
			sleep(100);
		} while (robot.rotation.getCurrentPosition() > pastPos && opModeIsActive());
		robot.arm.setPower(0);
		telemetry.addLine("Done");
		telemetry.update();
	}
}
