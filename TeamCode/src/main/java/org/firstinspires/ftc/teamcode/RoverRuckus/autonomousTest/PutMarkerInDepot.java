package org.firstinspires.ftc.teamcode.RoverRuckus.autonomousTest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OldRobot;

@SuppressWarnings("ALL")
@Autonomous(name = "PutMarkerInDepot", group = "Test")
@Disabled
public class PutMarkerInDepot extends LinearOpMode {
	private OldRobot robot = new OldRobot();
	
	@Override
	public void runOpMode() throws InterruptedException {
		robot.drive.addLinearOpMode(this);
		waitForStart();
		while (opModeIsActive()) {
			robot.init(hardwareMap);
			//robot.drive.move(260f, 1f, 2f);
			//robot.drive.turn(90, 0.5f);
			robot.marker.setPosition(0.84);
			sleep(2000);
			robot.flicker.setPosition(0.65);
			sleep(5000);
		}
	}
}
