package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDrive;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskProgram;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDoubleCallable;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.SimpleCondition;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.SheetMetalRobot;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Base autonomous.
 */
public abstract class AbstractAuto2 extends OurLinearOpMode {
	private static final int    HOOK_TURN_START_LOOK = -21000;
	private static final int    HOOK_TURN_END        = -26000;
	private static final double PARKER_POSITION_OUT  = 0;
	
	protected MecanumDrive    drive;
	//Moves bot
	private   SheetMetalRobot robot;
	private   TaskProgram     hookAndLook;
	//runs goldLooking and hook retraction
	private   Future<Integer> goldLook;
	private   SimpleCondition startGoldLook = new SimpleCondition();
	private   SimpleCondition unHooked      = new SimpleCondition();
	
	protected abstract void positionForDepot() throws InterruptedException;
	
	protected abstract void parkInCrater();
	
	@Override
	protected void initialize() {
		robot = new SheetMetalRobot(hardwareMap);
		hookAndLook = new TaskProgram();
		drive = new MecanumDrive(robot, new MecanumDrive.Parameters());
		loadLookAndHook();
		resetEncoders();
	}
	
	@Override
	public final void run() throws InterruptedException {
		unHook();
		knockOffGold();
		
		positionForDepot();
		
		putMarkerInDepot();
		
		robot.parker.setPosition(PARKER_POSITION_OUT);
		parkInCrater();
		
		drive.waitUntilDone();
		hookAndLook.waitUntilDone();
	}
	
	@Override
	protected void cleanup() {
		drive.stop();
		hookAndLook.stop();
	}
	
	private void loadLookAndHook() {
		//preload lookAndHook tasks.
		hookAndLook.add(startGoldLook::awaitTrue);//
		goldLook = hookAndLook.submit(new GoldLookDoubleCallable(hardwareMap));
		//hurray for method chain calls. unnecessary but it looks cool
		hookAndLook.then(unHooked::awaitTrue).then(this::retractHook).thenStop();
	}
	
	private void resetEncoders() {
		//Reset encoders.
		robot.collectArm.setMode(STOP_AND_RESET_ENCODER);
		robot.collectArm.setMode(RUN_WITHOUT_ENCODER);
		robot.scoreArm.setMode(STOP_AND_RESET_ENCODER);
		robot.scoreArm.setMode(RUN_WITHOUT_ENCODER);
		robot.hook.setMode(STOP_AND_RESET_ENCODER);
		robot.hook.setMode(RUN_WITHOUT_ENCODER);
	}
	
	private void retractHook() {
		robot.hook.setPower(-0.7);
		while (Math.abs(robot.hook.getCurrentPosition()/* - 0 */) > 100) {
			Thread.yield();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				break;
			}
		}
		robot.hook.setPower(0);
	}
	
	private void unHook() throws InterruptedException {
		robot.hook.setPower(1);
		//decreasing
		waitUntil(() -> robot.hook.getCurrentPosition() <= HOOK_TURN_START_LOOK);
		startGoldLook.setTrue();
		telemetry.addLine("GOLD LOOK STARTED");
		telemetry.update();
		waitUntil(() -> robot.hook.getCurrentPosition() <= HOOK_TURN_END);
		robot.hook.setPower(0);
		drive.moveXY(-0.15, 0.05, 10).then(unHooked::setTrue).moveXY(0, 0.1, 10).rotate(-10, 10);
	}
	
	private void knockOffGold() throws InterruptedException {
		int look = 0;
		try {
			look = goldLook.get(4, SECONDS);
		} catch (ExecutionException e) {
			e.getCause().printStackTrace();
		} catch (TimeoutException ignored) {
			telemetry.addLine("FAIL-SAFE HAPPENED");
			look = 2;
		}
		goldLook.cancel(true);
		telemetry.addData("Gold is at:", (look == 0) ? "left" : ((look == 1) ? "middle" : "right"
		));
		telemetry.update();
		drive.moveXY(-0.35 + 0.5 * look, 0.45, 10).moveXY(0, 0.25, 10)//knock off
		     .moveXY(0, -0.35, 10).moveXY(-0.6 - 0.5 * look, 0.05, 10);
	}
	
	private void putMarkerInDepot() throws InterruptedException {
		//deposit
		drive.waitUntilDone();
		robot.markerDoor.setPosition(0.9);
		sleep(1000);
		robot.flicker.setPosition(0);
		sleep(500);
	}
}
