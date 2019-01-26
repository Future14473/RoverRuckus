package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.DrivingLinearOpMode;

@Autonomous
public class DriveTest extends DrivingLinearOpMode {
	@Override
	protected void initialize() throws InterruptedException {
	
	}
	
	@Override
	protected void run() throws InterruptedException {
		drive.move(0, 0.5, 1);
		drive.sleep(100);
		drive.move(-90,0.5,1);
		drive.sleep(100);
		drive.move(180, 0.5, 1);
		drive.sleep(100);
		drive.move(90, 0.5, 1);
		drive.sleep(100);
		for (int i = 0; i < 10; i++) {
			drive.move(40, 0.1, 1);
			drive.move(40+180, 0.1, 1);
		}
	}
}
