package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MotorSet;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MotorSetPower.calcPower;

public class Robot {
	public final MotorSet wheels;
	public final DcMotor hook, scooper, collectArm, scoreArm;
	public final Servo marker, flicker, collectDoor, scoreDoor;
	public final CRServo angler;
	public final BNO055IMU imu;
	
	public Robot(HardwareMap hardwareMap) {
		
		DcMotor fl = hardwareMap.get(DcMotor.class, "FrontLeft");
		DcMotor fr = hardwareMap.get(DcMotor.class, "FrontRight");
		DcMotor bl = hardwareMap.get(DcMotor.class, "BackLeft");
		DcMotor br = hardwareMap.get(DcMotor.class, "BackRight");
		bl.setDirection(REVERSE);
		fl.setDirection(REVERSE);
		wheels = new MotorSet(fl, fr, bl, br);
		wheels.setZeroPowerBehavior(BRAKE);
		
		hook = hardwareMap.get(DcMotor.class, "Hook");
		hook.setMode(RUN_USING_ENCODER);
		hook.setZeroPowerBehavior(BRAKE);
		
		scooper = hardwareMap.get(DcMotor.class, "Scooper");
		scooper.setMode(RUN_USING_ENCODER);
		scooper.setZeroPowerBehavior(FLOAT);
		
		collectArm = hardwareMap.get(DcMotor.class, "CollectArm");
		collectArm.setMode(RUN_USING_ENCODER);
		collectArm.setZeroPowerBehavior(BRAKE);
		
		scoreArm = hardwareMap.get(DcMotor.class, "ScoreArm");
		scoreArm.setMode(RUN_USING_ENCODER);
		scoreArm.setZeroPowerBehavior(BRAKE);
		
		marker = hardwareMap.get(Servo.class, "Marker");
		flicker = hardwareMap.get(Servo.class, "Flicker");
		collectDoor = hardwareMap.get(Servo.class, "CollectDoor");
		scoreDoor = hardwareMap.get(Servo.class, "ScoreDoor");
		
		angler = hardwareMap.get(CRServo.class, "Angler");
		
		imu = hardwareMap.get(BNO055IMU.class, "imu");
	}
	
	/**
	 * Utility: set the motors right now to move in the specified direction, turnRate, and speed.
	 */
	public void moveAt(double direction, double turnRate, double speed) {
		wheels.setPower(calcPower(direction, turnRate, speed));
	}
	
}
