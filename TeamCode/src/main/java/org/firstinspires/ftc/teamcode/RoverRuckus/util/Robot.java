package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MotorSet;

import static com.qualcomm.hardware.bosch.BNO055IMU.AngleUnit.DEGREES;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.RADIANS;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.ZYX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.INTRINSIC;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MotorSetPower.calcPower;

public class Robot {
	public final MotorSet wheels;
	public final DcMotor hook, scooper, collectArm, scoreArm;
	public final Servo flicker, markerDoor, collectDoor, scoreDoor;
	public final CRServo angler;
	public final BNO055IMU imu;
	
	public Robot(HardwareMap hardwareMap) {
		
		DcMotorEx fl = hardwareMap.get(DcMotorEx.class, "FrontLeft");
		DcMotorEx fr = hardwareMap.get(DcMotorEx.class, "FrontRight");
		DcMotorEx bl = hardwareMap.get(DcMotorEx.class, "BackLeft");
		DcMotorEx br = hardwareMap.get(DcMotorEx.class, "BackRight");
		bl.setDirection(REVERSE);
		fl.setDirection(REVERSE);
		wheels = new MotorSet(fl, fr, bl, br);
		wheels.setZeroPowerBehavior(BRAKE);
		wheels.setTargetPositionTolerance(30);
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
		scoreDoor = hardwareMap.get(Servo.class, "ScoreDoor");
		
		angler = hardwareMap.get(CRServo.class, "Angler");
		
		imu = hardwareMap.get(BNO055IMU.class, "imu");
	}
	
	/**
	 * Utility: set the motors right now to move in the specified direction, turnRate, and speed.
	 */
	public void moveAt(double direction, double moveSpeed, double turnRate) {
		wheels.setPower(calcPower(direction, moveSpeed, turnRate));
	}
	
	/**
	 * Initializes the robot's imu. This if for consistent parameters in the
	 * IMU throughout our code.
	 *
	 * @implNote this does not wait for gyro calibration.
	 */
	public void initIMU() {
		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
		parameters.angleUnit = DEGREES;
		imu.initialize(parameters);
	}
	
	/**
	 * Gets the orientation of hte robot as read from the IMU.
	 * Used for consistent parameters in our code.
	 */
	public Orientation getOrientation() {
		return imu.getAngularOrientation(INTRINSIC, ZYX, RADIANS);
	}
	
	/**
	 * Gets the orientation of the robot, on a plane parallel to the floor.
	 * In RADIANS.
	 * Positive is counterclockwise. this is not the same as in
	 * {@link org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MecanumDrive MecanumDrive}!!
	 */
	public float getDirection() {
		return getOrientation().firstAngle;
	}
}
