package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwareTestBot  {

    DcMotor RightFront, RightBack, LeftFront, LeftBack;
    DcMotor Hooke;
    //DcMotor Arm;
    //DcMotor Pivot, Succ, Pivot2, Pivot3;
    public GoldAlignDetector detector;
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
        //Arm = hwMap.get(DcMotor.class, "Arm");
/*

        Pivot = hwMap.get(DcMotor.class, "pivot");
        Succ = hwMap.get(DcMotor.class, "succ");
        Pivot2 = hwMap.get(DcMotor.class, "pivot2");
        //Pivot3 = hwMap.get(DcMotor.class, "pivot3");
*/

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

        /*

        detector = new GoldAlignDetector();
        detector.init(hwMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();

        // Optional Tuning
        detector.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005;

        detector.ratioScorer.weight = 5;
        detector.ratioScorer.perfectRatio = 1.0;

        detector.enable();
*/
    }
}
