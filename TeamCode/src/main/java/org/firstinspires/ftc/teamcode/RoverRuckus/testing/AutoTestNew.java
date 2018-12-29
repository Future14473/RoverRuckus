package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldDoubleLooker;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "AutoTestNew", group = "test")
public class AutoTestNew extends LinearOpMode {
	private Robot robot = new Robot();
	private GoldDoubleLooker goldLooker = new GoldDoubleLooker();
	
	@Override
	public void runOpMode() throws InterruptedException {
		initialize();
		waitForStart();
		
		robot.Hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.Hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		runTo(-33000, robot.Hooke);
		
		robot.drive.move(270, 1, 0.15);
		robot.drive.move(0, 1, 0.1);
		robot.drive.waitForDone();
		
		knockOffGold();
		PutMarkerInDepot();
		ParkInCrater();
	}
	
	private void runTo(int encoder, DcMotor motor) {
		motor.setPower(1);
		//decreasing
		while (Math.abs(encoder - motor.getCurrentPosition()) > 100) {
			//wait
		}
		motor.setPower(0);
	}
	
	private void knockOffGold() {
		goldLooker.start();
		robot.drive.turn(20, 1);
		int look;
		do look = goldLooker.look(); while (look == -1);
		goldLooker.stop();
		robot.drive.turn(-20, 1);
		switch (look) {
			case 0:
				robot.drive.move(-45, 1, .6);
				break;
			case 1:
				robot.drive.move(0, 1, .3);
				break;
			case 2:
				robot.drive.move(45, 1, .6);
				break;
			default:
				throw new RuntimeException("This shouldn't happen");
		}
		robot.drive.move(0, 1, 0.3);
		robot.drive.move(0, 1, -0.3);
		robot.drive.move(-90, 1, 17.0 / 36 * look);
		robot.drive.waitForDone();
		
	}
	
/*
	private int closerLook() {
		int look = -1;
		while (look == -1) {
			look = goldLooker.look();
		}
		return look;
		*/
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
		return look;*//*

	}
*/
	
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
