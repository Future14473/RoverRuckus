package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDrive;

/**
 * A OpMode that includes a {@link MecanumDrive} and handling around it.
 */
public abstract class DecoratedOpMode extends ModifiedLinearOpMode {
	protected Robot robot;
	
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
	
	@Override
	protected final void runOpMode() throws InterruptedException {
		telemetry.addLine("Init started...");
		telemetry.addLine("Please wait before pressing start");
		telemetry.update();
		robot = new Robot(hardwareMap);
		initialize();
		telemetry.addLine("Init done");
		telemetry.addLine("You may now press start");
		telemetry.update();
		waitForStart();
		run();
	}
}
