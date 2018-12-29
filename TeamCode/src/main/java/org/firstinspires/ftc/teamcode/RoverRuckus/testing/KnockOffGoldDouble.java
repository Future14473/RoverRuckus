package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldDoubleLook;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "Knock Off gold, double look", group = "Test")
public class KnockOffGoldDouble extends LinearOpMode {
	private GoldDoubleLook goldLooker = new GoldDoubleLook();
	private Robot robot = new Robot();
	
	@Override
	public void runOpMode() {
		goldLooker.init(hardwareMap);
		robot.init(hardwareMap);
		robot.drive.addLinearOpMode(this);
		waitForStart();
		goldLooker.start();
		robot.drive.turn(20, 1);
		int look;
		do look = goldLooker.look(); while (look == -1);
		robot.drive.turn(-20, 1);
		switch (look) {
			case 0:
				robot.drive.move(-45, 1, .6);
				break;
			case 1:
				robot.drive.move(0, 1, .4);
				break;
			case 2:
				robot.drive.move(45, 1, .6);
				break;
			default:
				throw new RuntimeException("This shouldn't happen");
		}
		robot.drive.move(0, 1, 0.3);
		robot.drive.move(0, 1, -0.3);
		robot.drive.waitForDone();
	}
}
