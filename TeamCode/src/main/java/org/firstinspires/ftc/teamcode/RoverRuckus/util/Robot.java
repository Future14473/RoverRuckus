package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MotorSet;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MotorPowerSet.calcPowerSet;

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
		
		bl.setDirection(REVERSE);
		fl.setDirection(REVERSE);
		
		wheels = new MotorSet(fl, fr, bl, br);
		wheels.setZeroPowerBehavior(BRAKE);
		
		hook = hwMap.get(DcMotor.class, "Hook");
		hook.setMode(RUN_USING_ENCODER);
		hook.setZeroPowerBehavior(BRAKE);
		
		extender = hwMap.get(DcMotor.class, "Extender");
		extender.setMode(RUN_USING_ENCODER);
		extender.setZeroPowerBehavior(BRAKE);
		
		rotater = hwMap.get(DcMotor.class, "Rotater");
		rotater.setMode(RUN_USING_ENCODER);
		rotater.setZeroPowerBehavior(FLOAT);
		
		scorer = hwMap.get(DcMotor.class, "Scorer");
		scorer.setMode(RUN_USING_ENCODER);
		scorer.setZeroPowerBehavior(BRAKE);
		
		collector = hwMap.get(CRServo.class, "Collector");
		
		marker = hwMap.get(Servo.class, "Marker");
		flicker = hwMap.get(Servo.class, "Flicker");
		door = hwMap.get(Servo.class, "Door");
	}
	
	/**
	 * Utility: set the motors right now to move in the specified direction, turnRate, and speed.
	 */
	public void moveAt(double direction, double turnRate, double speed) {
		wheels.setPower(calcPowerSet(direction, turnRate, speed));
	}
	
}
