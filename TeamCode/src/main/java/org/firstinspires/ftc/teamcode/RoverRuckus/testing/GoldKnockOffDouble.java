package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDouble;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "Gold knockOff double look", group = "Test")
public class GoldKnockOffDouble extends LinearOpMode {
	private GoldLookDouble goldLooker = new GoldLookDouble();
	private Robot robot = new Robot();
	
	@Override
	public void runOpMode() throws InterruptedException {
		telemetry.addLine("init started... pls wait");
		telemetry.update();
		goldLooker.init(hardwareMap);
		robot.init(hardwareMap);
		robot.drive.addLinearOpMode(this);
		telemetry.addLine("Init done");
		telemetry.update();
		waitForStart();
		
		
		goldLooker.start();
		robot.drive.moveXY(-0.15, 0, 1);
		robot.drive.moveXY(0.15, 0.1, 1);
		robot.drive.turn(20, 1);
		robot.drive.waitForDone();
		int look;
		do look = goldLooker.look(); while (look == -1 && opModeIsActive());
		goldLooker.stop();
		telemetry.addData("Gold is at:", look);
		telemetry.update();
		robot.drive.turn(-20, 1);
		switch (look) {
			case 0:
				//robot.drive.move(-30, 1, .7);
				robot.drive.moveXY(-.5, .4, 1);
				break;
			case 1:
				//robot.drive.move(10, 1, .4);
				robot.drive.moveXY(.1, .4, 1);
				break;
			case 2:
				//robot.drive.move(60, 1, .7);
				robot.drive.moveXY(.7, .4, 1);
				break;
		}
		robot.drive.moveXY(0, 0.2, 1);
		robot.drive.moveXY(0, -0.2, 1);
		robot.drive.moveXY(-0.6 * look, 0, 1);
		robot.drive.waitForDone();
	}
}
