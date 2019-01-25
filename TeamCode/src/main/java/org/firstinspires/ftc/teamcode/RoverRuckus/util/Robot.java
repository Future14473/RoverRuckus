package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MotorSet;

public class Robot {
	public final DcMotor hook, rotater, extender, scorer;
	public final Servo marker, flicker, door;
	public final CRServo collector;
	public MotorSet wheels;
	
	public Robot(HardwareMap hwMap) {
		
		DcMotor fl = hwMap.get(DcMotor.class, "FrontLeft");
		DcMotor fr = hwMap.get(DcMotor.class, "FrontRight");
		DcMotor bl = hwMap.get(DcMotor.class, "BackLeft");
		DcMotor br = hwMap.get(DcMotor.class, "BackRight");
		
		bl.setDirection(DcMotorSimple.Direction.REVERSE);
		fl.setDirection(DcMotorSimple.Direction.REVERSE);
		
		wheels = new MotorSet(fl, fr, bl, br);
		
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
