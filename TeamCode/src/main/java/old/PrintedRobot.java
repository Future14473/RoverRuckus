//package org.firstinspires.ftc.teamcode.lib.robot;
//
//import com.qualcomm.hardware.bosch.BNO055IMU;
//import com.qualcomm.robotcore.hardware.CRServo;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
//
//import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
//import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
//import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
//import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
//
//public class PrintedRobot extends BaseRobot { //extend to keep functionality and not
//	// destroy the world with errors.
//
//	public final DcMotor hook, scooper, collectArm, scoreArm;
//	public final Servo flicker, markerDoor, collectDoor, scoreDoor, parker;
//	public final CRServo       angler;
//	public final BNO055IMU     imu;
//	private      long          pastTime;
//	private      MotorSetPower pastPower = MotorSetPower.ZERO;
//
//	public PrintedRobot(HardwareMap hardwareMap) {
//		super(hardwareMap);
//		pastTime = System.nanoTime();
//
//		hook = hardwareMap.get(DcMotor.class, "Hook");
//		hook.setMode(RUN_USING_ENCODER);
//		hook.setZeroPowerBehavior(BRAKE);
//
//		scooper = hardwareMap.get(DcMotor.class, "Scooper");
//		scooper.setMode(RUN_WITHOUT_ENCODER);
//		scooper.setZeroPowerBehavior(BRAKE);
//
//		collectArm = hardwareMap.get(DcMotor.class, "CollectArm");
//		collectArm.setMode(RUN_USING_ENCODER);
//		collectArm.setZeroPowerBehavior(BRAKE);
//		collectArm.setDirection(REVERSE);
//
//		scoreArm = hardwareMap.get(DcMotor.class, "ScoreArm");
//		scoreArm.setMode(RUN_USING_ENCODER);
//		scoreArm.setZeroPowerBehavior(BRAKE);
//		scoreArm.setDirection(REVERSE);
//
//		flicker = hardwareMap.get(Servo.class, "Flicker");
//		markerDoor = hardwareMap.get(Servo.class, "MarkerDoor");
//		collectDoor = hardwareMap.get(Servo.class, "CollectDoor");
//		scoreDoor = hardwareMap.get(Servo.class, "ScoreDump");
//		parker = hardwareMap.get(Servo.class, "Parker");
//
//		angler = hardwareMap.get(CRServo.class, "Angler");
//
//		imu = hardwareMap.get(BNO055IMU.class, "imu");
//	}
//
////	/**
////	 * Utility: set the motors right now to move in the specified direction,
////	 * turnRate, and speed.
////	 */
////	public void moveAt(double direction, double moveSpeed, double turnRate) {
////		pastPower = MotorSetPower.fromPolarNonStandard(direction, moveSpeed, turnRate);
////		wheels.setPower(pastPower);
////	}
//
//	public void smoothMoveAt(double direction, double speed, double turnRate) {
//		long curTime = System.nanoTime();
//		MotorSetPower actualPower = MotorSetPower.fromPolarNonStandard(direction, speed, turnRate)
//		                                         .rampFrom(pastPower,
//		                                                   RAMP_RATE / 1e9 * (curTime - pastTime));
//		pastTime = curTime;
//		pastPower = actualPower;
//		wheels.setPower(actualPower);
//	}
//}
