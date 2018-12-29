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
		initialize();
		waitForStart();
		
		robot.Hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.Hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		runTo(-33000, robot.Hooke);
		
		
		goldLooker.start();
		robot.drive.move(270, 0.5, 0.14);
		robot.drive.waitForDone();
		
		knockOffGold();
		PutMarkerInDepot();
		ParkInCrater();
	}
	
	public boolean runTo(int encoder, DcMotor motor) {
		motor.setPower(1);
		//decreasing
		while (Math.abs(encoder - motor.getCurrentPosition()) > 100) {
			//wait
		}
		motor.setPower(0);
		return true;
	}
	
	private void knockOffGold() {
		robot.drive.move(55, 1, .57);
		//robot.drive.turn(5,1);
		robot.drive.waitForDone();
		boolean found = false;
		
		int i;
		int look; // -1 means nothing, 0 means white, 1 means gold
		for (i = 1; i >= -1; i--) {// -1 is left, 0 is center, 1 is right position
			do {
				look = closerLook();
			} while (look == -1);
			if (look == 1) { //found gold
				robot.drive.move(0, 1f, .35f); // move forwards to hit gold
				robot.drive.move(180, 1f, .2f); // move back
				found = true;
				break;
			}
			
			if (i != -1) {// has not traverse the 3 positions yet
				robot.drive.move(280, 1f, 17f / 36);
			}
		}
		if (!found) {
			telemetry.addData("NOT FOUND", "NOT FOUND");
			telemetry.update();
		}
		
		//robot.drive.move(90, 1f, (1 - i) * 17f / 36);
		
		if (i == 1) {
			robot.drive.move(280, 1f, 34f / 36);
		} else if (i == 0) {
			robot.drive.move(280, 1f, 17f / 36);
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
	
	private int closerLook() {
		int look = -1;
		while (look == -1) {
			look = goldLooker.look();
		}
		return look;
		/*
		robot.drive.move(0, 0.8, 2f / 36);
		robot.drive.move(180, 0.8, 2f / 36);
		robot.drive.move(270, 0.8, 2f / 36);
		robot.drive.move(0, 0.8, 2f / 36);
		robot.drive.waitForDone();
		look = goldLooker.look();
		if (look != -1) {
			return look;
		}
		robot.drive.move(180, 0.8, 2f / 36);
		robot.drive.move(90, 0.8, 4f / 36);
		robot.drive.move(0, 0.8, 2f / 36);
		robot.drive.waitForDone();
		look = goldLooker.look();
		return look;*/
	}
	
	private void initialize() {
		telemetry.addLine("Init started...");
		telemetry.addLine("Pls wait thx");
		telemetry.update();
		
		robot.init(hardwareMap);
		robot.drive.addLinearOpMode(this);
		goldLooker.init(hardwareMap);
		
		telemetry.addLine("Init done");
		telemetry.update();
	}
	
	private void PutMarkerInDepot() {
		robot.drive.move(260, 0.8, 1.05);
		robot.drive.turn(40, 0.5);
		robot.drive.move(180, 0.8, 1);
		robot.Marker.setPosition(0.9);
		sleep(500);
		robot.Flicker.setPosition(0.65);
		sleep(500);
	}
	
	private void ParkInCrater() {
		robot.drive.move(0, 1, 2.5);
		robot.Arm.setPower(-1);
		sleep(3000);
		robot.Arm.setPower(0);
		robot.Rotation.setPower(1);
		sleep(1000);
		robot.Rotation.setPower(0);
	}
}
