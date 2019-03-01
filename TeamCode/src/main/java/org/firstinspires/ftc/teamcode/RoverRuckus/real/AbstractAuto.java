package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import org.firstinspires.ftc.teamcode.RoverRuckus.Constants;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDrive;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskProgram;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.GoldLookDoubleCallable;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.SimpleCondition;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.CurRobot;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.FLICKER_OUT;
import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.MARKER_DOOR_OPEN;

/**
 * Base autonomous.
 */
public abstract class AbstractAuto extends OurLinearOpMode {
	private static final int             HOOK_TURN_END = -11000;
	private final        AtomicLong      detectTime    = new AtomicLong();
	protected            MecanumDrive    drive;
	protected            TaskProgram     lookAndHook;
	//Moves bot
	protected            CurRobot        robot;
	//runs goldLooking and hook retraction
	private              Future<Integer> goldLook;
	private              SimpleCondition startGoldLook = new SimpleCondition();
	private              SimpleCondition unHooked      = new SimpleCondition();
	
	protected abstract void putMarkerInDepot() throws InterruptedException;
	
	protected abstract void parkInCrater();
	
	protected void flickMarkerOut() {
		robot.flicker.setPosition(FLICKER_OUT);
		sleep(500);
	}
	
	protected void openMarkerDoor() {
		robot.markerDoor.setPosition(MARKER_DOOR_OPEN);
	}
	
	@Override
	protected void initialize() throws InterruptedException {
		robot = new CurRobot(hardwareMap);
		robot.initIMU();
		lookAndHook = new TaskProgram();
		drive = new MecanumDrive(robot, new MecanumDrive.Parameters());
		loadLookAndHook();
		resetEncoders();
		waitUntil(robot::imuIsGyroCalibrated, 3, SECONDS);
	}
	
	@Override
	public void run() throws InterruptedException {
		unHook();
		knockOffGold();
		putMarkerInDepot();
		parkInCrater();
		drive.then(this::putDownParker)
		     .adjust();
		drive.waitUntilDone();
		lookAndHook.waitUntilDone();
	}
	
	@Override
	protected void cleanup() {
		drive.stop();
		lookAndHook.stop();
	}
	
	protected void putDownParker() {
		robot.parker.setPosition(Constants.PARKER_POSITION_OUT);
	}
	
	private void loadLookAndHook() {
		//preload lookAndHook tasks.
		lookAndHook.add(startGoldLook::await);
		goldLook = lookAndHook.submit(new GoldLookDoubleCallable(hardwareMap));
		lookAndHook.then(() -> detectTime.set(System.nanoTime()))
		           .then(unHooked::await)
		           .then(this::retractHook)
		           .thenStop();
		
	}
	
	private void resetEncoders() {
		//Reset encoders.
		robot.collectArm.setMode(STOP_AND_RESET_ENCODER);
		robot.collectArm.setMode(RUN_WITHOUT_ENCODER);
		robot.scoreArm.setMode(STOP_AND_RESET_ENCODER);
		robot.scoreArm.setMode(RUN_WITHOUT_ENCODER);
	}
	
	private void retractHook() {
		robot.hook.setPower(-0.7);
		while (Math.abs(robot.hook.getCurrentPosition()/* - 0 */) > 100) {
			Thread.yield();
			if (sleep(20)) break;
		}
		robot.hook.setPower(0);
	}
	
	protected void unHook() throws InterruptedException {
		startGoldLook.signal();
		telemetry.addLine("GOLD LOOK STARTED");
		robot.hook.setPower(1);
		//decreasing
		waitUntil(() -> robot.hook.getCurrentPosition() <= Constants.HOOK_TURN_START_LOOK);
		telemetry.update();
		waitUntil(() -> robot.hook.getCurrentPosition() <= HOOK_TURN_END);
		robot.hook.setPower(0);
		drive.move(-6, 6, 10).phantomTurn(-5, DEGREES).go(true)
		     .then(unHooked::signal)
		     .goTurn(10, DEGREES, 10, true);
	}
	
	private void knockOffGold() throws InterruptedException {
		int look = 0;
		try {
			look = goldLook.get(4, SECONDS);
		} catch (ExecutionException e) {
			e.getCause().printStackTrace();
		} catch (TimeoutException ignored) {
			telemetry.addLine("FAIL-SAFE HAPPENED");
			detectTime.set(System.nanoTime());
			look = (int) (Math.random() * 3);
		}
		goldLook.cancel(true);
		telemetry.addData("Gold detected", "%2.2f seconds ago",
		                  detectTime.get() == 0 ? 0 :
		                  (System.nanoTime() - detectTime.get()) / 1e9);
		telemetry.addData("Gold is at:", (look == 0) ? "left" : ((look == 1) ?
		                                                         "middle" : "right"));
		telemetry.update();
		drive.turn(-5, DEGREES, 10).move(-16 + 18 * look, 16, 10).go()
		     .goMove(0, 4.5, 10, true)
		     .goMove(0, -7, 0.8, true)
		     .goMove(-18 - 18 * look, 0, 10);
	}
}
