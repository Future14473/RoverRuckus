package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;


@Autonomous(name = "Cheese", group = "Test")
@Disabled
public class CheeseCode extends LinearOpMode {
	
	private Robot robot = new Robot();
	
	
	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);
		robot.drive.addLinearOpMode(this);
		waitForStart();
		robot.drive.move(0, 0.5f, 0.5f);
		//robot.drive.waitForDone();
		//sleep(1000);
		robot.drive.move(180, 0.5f, 1);
		//robot.drive.waitForDone();
		//sleep(1000);
		robot.drive.move(90, 0.5f, 0.5f);
		//robot.drive.waitForDone();
		//sleep(1000);
		robot.drive.move(270, 0.5f, 1);
		//robot.drive.waitForDone();
		//sleep(1000);
		while (opModeIsActive()) ;
	}
}
