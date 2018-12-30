package org.firstinspires.ftc.teamcode.RoverRuckus.simpletests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookSingle;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "KnockOffGoldDiagonal", group = "Test")
@Disabled
public class KnockOffGold extends LinearOpMode {
	private GoldLookSingle goldLookSingle = new GoldLookSingle();
	private Robot robot = new Robot();
	private boolean stop = false;
	private boolean found = false;
	
	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);
		goldLookSingle.init(hardwareMap);
		robot.drive.addLinearOpMode(this);
		waitForStart();
		goldLookSingle.start();
		robot.drive.move(55, 1f, .75f); //move forwards
		
		int i;
		int look = -1; // -1 means nothing, 0 means white, 1 means gold
		for (i = 1; i >= -1; i--) {// -1 is left, 0 is center, 1 is right position
			
			while (look == -1) {
				look = goldLookSingle.look();
				while (look == -1) {
					look = closerLook(robot, goldLookSingle);
				}
			}
			if (look == 1) { //found gold
				robot.drive.move(0, .5f, .3f); // move forwards to hit gold
				robot.drive.move(180, .5f, .3f); // move back
				found = true;
				break;
			}
			
			if (i != -1) {// has not traverse the 3 positions yet
				robot.drive.move(278, 1f, 19f / 36);
				look = -1;
			}
		}
		if (!found) {
			telemetry.addData("NOT FOUND", "NOT FOUND");
			telemetry.update();
		}
		
		//robot.drive.move(90, 1f, (1 - i) * 17f / 36);
		
		if (i == 1) {
			robot.drive.move(278, 1f, 38f / 36);
		} else if (i == 0) {
			robot.drive.move(278, 1f, 19f / 36);
		}
		
		//robot.drive.move(180, 1f, 0.4f);
		//robot.drive.stopRobot();

            /*
            if (found) {
                break;
            }
            */
		//}
	}
	
	public static int closerLook(Robot robot, GoldLookSingle goldLookSingle) throws InterruptedException {
		int look;
		robot.drive.move(0, 0.3f, 2f / 36);
		look = goldLookSingle.look();
		if (look != -1) {
			return look;
		}
		robot.drive.move(180, 0.3f, 2f / 36);
		robot.drive.move(270, 0.3f, 2f / 36);
		robot.drive.move(0, 0.3f, 2f / 36);
		robot.drive.waitForDone();
		look = goldLookSingle.look();
		if (look != -1) {
			return look;
		}
		robot.drive.move(180, 0.3f, 2f / 36);
		robot.drive.move(90, 0.3f, 4f / 36);
		robot.drive.move(0, 0.3f, 2f / 36);
		robot.drive.waitForDone();
		look = goldLookSingle.look();
		return look;
	}
}
