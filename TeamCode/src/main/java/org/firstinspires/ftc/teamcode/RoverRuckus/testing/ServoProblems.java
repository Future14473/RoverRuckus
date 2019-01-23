package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@SuppressWarnings("ALL")
@TeleOp
@Disabled
public class ServoProblems extends LinearOpMode {
	private Robot robot = new Robot();
	
	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);
		robot.drive.addLinearOpMode(this);
		
		waitForStart();
		telemetry.addLine("START");
		telemetry.update();
		sleep(2000);
		robot.marker.setPosition(0.9);
		telemetry.addLine("MARKER");
		telemetry.update();
		sleep(2000);
		robot.flicker.setPosition(0.65);
		telemetry.addLine("FLICKER");
		telemetry.update();
		sleep(2000);
		telemetry.addLine("DONE");
		telemetry.update();
		sleep(2000);
	}
}
