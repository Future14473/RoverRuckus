package org.firstinspires.ftc.teamcode.RoverRuckus.util;

/**
 * A class that facilitates making ModifiedLinearOpMode
 */
public abstract class OurLinearOpMode extends ModifiedLinearOpMode {
	protected Robot robot;
	
	/**
	 * This method is loop once upon initialization.
	 * Put any additional initialization code here.
	 */
	protected abstract void initialize();
	
	/**
	 * This method is loop after the start button is pressed. OpMode will stop when this
	 * method returns or throws InterruptedException
	 *
	 * @throws InterruptedException to stop the OpMode early.
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
