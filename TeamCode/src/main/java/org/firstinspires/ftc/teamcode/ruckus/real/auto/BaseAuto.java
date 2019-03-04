package org.firstinspires.ftc.teamcode.ruckus.real.auto;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.teamcode.lib.navigation.MecanumDrive;
import org.firstinspires.ftc.teamcode.lib.opmode.LimitedMotor;
import org.firstinspires.ftc.teamcode.lib.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.lib.opmode.SimpleCondition;
import org.firstinspires.ftc.teamcode.lib.robot.CurRobot;
import org.firstinspires.ftc.teamcode.lib.tasks.TaskProgram;
import org.firstinspires.ftc.teamcode.ruckus.goldlook.GoldLookDoubleCallable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.teamcode.config.TeleopAndAutoConstants.*;

/**
 * Base autonomous.
 */
public abstract class BaseAuto extends OurLinearOpMode {
	/** for looks */
	private final AtomicLong      detectTime          = new AtomicLong();
	protected     CurRobot        robot;
	/** Moves bot, does most of the auto */
	protected     MecanumDrive    driveAndStuff;
	/** Does look and hook retraction */
	protected     TaskProgram     lookAndHook;
	protected     SimpleCondition startInitialCollect = new SimpleCondition();
	private       LimitedMotor    collectArm;
	private       Future<Integer> goldLook;
	private       SimpleCondition startGoldLook       = new SimpleCondition();
	private       SimpleCondition unHooked            = new SimpleCondition();
	
	protected abstract void putMarkerInDepot();
	
	protected abstract void prepareInitialCollect();
	
	protected void flickMarkerOut() {
		robot.wheels.stop();
		robot.flicker.setPosition(FLICKER_OUT);
		sleep(400);
	}
	
	protected void openMarkerDoor() {
		robot.markerDoor.setPosition(MARKER_DOOR_OPEN);
	}
	
	@Override
	protected void initialize() throws InterruptedException {
		robot = new CurRobot(hardwareMap);
		robot.initIMU();
		collectArm = new LimitedMotor(robot.collectArm, MOTOR_MIN, COLLECT_ARM_MAX * 5 / 6,
		                              true);
		lookAndHook = new TaskProgram();
		driveAndStuff = new MecanumDrive(robot, new MecanumDrive.Parameters());
		loadLookAndHook();
		resetEncoders();
		waitUntil(robot::imuIsGyroCalibrated, 5, SECONDS);
	}
	
	@Override
	protected void cleanup() {
		driveAndStuff.stop();
		lookAndHook.stop();
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
			if (sleep(40)) break;
		}
		robot.hook.setPower(0);
	}
	
	protected void unHook() throws InterruptedException {
		startGoldLook.signal();
		robot.hook.setPower(1);
		telemetry.addLine("GOLD LOOK STARTED");
		telemetry.update();
		waitUntil(() -> robot.hook.getCurrentPosition() <= HOOK_TURN_END);
		robot.hook.setPower(0);
		//position for things.
		driveAndStuff.move(-6, 6, 10).phantomTurn(-5, DEGREES).go(true)
		             .then(unHooked::signal)
		             .thenTurn(10, DEGREES, 10, true);
	}
	
	protected void extendArm() {
		try {
			while (collectArm.getLastState() != LimitedMotor.State.UPPER) {
				collectArm.setPowerLimited(1);
				sleep(30);
			}
		} finally {
			collectArm.setPowerLimited(0);
		}
	}
	
	protected int getGoldLook() throws InterruptedException {
		int look = 0;
		try {
			look = goldLook.get(5, SECONDS);
		} catch (ExecutionException e) {
			e.getCause().printStackTrace();
			RobotLog.setGlobalWarningMsg(
					new RobotCoreException("Error in getting gold looking", e.getCause()),
					"Error in getting gold look");
		} catch (TimeoutException e) {
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
		return look;
	}
	
}
