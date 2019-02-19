package org.firstinspires.ftc.teamcode.RoverRuckus.util.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

public class CurRobot extends BaseRobot {
	
	public DcMotor hook, scooper, collectArm, scoreArm;
	public Servo flicker, markerDoor, collectDoor, scoreDump, parker;
	public CRServo angler;
	
	CurRobot(HardwareMap hardwareMap, int dummy) {
		super(hardwareMap);
	}
	
	public CurRobot(HardwareMap hardwareMap) {
		super(hardwareMap);
		
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
		scoreDump = hardwareMap.get(Servo.class, "ScoreDump");
		parker = hardwareMap.get(Servo.class, "Parker");
		
		angler = hardwareMap.get(CRServo.class, "Angler");
		
	}
	
}
