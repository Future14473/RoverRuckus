package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldDoubleLooker;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "Knock Off gold, double look", group = "Test")
public class DoubleKnockOffGold extends LinearOpMode {
	private GoldDoubleLooker goldLooker = new GoldDoubleLooker();
	private Robot robot = new Robot();
	
	@Override
	public void runOpMode() {
		goldLooker.init(hardwareMap);
		robot.init(hardwareMap);
		robot.drive.addLinearOpMode(this);
		waitForStart();
		goldLooker.start();
		robot.drive.turn(20, 0.5);
		int look;
		do look = goldLooker.look(); while (look == -1);
		robot.drive.turn(-20, 0.5);
		switch (look) {
			case 0:
				robot.drive.move(-45, 0.5, .6);
				break;
			case 1:
				robot.drive.move(0, 0.5, .4);
				break;
			case 2:
				robot.drive.move(45, 0.5, .6);
				break;
			default:
				throw new RuntimeException("This shouldn't happen");
		}
		robot.drive.move(0, 0.5, 0.3);
		robot.drive.move(0, 0.5, -0.2);
		robot.drive.waitForDone();
	}
}
