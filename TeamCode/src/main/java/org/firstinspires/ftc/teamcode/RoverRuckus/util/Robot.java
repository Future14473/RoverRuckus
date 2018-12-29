package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Robot {
	//Expansion Hub 2
	public DcMotor hooke, rotation, arm, collection;
	public Servo marker, flicker;
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
		
		hooke = hwMap.get(DcMotor.class, "Hooke");
		rotation = hwMap.get(DcMotor.class, "Rotation");
		arm = hwMap.get(DcMotor.class, "Arm");
		
		collection = hwMap.get(DcMotor.class, "Collection");
		
		marker = hwMap.get(Servo.class, "Marker");
		flicker = hwMap.get(Servo.class, "Flicker");
		
		//SensorRange = hwMap.get(DistanceSensor.class, "SensorRange");
		
		leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
		leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
		//Hooke.setDirection(DcMotorSimple.Direction.REVERSE);
		
		drive = new DriveHandler(this);
		
		drive.setModeEncoder();
		rotation.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		rotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	}
}
