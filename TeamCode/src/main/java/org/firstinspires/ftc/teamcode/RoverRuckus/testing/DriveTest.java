package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDrive;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.DecoratedOpMode;

@Autonomous
public class DriveTest extends DecoratedOpMode {
	protected MecanumDrive drive;
	
	@Override
	protected void initialize() throws InterruptedException {
	
	}
	
	@Override
	protected void run() throws InterruptedException {
		drive.move(0, 0.5, 1);
		drive.move(-90, 0.5, 1);
		drive.move(180, 0.5, 1);
		drive.move(90, 0.5, 1);
	}
}
