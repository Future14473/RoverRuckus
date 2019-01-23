package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.*;

public class Robot2 {
	//Expansion Hub 2
	public final DcMotor hook, rotater, extender, scorer;
	public final Servo marker, flicker, door;
	public final CRServo collector;
	public final OldDriveHandlerImpl drive;
	//public CRServo tape;
	//Expansion Hub 1
	public DcMotor rightFront, rightBack, leftFront, leftBack;
	
	
	public Robot2(HardwareMap hwMap) {
		
		leftFront = hwMap.get(DcMotor.class, "FrontLeft");
		rightFront = hwMap.get(DcMotor.class, "FrontRight");
		leftBack = hwMap.get(DcMotor.class, "BackLeft");
		rightBack = hwMap.get(DcMotor.class, "BackRight");
		
		leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
		leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
		
		drive = new OldDriveHandlerImpl(leftFront, rightFront, leftBack, rightBack);
		drive.setModeEncoder();
		
		
		hook = hwMap.get(DcMotor.class, "Hook");
		hook.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		hook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		extender = hwMap.get(DcMotor.class, "Extender");
		extender.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		extender.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		rotater = hwMap.get(DcMotor.class, "Rotater");
		rotater.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rotater.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
		
		scorer = hwMap.get(DcMotor.class, "Scorer");
		scorer.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		scorer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		collector = hwMap.get(CRServo.class, "Collector");
		
		marker = hwMap.get(Servo.class, "Marker");
		flicker = hwMap.get(Servo.class, "Flicker");
		door = hwMap.get(Servo.class, "Door");
	}
}
