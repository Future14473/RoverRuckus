package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLooker;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.HardwareTestBot;

@Autonomous(name = "Gold Knocking Tester", group = "Test")
public class GoldKnockingTester extends OpMode {
	
	private GoldLooker goldLooker;
	private HardwareTestBot robot = new HardwareTestBot();
	private boolean stop = false;
	
	@Override
	public void init() {
		robot.init(hardwareMap);
		goldLooker = new GoldLooker(hardwareMap);
	}
	
	@Override
	public void start() {
		goldLooker.start();
		robot.drive.move(0, 1f, .4f);
		robot.drive.move(-90, 1f, 17f / 36);
		robot.drive.waitForDone();
		robot.drive.resetCoords();
		int i;
		for (i = 0; i < 3; i++) {
			int look = -1;
			while (look == -1) {
				look = goldLooker.look();
			}
			if (look == 1) {
				robot.drive.move(0, .5f, .3f);
				robot.drive.move(180, .5f, .3f);
				robot.drive.waitForDone();
				break;
			}
			if (i != 2)
				robot.drive.move(90, 1f, 17f / 36);
			robot.drive.waitForDone();
		}
		robot.drive.move(90, 1f, (1 - i) * 17f / 36);
	}
	
	
	@Override
	public void loop() {
/*
		if (stop) {
			if (!robot.drive.hasTasks()) requestOpModeStop();
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
//*/
	}
}
