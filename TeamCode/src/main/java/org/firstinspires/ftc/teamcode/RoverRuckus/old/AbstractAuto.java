//package org.firstinspires.ftc.teamcode.RoverRuckus.real;
//
//import com.qualcomm.robotcore.util.ElapsedTime;
//import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDrive;
//import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLookDouble;
//import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;
//import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.CurRobot;
//
//import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
//import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
//import static java.util.concurrent.TimeUnit.SECONDS;
//
///**
// * For autonomous
// */
//public abstract class AbstractAuto extends OurLinearOpMode {
//	private static final int    HOOK_TURN_START_LOOK = -22000;
//	private static final int    HOOK_TURN_END        = -26000;
//	private static final double PARKER_POSITION_OUT  = 0;
//
//	private final GoldLookDouble goldLooker = new GoldLookDouble();
//	protected     MecanumDrive   drive;
//	private       CurRobot       robot      = new CurRobot(hardwareMap);
//	private       ElapsedTime    timer      = new ElapsedTime();
//
//	protected abstract void positionForDepot() throws InterruptedException;
//
//	protected abstract void parkInCrater();
//
//	@Override
//	protected void initialize() {
//		robot = new CurRobot(hardwareMap);
//		goldLooker.init(hardwareMap);
//		drive = new MecanumDrive(robot, new MecanumDrive.Parameters());
//		//Reset encoders.
//		robot.collectArm.setMode(STOP_AND_RESET_ENCODER);
//		robot.collectArm.setMode(RUN_WITHOUT_ENCODER);
//		robot.scoreArm.setMode(STOP_AND_RESET_ENCODER);
//		robot.scoreArm.setMode(RUN_WITHOUT_ENCODER);
//		robot.hook.setMode(STOP_AND_RESET_ENCODER);
//		robot.hook.setMode(RUN_WITHOUT_ENCODER);
//	}
//
//	@Override
//	public final void run() throws InterruptedException {
//		unHook();
//		knockOffGold();
//
//		positionForDepot();
//		drive.waitUntilDone();
//
//		robot.hook.setPower(0);
//		putMarkerInDepot();
//
//		robot.parker.setPosition(PARKER_POSITION_OUT);
//		parkInCrater();
//		finishHook();
//		drive.waitUntilDone();
//	}
//
//	@Override
//	protected void cleanup() {
//		goldLooker.stop();
//	}
//
//	private void finishHook() throws InterruptedException {
//		robot.hook.setPower(-0.7);
//		waitUntil(
//				() -> Math.abs(robot.hook.getCurrentPosition()/* - 0 */) < 100);
//		robot.hook.setPower(0);
//	}
//
//	private void unHook() throws InterruptedException {
//		robot.hook.setPower(1);
//		//decreasing
//		waitUntil(
//				() -> robot.hook.getCurrentPosition() <= HOOK_TURN_START_LOOK);
//		goldLooker.start();
//		telemetry.addLine("GOLD LOOK STARTED");
//		telemetry.update();
//		waitUntil(() -> robot.hook.getCurrentPosition() <= HOOK_TURN_END);
//		robot.hook.setPower(0);
//		drive.moveXY(-0.15, 0.05, 10);
//		drive.moveXY(0, 0.1, 10);
//		drive.rotate(-10, 10);
//	}
//
//	private void knockOffGold() throws InterruptedException {
//		drive.waitUntilDone();
//		robot.hook.setPower(-0.6); //HOOK INTERLUDE
//		timer.reset();
//		int look = goldLooker.getLook(3, SECONDS);
//		if (look == -1) {
//			look = 2;
//			telemetry.addLine("FAIL-SAFE HAPPENED");
//		} else look = (look + 2) % 3;
//		goldLooker.stop();
//		telemetry.addData("Gold is at:", (look == 0) ? "left" :
//		                                 ((look == 1) ? "middle" : "right"));
//		telemetry.update();
//		drive.waitUntilDone();
//		drive.moveXY(-0.35 + 0.5 * look, 0.45, 10); //go to
//		drive.moveXY(0, 0.25, 10); //knock off
//		drive.moveXY(0, -0.35, 10);
//		drive.moveXY(-0.6 - 0.5 * look, 0.05, 10); //repos
//		drive.waitUntilDone();
//	}
//
//	private void putMarkerInDepot() {
//		//deposit
//		robot.markerDoor.setPosition(0.9);
//		sleep(1000);
//		robot.flicker.setPosition(0);
//		sleep(500);
//	}
//}
