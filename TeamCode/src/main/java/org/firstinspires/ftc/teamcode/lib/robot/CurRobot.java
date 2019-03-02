package org.firstinspires.ftc.teamcode.lib.robot;

import com.qualcomm.robotcore.hardware.*;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

public class CurRobot extends BaseRobot {
	
	public DcMotorEx hook;
	public DcMotorEx collectArm;
	public DcMotorEx scoreArm;
	public DcMotor   scooper;
	public Servo     flicker, markerDoor, collectDoor, scoreDump, parker;
	public CRServo angler;
	
	public CurRobot(HardwareMap hardwareMap) {
		super(hardwareMap);
		
		hook = hardwareMap.get(DcMotorEx.class, "Hook");
		hook.setMode(RUN_USING_ENCODER);
		hook.setZeroPowerBehavior(BRAKE);
		hook.setDirection(REVERSE);
		
		scooper = hardwareMap.get(DcMotor.class, "Scooper");
		scooper.setMode(RUN_WITHOUT_ENCODER); //no encoder
		scooper.setZeroPowerBehavior(BRAKE);
		
		collectArm = hardwareMap.get(DcMotorEx.class, "CollectArm");
		collectArm.setMode(RUN_USING_ENCODER);
		collectArm.setZeroPowerBehavior(BRAKE);
		//collectArm.setDirection(REVERSE);
		
		scoreArm = hardwareMap.get(DcMotorEx.class, "ScoreArm");
		scoreArm.setMode(RUN_USING_ENCODER);
		scoreArm.setZeroPowerBehavior(BRAKE);
		//scoreArm.setDirection(REVERSE);
		
		flicker = hardwareMap.get(Servo.class, "Flicker");
		markerDoor = hardwareMap.get(Servo.class, "MarkerDoor");
		collectDoor = hardwareMap.get(Servo.class, "CollectDoor");
		scoreDump = hardwareMap.get(Servo.class, "ScoreDump");
		parker = hardwareMap.get(Servo.class, "Parker");
		
		angler = hardwareMap.get(CRServo.class, "Angler");
		
	}
	
}
