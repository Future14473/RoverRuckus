package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.function.Supplier;

/**
 * A class that facilitates making ModifiedLinearOpMode
 */
public abstract class DecoratedLinearOpMode extends ModifiedLinearOpMode {
	protected Robot robot;
	
	/**
	 * This method is run once upon initialization.
	 * Put any additional initialization code here.
	 *
	 * @throws InterruptedException for when the OpMode early while initializing.
	 */
	protected abstract void initialize() throws InterruptedException;
	
	/**
	 * This method is run after the start button is pressed. OpMode will stop when this
	 * method returns or throws InterruptedException
	 *
	 * @throws InterruptedException to stop the OpMode early.
	 */
	protected abstract void run() throws InterruptedException;
	
	/**
	 * Initializes the robot's imu and returns an {@link Supplier} of {@link Orientation}s that gives the robot's
	 * orientation at any given time. <br>
	 * DEGREES ARE MEASURED IN "STANDARD" ORIENTATION -- CounterClockwise is Positive. This is NOT the same
	 * as in {@link org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MecanumDrive MecanumDrive}. <br>
	 *
	 * @implNote this does not wait for gyro calibration.
	 */
	protected Supplier<Orientation> getOrientationSupplier() {
		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
		parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
		robot.imu.initialize(parameters);
		return () -> robot.imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
	}
	
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
