package org.firstinspires.ftc.teamcode.RoverRuckus.autonomousTest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDouble;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OldRobot;

@Autonomous(name = "Gold knockOff double look", group = "Test")
@Disabled
public class GoldKnockOffDouble extends LinearOpMode {
	private GoldLookDouble goldLooker = new GoldLookDouble();
	private OldRobot robot = new OldRobot();
	
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
		robot.drive.moveXY(-0.15, 0, 10);
		robot.drive.moveXY(0, 0.1, 10);
		int look;
		do look = goldLooker.look(); while (look == -1 && opModeIsActive());
		look = (look + 2) % 3;
		goldLooker.stop();
		telemetry.addData("Gold is at:", look);
		telemetry.update();
		switch (look) {
			case 0:
				robot.drive.moveXY(-.25, .4, 10);
				break;
			case 1:
				robot.drive.moveXY(.25, .4, 10);
				break;
			case 2:
				robot.drive.moveXY(.75, .4, 10);
				break;
		}
		robot.drive.moveXY(0, 0.2, 10);
		robot.drive.moveXY(0, -0.2, 10);
		robot.drive.moveXY(-0.5 * look, 0, 10); //CHANGE THIS TO INCLUDE MORE DISTANCE
		robot.drive.waitForDone();
	}
}
