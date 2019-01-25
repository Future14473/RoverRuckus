package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class OldRobot {
	public DcMotor hooke, rotation, arm, collection;
	public Servo marker, flicker, opener;
	public OldDriveHandler drive;
	public DcMotor rightFront, rightBack, leftFront, leftBack;
	
	
	public OldRobot() {
	}
	
	public void init(HardwareMap hwMap) {
		
		leftFront = hwMap.get(DcMotor.class, "FrontLeft");
		rightFront = hwMap.get(DcMotor.class, "FrontRight");
		leftBack = hwMap.get(DcMotor.class, "BackLeft");
		rightBack = hwMap.get(DcMotor.class, "BackRight");
		
		leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
		leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
		
		drive = new OldDriveHandler(this);
		
		hooke = hwMap.get(DcMotor.class, "Hooke");
		hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		hooke.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		arm = hwMap.get(DcMotor.class, "Arm");
		arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rotation = hwMap.get(DcMotor.class, "Rotation");
		rotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
		rotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		rotation.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		collection = hwMap.get(DcMotor.class, "Collection");
		collection.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		collection.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		
		marker = hwMap.get(Servo.class, "Marker");
		flicker = hwMap.get(Servo.class, "Flicker");
		opener = hwMap.get(Servo.class, "Opener");
		/*tape = hwMap.get(CRServo.class, "Tape");
		tape.setPower(0.5);*/
	}
}
