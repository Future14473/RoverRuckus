package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDouble;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "Auto Next to Depot", group = "autonomous")
public class AutoNextToDepot extends LinearOpMode {
	private Robot robot = new Robot();
	private GoldLookDouble goldLooker = new GoldLookDouble();
	
	@Override
	public void runOpMode() throws InterruptedException {
		initialize();
		waitForStart();
		
		unHook();
		knockOffGold();
		putMarkerInDepot();
		//parkInCrater();
	}
	
	private void unHook() {
		robot.hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		robot.hooke.setPower(1);
		//decreasing
		while (Math.abs(-32000 - robot.hooke.getCurrentPosition()) > 100 && opModeIsActive()) {
			idle();
		}
		robot.hooke.setPower(0);
	}
	
	private void knockOffGold() throws InterruptedException {
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
				robot.drive.moveXY(-.5, .4, 1);
				break;
			case 1:
				robot.drive.moveXY(.1, .4, 1);
				break;
			case 2:
				robot.drive.moveXY(.7, .4, 1);
				break;
		}
		robot.drive.moveXY(0, 0.2, 1);
		robot.drive.moveXY(0, -0.2, 1);
		robot.drive.moveXY(-0.6 * look - 0.6, 0, 1);
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
		robot.drive.turn(-135, 1); //turn
		robot.drive.moveXY(0.5, 0, 0.5); //wall hug
		robot.drive.moveXY(0, -1, 1); //go to crater
		robot.drive.waitForDone();
		//deposit
		robot.marker.setPosition(0.9);
		sleep(500);
		robot.flicker.setPosition(0.65);
		sleep(500);
	}
	
	private void parkInCrater() throws InterruptedException {
		robot.drive.moveXY(0, 2, 1);
		robot.rotation.setPower(-1);
		sleep(3000);
		robot.rotation.setPower(0);
		robot.arm.setPower(1);
		sleep(1000);
		robot.arm.setPower(0);
	}
}
