package org.firstinspires.ftc.teamcode.RoverRuckus.assets;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwareTestBot  {

    //Expansion Hub 1
    DcMotor rightFront, rightBack, leftFront, leftBack;

    //Expansion Hub 2
    public DcMotor Hooke, Arm, Collection;
    public Servo Marker;
    public DistanceSensor SensorRange;

    HardwareMap hwMap;
    public ElapsedTime period = new ElapsedTime();
	
	public DriveHandler drive;
    public HardwareTestBot() {

    }

    public void init(HardwareMap ahwMap){

        hwMap = ahwMap;

        leftFront = hwMap.get(DcMotor.class,"FrontLeft");
        rightFront = hwMap.get(DcMotor.class,"FrontRight");
        leftBack = hwMap.get(DcMotor.class,"BackLeft");
        rightBack = hwMap.get(DcMotor.class,"BackRight");

        //Hooke = hwMap.get(DcMotor.class, "Hooke");
        //Arm = hwMap.get(DcMotor.class, "Arm");
        //Marker = hwMap.get(Servo.class,"Marker");
        //Collection = hwMap.get(Servo.class, "Collection");

        //CensorRage = hwMap.get(DistanceSensor.class, "SensorRange");

        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        //Hooke.setDirection(DcMotorSimple.Direction.REVERSE);

        drive = new DriveHandler(this);
		

	    drive.setModeEncoder();
        //Arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Hooke.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
