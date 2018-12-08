package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RoverRuckus.automonous.DriveHandler;

public class HardwareTestBot  {

	public DcMotor rightFront, rightBack, leftFront, leftBack; //DEAR BEN: PLEASE MAKE YOUR NAMING
    //THINGS RIGHT... ONLY CLASSES GET TO BE CAPTALIZED
    DcMotor Hooke;
    //DcMotor Arm;
    //DcMotor Pivot, Succ, Pivot2, Pivot3;
    //public GoldAlignDetector detector;
    HardwareMap hwMap;
    //Benjamin Added this
    DriveHandler driveHandler;
    public HardwareTestBot() {

    }

    public void init(HardwareMap ahwMap){

        hwMap = ahwMap;

        leftFront = hwMap.get(DcMotor.class,"FrontLeft");
        rightFront = hwMap.get(DcMotor.class,"FrontRight");
        leftBack = hwMap.get(DcMotor.class,"BackLeft");
        rightBack = hwMap.get(DcMotor.class,"BackRight");
        Hooke = hwMap.get(DcMotor.class, "Hooke");
        //Arm = hwMap.get(DcMotor.class, "Arm");
/*

        Pivot = hwMap.get(DcMotor.class, "pivot");
        Succ = hwMap.get(DcMotor.class, "succ");
        Pivot2 = hwMap.get(DcMotor.class, "pivot2");
        //Pivot3 = hwMap.get(DcMotor.class, "pivot3");
*/

        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        Hooke.setDirection(DcMotorSimple.Direction.REVERSE);


        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        Hooke.setPower(0);

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //Arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Hooke.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveHandler = new DriveHandler(this);
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
