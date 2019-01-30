package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDouble;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MecanumDrive;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

/**
 * For autonomous
 */
public abstract class AbstractAuto extends OurLinearOpMode {
	private static final int HOOK_TURN = -26000;
	protected MecanumDrive drive;
	private ElapsedTime timer = new ElapsedTime();
	private GoldLookDouble goldLooker = new GoldLookDouble();
	private int look = -1;
	
	protected abstract void positionForDepot() throws InterruptedException;
	
	@Override
	protected void initialize() throws InterruptedException {
		goldLooker.init(hardwareMap);
		drive = new MecanumDrive(robot.wheels);
		robot.collectArm.setMode(STOP_AND_RESET_ENCODER);
		robot.collectArm.setMode(RUN_USING_ENCODER);
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
		drive.stop();
		goldLooker.stop();
		robot.imu.close();
	}
	
	private void unHook() throws InterruptedException {
		robot.hook.setPower(1);
		//decreasing
		waitUntil(() -> {
			telemetry.addData("HOOK POS", robot.hook.getCurrentPosition());
			telemetry.update();
			return Math.abs(robot.hook.getCurrentPosition() - HOOK_TURN) < 80;
		});
		robot.hook.setPower(0);
		drive.moveXY(-0.15, 0.1, 10);
		drive.moveXY(0, 0.1, 10);
		drive.turn(-15, 10);
	}
	
	protected void knockOffGold() throws InterruptedException {
		goldLooker.start();
		timer.reset();
		while (look == -1 && timer.milliseconds() < 5000 && opModeIsActive()) look = goldLooker.look();
		if (look == -1) {
			look = 2;
			telemetry.addLine("FAIL-SAFE HAPPENED");
		} else look = (look + 2) % 3;
		goldLooker.pause();
		telemetry.addData("Gold is at:", (look == 0) ? "left" : ((look == 1) ? "middle" : "right"));
		telemetry.update();
		drive.waitUntilDone();
		robot.hook.setPower(-0.7); //INTERLUDE
		drive.moveXY(-0.35 + 0.5 * look, 0.35, 10);
		drive.moveXY(0, 0.15, 10);
		drive.moveXY(0, -0.15, 10);
		drive.moveXY(-0.5 * look - 0.6, 0.05, 10);
		drive.waitUntilDone();
	}
	
	private void putMarkerInDepot() throws InterruptedException {
		//deposit
		robot.markerDoor.setPosition(0.9);
		sleep(1000);
		robot.flicker.setPosition(0);
		sleep(1000);
	}
	
	protected void afterDepot() throws InterruptedException {}
	
	private void parkInCrater() throws InterruptedException {
		drive.moveXY(-0.15, 1.7, 10);
	}
}
