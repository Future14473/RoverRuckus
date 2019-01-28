package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.DecoratedLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MecanumDrive;

@Autonomous
@Disabled
public class DriveTest extends DecoratedLinearOpMode {
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
	
	@Override
	protected void cleanup() {}
}
