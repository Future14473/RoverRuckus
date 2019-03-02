//package org.firstinspires.ftc.teamcode.RoverRuckus.old;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import org.firstinspires.ftc.teamcode.lib.opmode.GoldLookDouble;
//
//@Deprecated
//public abstract class AbstractAutoOld extends LinearOpMode {
//	private static final int            HOOK_TURN  = -25700;
//	// --Commented out by Inspection (1/22/2019 6:58 PM):private static final
//	// int ARM_TURN = -1500;
//	protected            OldRobot       robot      = new OldRobot();
//	private              ElapsedTime    timer      = new ElapsedTime();
//	private              GoldLookDouble goldLooker = new GoldLookDouble();
//	private              int            look       = -1;
//
//	protected abstract void putMarkerInDepot() throws InterruptedException;
//
//	@Override
//	public final void runOpMode() throws InterruptedException {
//		initialize();
//		waitForStart();
//
//		unHook();
//		knockOffGold();
//
//		putMarkerInDepot();
//		putMarkerInDepot();
//
//		extra();
//
//		parkInCrater();
//		finish();
//	}
//
//	private void initialize() {
//		telemetry.addLine("Init started...");
//		telemetry.addLine("Pls wait thx");
//		telemetry.update();
//
//		robot.init(hardwareMap);
//		robot.drive.addLinearOpMode(this);
//		goldLooker.init(hardwareMap);
//
//		telemetry.addLine("Init done");
//		telemetry.update();
//	}
//
//	private void unHook() throws InterruptedException {
//		robot.hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//		robot.hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//		robot.hooke.setPower(1);
//		//decreasing
//		while (Math.abs(HOOK_TURN - robot.hooke.getCurrentPosition()) > 50 &&
//		       opModeIsActive()) {
//			idle();
//		}
//		robot.hooke.setPower(0);
//		robot.drive.moveXY(-0.15, 0.1, 10);
//		robot.drive.moveXY(0, 0.1, 10);
//		robot.drive.turn(-10, 10);
//	}
//
//	protected void knockOffGold() throws InterruptedException {
//		goldLooker.start();
//		timer.reset();
//		while (look == -1 && timer.milliseconds() < 5000 && opModeIsActive())
//			look = goldLooker.getLook();
//		if (look == -1) {
//			look = 2;
//			telemetry.addLine("FAIL-SAFE HAPPENED");
//		} else look = (look + 2) % 3;
//		goldLooker.stop();
//		telemetry.addData("Gold is at:", (look == 0) ? "left" :
//		                                 ((look == 1) ? "middle" : "right"));
//		telemetry.update();
//		robot.drive.waitForDone();
//		robot.hooke.setPower(-1); //INTERLUDE
//		robot.drive.moveXY(-0.35 + 0.5 * look, 0.4, 10);
//		robot.drive.moveXY(0, 0.25, 10);
//		robot.drive.moveXY(0, -0.25, 10);
//		robot.drive.moveXY(-0.5 * look - 0.5, 0, 10);
//		robot.drive.waitForDone();
//	}
//
//	private void putMarkerInDepot() throws InterruptedException {
//		robot.drive.waitForDone();
//		//deposit
//		robot.marker.setPosition(0.9);
//		sleep(1000);
//		robot.flicker.setPosition(0.65);
//		sleep(1000);
//	}
//
//	public void extra() throws InterruptedException {
//	}
//
//	private void parkInCrater() throws InterruptedException {
//		robot.drive.moveXY(0, 1.7, 10);
//		robot.rotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//		robot.rotation.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//		robot.rotation.setPower(0.6);
//		sleep(1000);
//		robot.rotation.setPower(0);
//		robot.arm.setPower(-1);
//		sleep(1000);
//		robot.arm.setPower(0);
//		robot.drive.waitForDone();
//	}
//
//	private void finish() {
//		goldLooker.stop();
//		while (Math.abs(robot.hooke.getCurrentPosition()/* - 0 */) > 20 &
//		       opModeIsActive()) idle();
//		robot.hooke.setPower(0);
//	}
//}
