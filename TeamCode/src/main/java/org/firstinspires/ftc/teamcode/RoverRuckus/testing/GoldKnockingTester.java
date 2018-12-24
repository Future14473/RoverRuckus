package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLooker;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.HardwareTestBot;

@Autonomous(name = "Gold Knocking Tester", group = "Test")
public class GoldKnockingTester extends LinearOpMode {
	
	private GoldLooker goldLooker;
	private HardwareTestBot robot = new HardwareTestBot();
	private boolean stop = false;
	
	@Override
	public void runOpMode() {
		robot.init(hardwareMap);
		goldLooker = new GoldLooker(hardwareMap);
		waitForStart();
		goldLooker.start();
		robot.drive.startMoveThread();
		robot.drive.move(90, 0.2f, 15f / 36, 500);
		robot.drive.move(-90, 0.2f, 15f / 36, 500);
		robot.drive.move(-90, 0.2f, 15f / 36, 500);
		while (opModeIsActive()) {
			if (stop) {
				if (!robot.drive.hasTasks()) break;
			} else {
				if (robot.drive.moveEndFlag) {
					robot.drive.moveEndFlag = false;
					int look = goldLooker.look();
					if (look == 1) {
						robot.drive.cancelTasks();
						robot.drive.move(0, 0.5f, 0.2f);
						robot.drive.move(180, 0.5f, 0.2f);
						stop = true;
					}
				}
			}
		}
	}
}
