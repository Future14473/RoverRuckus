package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Robot {
	//Expansion Hub 2
	public DcMotor Hooke, Arm, Rotation, Collection;
	public Servo Marker, Flicker;
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
		
		
		Hooke = hwMap.get(DcMotor.class, "Hooke");
		Hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		Hooke.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		Arm = hwMap.get(DcMotor.class, "Arm");
		Arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		Arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		Rotation = hwMap.get(DcMotor.class, "Rotation");
		Rotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		Rotation.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		Collection = hwMap.get(DcMotor.class, "Collection");
		Collection.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		Collection.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		
		Marker = hwMap.get(Servo.class, "Marker");
		Flicker = hwMap.get(Servo.class, "Flicker");
		
		//SensorRange = hwMap.get(DistanceSensor.class, "SensorRange");
		
		leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
		leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
		//Hooke.setDirection(DcMotorSimple.Direction.REVERSE);
		
	}
}
