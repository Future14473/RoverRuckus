package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MecanumDrive;

public abstract class DrivingLinearOpMode extends ModifiedLinearOpMode {
	protected Robot robot;
	protected MecanumDrive drive;
	
	/**
	 * This method is run once upon initialization.
	 * Put any additional initialization code here.
	 *
	 * @throws InterruptedException for when the OpMode stops early
	 */
	protected abstract void initialize() throws InterruptedException;
	
	/**
	 * This method is run after the start button is pressed. OpMode will stop when this
	 * method returns.
	 *
	 * @throws InterruptedException for when the OpMode stops early
	 */
	protected abstract void run() throws InterruptedException;
	
	/**
	 * This method is run during cleanup.
	 * Put additional cleanup code here.
	 */
	protected void finish() {}
	
	@Override
	protected final void runOpMode() throws InterruptedException {
		robot = new Robot(hardwareMap);
		drive = new MecanumDrive(robot.wheels);
		initialize();
		waitForStart();
		run();
	}
	
	@Override
	protected final void cleanup() {
		drive.stop();
		finish();
	}
}
