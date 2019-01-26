package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.DrivingOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDouble;

public abstract class AbstractAuto extends DrivingOpMode {
	private static final int HOOK_TURN = -25700;
	private ElapsedTime timer = new ElapsedTime();
	private GoldLookDouble goldLooker = new GoldLookDouble();
	private int look = -1;
	
	protected abstract void positionForDepot() throws InterruptedException;
	
	@Override
	protected void initialize() {
		
		goldLooker.init(hardwareMap);
		
	}
	
	@Override
	public final void run() throws InterruptedException {
		unHook();
		knockOffGold();
		
		positionForDepot();
		drive.waitUntilDone();
		putMarkerInDepot();
		
		extra();
		
		parkInCrater();
	}
	
	@Override
	protected void finish() {
		goldLooker.stop();
	}
	
	private void unHook() throws InterruptedException {
		robot.hook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.hook.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		robot.hook.setPower(1);
		//decreasing
		waitUntil(() -> Math.abs(robot.hook.getCurrentPosition() - HOOK_TURN) > 50);
		robot.hook.setPower(0);
		drive.moveXY(-0.15, 0.1, 10);
		drive.moveXY(0, 0.1, 10);
		drive.turn(-10, 10);
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
		robot.hook.setPower(-1); //INTERLUDE
		drive.moveXY(-0.35 + 0.5 * look, 0.4, 10);
		drive.moveXY(0, 0.25, 10);
		drive.moveXY(0, -0.25, 10);
		drive.moveXY(-0.5 * look - 0.5, 0, 10);
		drive.waitUntilDone();
	}
	
	private void putMarkerInDepot() throws InterruptedException {
		//deposit
		robot.marker.setPosition(0.9);
		sleep(1000);
		robot.flicker.setPosition(0.65);
		sleep(1000);
	}
	
	public void extra() throws InterruptedException {}
	
	private void parkInCrater() throws InterruptedException {
		drive.moveXY(0, 1.7, 10);
		robot.rotater.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.rotater.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		robot.rotater.setPower(0.6);
		sleep(1000);
		robot.rotater.setPower(0);
		robot.extender.setPower(-1);
		sleep(1000);
		robot.extender.setPower(0);
		drive.waitUntilDone();
		waitUntil(() -> Math.abs(robot.hook.getCurrentPosition()/* - 0 */) > 20);
		robot.hook.setPower(0);
	}
}
