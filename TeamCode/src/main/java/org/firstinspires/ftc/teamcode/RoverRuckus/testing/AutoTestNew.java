package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLooker;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "AutoTestNew", group = "test")
public class AutoTestNew extends LinearOpMode {
	private Robot robot = new Robot();
	private GoldLooker goldLooker = new GoldLooker();
	
	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);
		robot.drive.addLinearOpMode(this);
		goldLooker.init(hardwareMap);
		waitForStart();
		/*
		robot.Hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.Hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		runTo(-33000, robot.Hooke);
		*/
		
		goldLooker.start();
		robot.drive.move(270, 0.5, 0.07);
		robot.drive.move(0, 0.5, 0.05);
		robot.drive.move(55, 1f, .57f);
		robot.drive.waitForDone();
		
		knockOffGold();
	}
	public boolean runTo(int encoder, DcMotor motor) {
		int dir;
		
		if (motor.getCurrentPosition() >= encoder) dir = 1;
		if (motor.getCurrentPosition() < encoder) dir = -1;
		motor.setPower(1);
		//decreasing
		while (Math.abs(encoder - motor.getCurrentPosition()) > 100) {
			//wait
		}
		motor.setPower(0);
		return true;
	}
	
	private void knockOffGold() {
		boolean found = false;
		int i;
		int look = -1; // -1 means nothing, 0 means white, 1 means gold
		for (i = 1; i >= -1; i--) {// -1 is left, 0 is center, 1 is right position
			if (look == -1) {
				look = goldLooker.look();
				while (look == -1) {
					look = closerLook();
				}
			}
			if (look == 1) { //found gold
				robot.drive.move(0, .5f, .3f); // move forwards to hit gold
				robot.drive.move(180, .5f, .3f); // move back
				found = true;
				break;
			}
			
			if (i != -1) {// has not traverse the 3 positions yet
				robot.drive.move(270, 1f, 17f / 36);
				look = -1;
			}
		}
		if (!found) {
			telemetry.addData("NOT FOUND", "NOT FOUND");
			telemetry.update();
		}
		
		//robot.drive.move(90, 1f, (1 - i) * 17f / 36);
		
		if (i == 1) {
			robot.drive.move(270, 1f, 34f / 36);
		} else if (i == 0) {
			robot.drive.move(270, 1f, 17f / 36);
		}
		
	}
	
	private int closerLook() {
		int look;
		robot.drive.move(0, 0.3f, 2f / 36);
		look = goldLooker.look();
		if (look != -1) {
			return look;
		}
		robot.drive.move(180, 0.3f, 2f / 36);
		robot.drive.move(270, 0.3f, 2f / 36);
		robot.drive.move(0, 0.3f, 2f / 36);
		look = goldLooker.look();
		if (look != -1) {
			return look;
		}
		robot.drive.move(180, 0.3f, 2f / 36);
		robot.drive.move(90, 0.3f, 4f / 36);
		robot.drive.move(0, 0.3f, 2f / 36);
		look = goldLooker.look();
		return look;
	}
}
