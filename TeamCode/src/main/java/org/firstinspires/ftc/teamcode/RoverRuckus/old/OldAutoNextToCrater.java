package org.firstinspires.ftc.teamcode.RoverRuckus.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDouble;

@Autonomous(name = "Old Auto Next to Crater", group = "autonomous")
@Disabled
public class OldAutoNextToCrater extends LinearOpMode {
	private OldRobot       robot      = new OldRobot();
	private GoldLookDouble goldLooker = new GoldLookDouble();
	
	@Override
	public void runOpMode() throws InterruptedException {
		initialize();
		waitForStart();
		
		unHook();
		knockOffGold();
		robot.hooke.setPower(-1);
		putMarkerInDepot();
		parkInCrater();
		while (Math.abs(robot.hooke.getCurrentPosition()) > 50) idle();
		robot.hooke.setPower(0);
	}
	
	private void unHook() {
		robot.hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		robot.hooke.setPower(1);
		//decreasing
		while (Math.abs(-26000 - robot.hooke.getCurrentPosition()) > 100 &&
		       opModeIsActive()) {
			idle();
		}
		robot.hooke.setPower(0);
	}
	
	private void knockOffGold() throws InterruptedException {
		goldLooker.start();
		robot.drive.moveXY(-0.15, 0, 10);
		robot.drive.moveXY(0, 0.1, 10);
		int look;
		do look = goldLooker.getLook(); while (look == -1 && opModeIsActive());
		look = (look + 2) % 3;
		goldLooker.stop();
		telemetry.addData("Gold is at:", look);
		telemetry.update();
		robot.drive.moveXY(-.35 + .5 * look, .4, 10);
		robot.drive.moveXY(0, 0.25, 10);
		robot.drive.moveXY(0, -0.25, 10);
		robot.drive.moveXY(-0.5 * look - 1.2, 0, 10);
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
	
	private void putMarkerInDepot() throws InterruptedException {
		robot.drive.turn(45, 10); //turn
		robot.drive.moveXY(-0.2, 0, 10);
		robot.drive.moveXY(0, -1.1, 10); //go to depot
		robot.drive.waitForDone();
		//deposit
		robot.marker.setPosition(0.9);
		sleep(500);
		robot.flicker.setPosition(0.65);
	}
	
	private void parkInCrater() throws InterruptedException {
		robot.drive.moveXY(0, 1.7, 10);
		robot.arm.setPower(1);
		robot.rotation.setPower(1);
		sleep(500);
		robot.arm.setPower(0);
		sleep(1000);
		robot.rotation.setPower(0);
		robot.drive.waitForDone();
	}
}
