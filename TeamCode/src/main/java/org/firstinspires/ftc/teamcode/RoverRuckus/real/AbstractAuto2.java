package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDrive;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.GateTask;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.TaskProgram;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDoubleCallable;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.SheetMetalRobot;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * For autonomous
 */
@SuppressWarnings("Duplicates")
public abstract class AbstractAuto2 extends OurLinearOpMode {
	private static final int HOOK_TURN_START_LOOK = -22000;
	private static final int HOOK_TURN_END = -26000;
	private static final double PARKER_POSITION_OUT = 0;
	private SheetMetalRobot robot = new SheetMetalRobot(hardwareMap);
	private ElapsedTime timer = new ElapsedTime();
	
	protected MecanumDrive drive;
	private TaskProgram lookAndHook;
	private Future<Integer> goldLook;
	private GateTask hookDownGate = new GateTask();
	
	protected abstract void positionForDepot() throws InterruptedException;
	
	protected abstract void parkInCrater();
	
	@Override
	protected void initialize() {
		robot = new SheetMetalRobot(hardwareMap);
		lookAndHook = new TaskProgram(false);
		drive = new MecanumDrive(robot, new MecanumDrive.Parameters());
		resetEncoders();
		loadLookAndHook();
	}
	
	private void loadLookAndHook() {
		//preload lookAndHook tasks.
		goldLook = lookAndHook.call(new GoldLookDoubleCallable(hardwareMap));
		lookAndHook.add(hookDownGate);
		lookAndHook.add(this::finishHook);
		lookAndHook.thenStop();
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
	
	@Override
	public final void run() throws InterruptedException {
		unHook();
		knockOffGold();
		
		positionForDepot();
		
		putMarkerInDepot();
		
		robot.parker.setPosition(PARKER_POSITION_OUT);
		parkInCrater();
		drive.waitUntilDone();
		lookAndHook.waitUntilDone();
	}
	
	@Override
	protected void cleanup() {
		drive.close();
		lookAndHook.close();
	}
	
	private void finishHook() {
		robot.hook.setPower(-0.7);
		while (Math.abs(robot.hook.getCurrentPosition()/* - 0 */) < 100) {
			Thread.yield();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				return;
			}
		}
		robot.hook.setPower(0);
	}
	
	private void unHook() throws InterruptedException {
		robot.hook.setPower(1);
		//decreasing
		waitUntil(() -> robot.hook.getCurrentPosition() <= HOOK_TURN_START_LOOK);
		lookAndHook.start();
		telemetry.addLine("GOLD LOOK STARTED");
		telemetry.update();
		waitUntil(() -> robot.hook.getCurrentPosition() <= HOOK_TURN_END);
		robot.hook.setPower(0);
		drive.moveXY(-0.15, 0.05, 10);
		drive.moveXY(0, 0.1, 10);
		drive.add(() -> { //notify other guy
			robot.hook.setPower(-0.6);
			hookDownGate.openGate();
		});
		drive.rotate(-10, 10);
	}
	
	private void knockOffGold() throws InterruptedException {
		timer.reset();
		int look = 0;
		try {
			look = goldLook.get(4, SECONDS);
		} catch (ExecutionException e) {
			e.getCause().printStackTrace();
		} catch (TimeoutException e) {
			telemetry.addLine("FAIL-SAFE HAPPENED");
			look = 2;
		}
		if (look != -1) {look = (look + 2) % 3;}
		goldLook.cancel(true);
		telemetry.addData("Gold is at:", (look == 0) ? "left" : ((look == 1) ?
				"middle" : "right"));
		telemetry.update();
		drive.waitUntilDone();
		drive.moveXY(-0.35 + 0.5 * look, 0.45, 10); //go to
		drive.moveXY(0, 0.25, 10); //knock off
		drive.moveXY(0, -0.35, 10);
		drive.moveXY(-0.6 - 0.5 * look, 0.05, 10); //repos
		drive.waitUntilDone();
	}
	
	private void putMarkerInDepot() throws InterruptedException {
		//deposit
		robot.markerDoor.setPosition(0.9);
		sleep(1000);
		robot.flicker.setPosition(0);
		sleep(500);
	}
}
