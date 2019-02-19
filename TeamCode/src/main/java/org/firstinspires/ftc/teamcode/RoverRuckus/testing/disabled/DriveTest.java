package org.firstinspires.ftc.teamcode.RoverRuckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDrive;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.CurRobot;

@Autonomous
@Disabled
public class DriveTest extends OurLinearOpMode {
	private MecanumDrive drive;
	
	@Override
	protected void initialize() {
		CurRobot robot = new CurRobot(hardwareMap);
		drive = new MecanumDrive(robot, new MecanumDrive.Parameters());
	}
	
	@Override
	protected void run() throws InterruptedException {
		drive.move(0, 0.5, 1);
		drive.move(-90, 0.5, 1);
		drive.move(180, 0.5, 1);
		drive.move(90, 0.5, 1);
		drive.waitUntilDone();
	}
}
