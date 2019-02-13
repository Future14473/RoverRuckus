package org.firstinspires.ftc.teamcode.RoverRuckus.util.robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static com.qualcomm.hardware.bosch.BNO055IMU.AngleUnit.RADIANS;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.ZYX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.INTRINSIC;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower.calcPower;

@SuppressWarnings("Duplicates")
public class SheetMetalRobot implements IRobot {
	//maximum acceleration in powerLevel/second
	private static final double MAX_ACCELERATION          = 2.0;
	private static final int    TARGET_POSITION_TOLERANCE = 25;
	
	public final MotorSet wheels;
	public final DcMotor  hook, scooper, collectArm, scoreArm;
	public final Servo flicker, markerDoor, collectDoor, scoreDump, parker;
	public final CRServo   angler;
	public final BNO055IMU imu;
	
	private final CumulativeDirection direction;
	private       long                pastTime;
	private       MotorSetPower       pastPower = MotorSetPower.ZERO;
	
	public SheetMetalRobot(HardwareMap hardwareMap) {
		
		DcMotorEx fl = hardwareMap.get(DcMotorEx.class, "FrontLeft");
		DcMotorEx fr = hardwareMap.get(DcMotorEx.class, "FrontRight");
		DcMotorEx bl = hardwareMap.get(DcMotorEx.class, "BackLeft");
		DcMotorEx br = hardwareMap.get(DcMotorEx.class, "BackRight");
		fl.setDirection(REVERSE);
		bl.setDirection(REVERSE);
		wheels = new MotorSet(fl, fr, bl, br);
		wheels.setZeroPowerBehavior(BRAKE);
		wheels.setTargetPositionTolerance(TARGET_POSITION_TOLERANCE);
		
		hook = hardwareMap.get(DcMotor.class, "Hook");
		hook.setMode(RUN_USING_ENCODER);
		hook.setZeroPowerBehavior(BRAKE);
		
		scooper = hardwareMap.get(DcMotor.class, "Scooper");
		scooper.setMode(RUN_WITHOUT_ENCODER);
		scooper.setZeroPowerBehavior(BRAKE);
		
		collectArm = hardwareMap.get(DcMotor.class, "CollectArm");
		collectArm.setMode(RUN_USING_ENCODER);
		collectArm.setZeroPowerBehavior(BRAKE);
		collectArm.setDirection(REVERSE);
		
		scoreArm = hardwareMap.get(DcMotor.class, "ScoreArm");
		scoreArm.setMode(RUN_USING_ENCODER);
		scoreArm.setZeroPowerBehavior(BRAKE);
		scoreArm.setDirection(REVERSE);
		
		flicker = hardwareMap.get(Servo.class, "Flicker");
		markerDoor = hardwareMap.get(Servo.class, "MarkerDoor");
		collectDoor = hardwareMap.get(Servo.class, "CollectDoor");
		scoreDump = hardwareMap.get(Servo.class, "ScoreDump");
		parker = hardwareMap.get(Servo.class, "Parker");
		
		angler = hardwareMap.get(CRServo.class, "Angler");
		
		imu = hardwareMap.get(BNO055IMU.class, "imu");
		pastTime = System.nanoTime();
		
		direction =
				new CumulativeDirection(() -> this.getOrientation().firstAngle);
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
		return Math.toDegrees(-direction.getAsDouble());
	}
}
