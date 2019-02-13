package org.firstinspires.ftc.teamcode.RoverRuckus.util.robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static com.qualcomm.hardware.bosch.BNO055IMU.AngleUnit.RADIANS;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.ZYX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.INTRINSIC;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower.calcPower;

public abstract class BaseRobot implements Robot {
	private static final int    TARGET_POSITION_TOLERANCE = 25;
	//maximum acceleration in powerLevel/second
	private static final double MAX_ACCELERATION          = 1.5;
	
	public final    MotorSet            wheels;
	protected final CumulativeDirection direction;
	private final   BNO055IMU           imu;
	private         long                pastTime;
	private         MotorSetPower       pastPower = MotorSetPower.ZERO;
	
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
		wheels.setTargetPositionTolerance(TARGET_POSITION_TOLERANCE);
		direction =
				new CumulativeDirection(() -> this.getOrientation().firstAngle);
		pastTime = System.nanoTime();
	}
	
	/**
	 * Utility: set the motors right now to move in the specified direction,
	 * turnRate, and speed.
	 */
	@SuppressWarnings("unused")
	public void moveAt(double direction, double moveSpeed, double turnRate) {
		pastPower = calcPower(direction, moveSpeed, turnRate);
		wheels.setPower(pastPower);
	}
	
	public void smoothMoveAt(double direction, double speed, double turnRate) {
		long curTime = System.nanoTime();
		MotorSetPower actualPower =
				calcPower(direction, speed, turnRate).rampFrom(pastPower,
				                                               MAX_ACCELERATION /
				                                               1e9 * (curTime -
				                                                      pastTime));
		pastTime = curTime;
		pastPower = actualPower;
		wheels.setPower(actualPower);
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
		return -direction.getAsDouble();
	}
}
