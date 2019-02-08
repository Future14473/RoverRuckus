package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.mecanumdrive.MecanumDrive;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDouble;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

/**
 * For autonomous
 */
@SuppressWarnings("Duplicates")
public abstract class AbstractAuto extends OurLinearOpMode {
	private static final int HOOK_TURN_START_LOOK = -22000;
	private static final int HOOK_TURN_END = -26000;
	private static final double PARKER_POSITION_OUT = 0;
	protected MecanumDrive drive;
	private ElapsedTime timer = new ElapsedTime();
	private GoldLookDouble goldLooker = new GoldLookDouble();
	
	protected abstract void positionForDepot() throws InterruptedException;
	
	protected abstract void parkInCrater();
	
	@Override
	protected void initialize() {
		goldLooker.init(hardwareMap);
		drive = new MecanumDrive(robot, new MecanumDrive.Parameters());
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
		
		robot.hook.setPower(0);
		putMarkerInDepot();
		robot.parker.setPosition(PARKER_POSITION_OUT);
		parkInCrater();
		finishHook();
		drive.waitUntilDone();
	}
	
	private void finishHook() throws InterruptedException {
		robot.hook.setPower(-0.7);
		waitUntil(() -> Math.abs(robot.hook.getCurrentPosition()/* - 0 */) < 100);
		robot.hook.setPower(0);
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
		robot.hook.setPower(-0.6); //HOOK INTERLUDE
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
