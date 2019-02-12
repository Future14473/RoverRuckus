package org.firstinspires.ftc.teamcode.RoverRuckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDrive;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpModePrinted;

@Autonomous
@Disabled
public class DriveTest extends OurLinearOpModePrinted {
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
