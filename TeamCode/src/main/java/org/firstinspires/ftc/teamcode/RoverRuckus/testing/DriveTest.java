package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.mecanumdrive.MecanumDrive;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;

@Autonomous
@Disabled
public class DriveTest extends OurLinearOpMode {
	private MecanumDrive drive;
	
	@Override
	protected void initialize() {
	
	}
	
	@Override
	protected void run() {
		drive.move(0, 0.5, 1);
		drive.move(-90, 0.5, 1);
		drive.move(180, 0.5, 1);
		drive.move(90, 0.5, 1);
	}
}
