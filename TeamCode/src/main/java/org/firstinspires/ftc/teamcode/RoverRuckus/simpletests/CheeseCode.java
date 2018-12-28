package org.firstinspires.ftc.teamcode.RoverRuckus.simpletests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.HardwareTestBot;


@Autonomous(name = "Cheese", group = "Test")
public class CheeseCode extends LinearOpMode {
	
	private HardwareTestBot robot = new HardwareTestBot();
	
	
	@Override
	public void runOpMode() {
		robot.init(hardwareMap);
		waitForStart();
            /*
            move seems to have issues because the setPower method works find but move method doesn't work
             */
		robot.drive.move(0, 0.5f, 0.5f);
		//robot.drive.waitForDone();
		//sleep(1000);
		robot.drive.move(180, 0.5f, 0.5f);
		//robot.drive.waitForDone();
		//sleep(1000);
		robot.drive.move(90, 0.5f, 0.5f);
		//robot.drive.waitForDone();
		//sleep(1000);
		robot.drive.move(270, 0.5f, 0.5f);
		//robot.drive.waitForDone();
		//sleep(1000);
		robot.drive.waitForDone();
	}
}
