package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDouble;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "New Autonomous Test", group = "autonomous")
public class AutoTestNew extends LinearOpMode {
	private Robot robot = new Robot();
	private GoldLookDouble goldLooker = new GoldLookDouble();
	
	@Override
	public void runOpMode() throws InterruptedException {
		initialize();
		waitForStart();
		
		robot.hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		runTo(-33000, robot.hooke);
		
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
	
	private void knockOffGold() throws InterruptedException {
		goldLooker.start();
		robot.drive.move(270, 1, 0.15);
		robot.drive.move(0, 1, 0.1);
		robot.drive.turn(20, 1);
		robot.drive.waitForDone();
		int look;
		do look = goldLooker.look(); while (look == -1);
		goldLooker.stop();
		robot.drive.turn(-20, 1);
		switch (look) {
			case 0:
				//robot.drive.move(-30, 1, .7);
				robot.drive.moveXY(-.4, .5, 1);
				break;
			case 1:
				//robot.drive.move(10, 1, .4);
				robot.drive.moveXY(.1, .5, 1);
				break;
			case 2:
				//robot.drive.move(60, 1, .7);
				robot.drive.moveXY(.6, .5, 1);
				break;
		}
		robot.drive.move(0, 1, 0.2);
		robot.drive.move(0, 1, -0.2);
		robot.drive.move(-90, 1, 0.5 * look);
		robot.drive.waitForDone();
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
	
	private void PutMarkerInDepot() throws InterruptedException {
		robot.drive.move(260, 1, 1.05);
		robot.drive.turn(40, 1);
		robot.drive.move(-90, 0.3, 0.4);
		robot.drive.move(180, 1, 1);
		robot.drive.waitForDone();
		robot.marker.setPosition(0.9);
		sleep(500);
		robot.flicker.setPosition(0.65);
		sleep(500);
	}
	
	private void ParkInCrater() throws InterruptedException {
		robot.drive.move(0, 1, 2.5);
		robot.rotation.setPower(-1);
		sleep(3000);
		robot.rotation.setPower(0);
		robot.arm.setPower(1);
		sleep(1000);
		robot.arm.setPower(0);
	}
}
