package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Robot {
	//Expansion Hub 2
	public DcMotor hooke, rotation, arm, collection;
	public Servo marker, flicker;
	public CRServo tape;
	public DistanceSensor SensorRange;
	public ElapsedTime period = new ElapsedTime();
	public DriveHandler drive;
	//Expansion Hub 1
	public DcMotor rightFront, rightBack, leftFront, leftBack;
	private HardwareMap hwMap;
	
	
	public Robot() {
	}
	
	public void init(HardwareMap ahwMap) {
		
		hwMap = ahwMap;
		
		leftFront = hwMap.get(DcMotor.class, "FrontLeft");
		rightFront = hwMap.get(DcMotor.class, "FrontRight");
		leftBack = hwMap.get(DcMotor.class, "BackLeft");
		rightBack = hwMap.get(DcMotor.class, "BackRight");
		drive = new DriveHandler(this);
		drive.setModeEncoder();
		
		hooke = hwMap.get(DcMotor.class, "Hooke");
		hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		hooke.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		arm = hwMap.get(DcMotor.class, "Arm");
		arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rotation = hwMap.get(DcMotor.class, "Rotation");
		rotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rotation.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		collection = hwMap.get(DcMotor.class, "Collection");
		collection.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		collection.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		
		marker = hwMap.get(Servo.class, "Marker");
		flicker = hwMap.get(Servo.class, "Flicker");
		tape = hwMap.get(CRServo.class, "Tape");
		tape.setPower(0.5);
		
		leftBack.setDirection(DcMotorSimple.Direction.REVERSE); // sets the power of left back wheel to reverse
		leftFront.setDirection(DcMotorSimple.Direction.REVERSE); // same for this one
		
		drive = new DriveHandler(this);
		
		drive.setModeEncoder();
		rotation.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		rotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	}
}
