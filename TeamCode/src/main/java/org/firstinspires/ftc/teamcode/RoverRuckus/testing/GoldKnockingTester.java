package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLooker;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "Gold Knocking Tester", group = "Test")
public class GoldKnockingTester extends LinearOpMode {
	
	private GoldLooker goldLooker;
	private Robot robot = new Robot();
	private boolean found = false;
	//private int pos = -1;
	
	@Override
	public void runOpMode() {
		telemetry.addLine("Init started...");
		telemetry.addLine("Pls wait thx");
		telemetry.update();
		robot.init(hardwareMap);
		goldLooker = new GoldLooker(hardwareMap);
		robot.Hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.Hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		telemetry.addLine("Init done");
		telemetry.update();
		waitForStart();
		//runto(-32000);
		goldLooker.start();
		
		robot.drive.move(290, 1, .1);
		robot.drive.move(55, 1, .57);
		robot.drive.waitForDone();
		int i, look; // -1 means nothing, 0 means white, 1 means gold
		for (i = 1; i >= -1; i--) {// -1 is left, 0 is center, 1 is right position
			telemetry.addData("i is:", i);
			telemetry.addLine("looking...");
			telemetry.update();
			look = closerLook();
			telemetry.addData("look is:", look);
			telemetry.update();
			if (look == 1) { //found gold
				robot.drive.move(0, .5, .3); // move forwards to hit gold
				robot.drive.move(180, .5, .3); // move back
				robot.drive.waitForDone();
				found = true;
				break;
			}
			//is white.
			if (i != -1) {// has not traverse the 3 positions yet
				robot.drive.move(270, 1, 17.0 / 36);
				robot.drive.waitForDone();
			}
		}
		if (!found) {
			telemetry.addLine("NOT FOUND, MOVING ON WITH LIFE");
			telemetry.update();
		}
		
		if (i == 1) {
			robot.drive.move(270, 1, 34.0 / 36);
		} else if (i == 0) {
			robot.drive.move(270, 1, 17.0 / 36);
		}
	}
	
	private int closerLook() {
		int look = -1;
		while (look == -1) {
			if (!robot.drive.hasTasks()) {
				//robot.drive.move(0, 0.3f, 2.0 / 36);
				//robot.drive.move(180, 0.3f, 2.0 / 36);
//				robot.drive.move(270, 0.5f, 2.0 / 36);
//				robot.drive.move(90, 0.5f, 2.0 / 36);
//				robot.drive.move(270, 0.5f, 2.0 / 36);
//				//robot.drive.move(0, 0.3f, 2.0 / 36);
//				//robot.drive.move(180, 0.3f, 2.0 / 36);
//				robot.drive.move(90, 0.5f, 4.0 / 36);
				//robot.drive.move(0, 0.3f, 2.0 / 36);
			}
			look = goldLooker.look();
		}
		robot.drive.cancelTasks();
		return look;
	}
}

