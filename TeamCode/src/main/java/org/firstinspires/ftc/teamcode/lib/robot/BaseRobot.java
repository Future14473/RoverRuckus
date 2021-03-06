package org.firstinspires.ftc.teamcode.lib.robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.config.HardwareConstants;
import org.firstinspires.ftc.teamcode.lib.util.CumulativeDirection;

import static com.qualcomm.hardware.bosch.BNO055IMU.AngleUnit.RADIANS;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.ZYX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.INTRINSIC;

public abstract class BaseRobot implements IRobot {
	public final    MotorSet            wheels;
	protected final CumulativeDirection direction;
	private final   BNO055IMU           imu;
	
	public BaseRobot(HardwareMap hardwareMap) {
		imu = hardwareMap.get(BNO055IMU.class, "imu");
		DcMotorEx fl = hardwareMap.get(DcMotorEx.class, "FrontLeft");
		DcMotorEx fr = hardwareMap.get(DcMotorEx.class, "FrontRight");
		DcMotorEx bl = hardwareMap.get(DcMotorEx.class, "BackLeft");
		DcMotorEx br = hardwareMap.get(DcMotorEx.class, "BackRight");
		fl.setDirection(REVERSE);
		bl.setDirection(REVERSE);
		wheels = new MotorSet(fl, fr, bl, br);
		wheels.setZeroPowerBehavior(BRAKE);
		wheels.setPIDFCoefficients(RUN_USING_ENCODER, HardwareConstants.RUN_USING_ENCODER_PIDF);
		direction = new CumulativeDirection(() -> this.getOrientation().firstAngle);
	}
	
	/**
	 * Initializes the robot's imu. This is for consistent parameters in the
	 * IMU throughout our code.
	 *
	 * @implNote this does not wait for gyro calibration.
	 */
	public void initIMU() {
		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
		parameters.angleUnit = RADIANS;
		imu.initialize(parameters);
	}
	
	/**
	 * Gets the orientation of hte robot as read from the IMU.
	 * Used for consistent parameters in our code.
	 * Orientation is in INTRINSIC, ZYX, RADIANS. First Angle is plane parallel
	 * to floor.
	 */
	private Orientation getOrientation() {
		return imu.getAngularOrientation(INTRINSIC, ZYX, AngleUnit.RADIANS);
	}
	
	@Override
	public MotorSet getWheels() {
		return wheels;
	}
	
	@Override
	public double getAngle() {
		return direction.getAsDouble();
	}
	
	public boolean imuIsGyroCalibrated() {
		return imu.isGyroCalibrated();
	}
}
