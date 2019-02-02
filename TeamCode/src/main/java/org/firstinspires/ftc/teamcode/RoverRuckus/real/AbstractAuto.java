package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDouble;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MecanumDrive;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

/**
 * For autonomous
 */
@SuppressWarnings("Duplicates")
public abstract class AbstractAuto extends OurLinearOpMode {
	private static final int HOOK_TURN_START_LOOK = -24000;
	private static final int HOOK_TURN_END = -26000;
	private static final int ARM_TURN = -3800;
	protected MecanumDrive drive;
	private ElapsedTime timer = new ElapsedTime();
	private GoldLookDouble goldLooker = new GoldLookDouble();
	
	protected abstract void positionForDepot() throws InterruptedException;
	
	@Override
	protected void initialize() {
		goldLooker.init(hardwareMap);
		drive = new MecanumDrive(robot.wheels);
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
		drive.waitUntilDone();
		
		checkHook();
		putMarkerInDepot();
		checkHook();
		afterDepot();
		checkHook();
		parkInCrater();
		
		waitUntil(() -> Math.abs(robot.hook.getCurrentPosition()/* - 0 */) > 40);
		robot.hook.setPower(0);
		
		drive.waitUntilDone();
	}
	
	private void checkHook() {
		if (Math.abs(robot.hook.getCurrentPosition()/* - 0 */) > 40) robot.hook.setPower(0);
	}
	
	@Override
	protected void cleanup() {
		drive.stop(); //IMPORTANT
		goldLooker.stop();
	}
	
	private void unHook() throws InterruptedException {
		robot.hook.setPower(1);
		//decreasing
		waitUntil(() -> robot.hook.getCurrentPosition() <= HOOK_TURN_START_LOOK);
		goldLooker.start();
		telemetry.addLine("GOLD LOOK STARTED");
		telemetry.update();
		waitUntil(() -> robot.hook.getCurrentPosition() <= HOOK_TURN_END);
		robot.hook.setPower(0);
		drive.moveXY(-0.15, 0.05, 10);
		drive.moveXY(0, 0.1, 10);
		drive.turn(-10, 10);
	}
	
	private void knockOffGold() throws InterruptedException {
		drive.waitUntilDone();
		timer.reset();
		waitUntil(() -> goldLooker.hasDetected() || timer.seconds() > 3);
		int look = goldLooker.getLook();
		if (look == -1) {
			look = 2;
			telemetry.addLine("FAIL-SAFE HAPPENED");
		} else look = (look + 2) % 3;
		goldLooker.stop();
		telemetry.addData("Gold is at:", (look == 0) ? "left" : ((look == 1) ? "middle" : "right"));
		telemetry.update();
		drive.waitUntilDone();
		robot.hook.setPower(-0.7); //INTERLUDE
		drive.moveXY(-0.35 + 0.5 * look, 0.37, 10);
		drive.moveXY(0, 0.15, 10);
		drive.moveXY(0, -0.15, 10);
		drive.moveXY(-0.6 - 0.5 * look, 0.05, 10);
		drive.waitUntilDone();
	}
	
	private void putMarkerInDepot() throws InterruptedException {
		//deposit
		robot.markerDoor.setPosition(0.9);
		sleep(800);
		robot.flicker.setPosition(0);
		sleep(500);
	}
	
	protected void afterDepot() throws InterruptedException {}
	
	private void parkInCrater() throws InterruptedException {
		drive.moveXY(-0.12, 1.5, 10);
		drive.waitUntilDone();
		drive.moveXY(-0.03, 0.5, 10);
		robot.collectArm.setPower(1);
		robot.scooper.setPower(1);
		waitUntil(() -> robot.collectArm.getCurrentPosition() < ARM_TURN);
		sleep(3000);
		robot.scooper.setPower(0);
	}
}
