package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwareTestBot  {

    //Expansion Hub 1
    DcMotor RightFront, RightBack, LeftFront, LeftBack;

    //Expansion Hub 2
    DcMotor Hooke,Arm;
    Servo Marker;
    DistanceSensor CensorRage;

    HardwareMap hwMap;
    public ElapsedTime period = new ElapsedTime();

    public HardwareTestBot() {

    }

    public void init(HardwareMap ahwMap){

        hwMap = ahwMap;

        LeftFront = hwMap.get(DcMotor.class,"FrontLeft");
        RightFront = hwMap.get(DcMotor.class,"FrontRight");
        LeftBack = hwMap.get(DcMotor.class,"BackLeft");
        RightBack = hwMap.get(DcMotor.class,"BackRight");

        Hooke = hwMap.get(DcMotor.class, "Hooke");
        Arm = hwMap.get(DcMotor.class, "Arm");
        Marker = hwMap.get(Servo.class,"Marker");

        CensorRage = hwMap.get(DistanceSensor.class, "SensorRange");

        RightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        RightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        Hooke.setDirection(DcMotorSimple.Direction.REVERSE);


        LeftFront.setPower(0);
        RightFront.setPower(0);
        LeftBack.setPower(0);
        RightBack.setPower(0);
        Hooke.setPower(0);

        LeftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LeftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //Arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Hooke.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
