//package org.firstinspires.ftc.teamcode.RoverRuckus.Real;
//
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//public class HardwareTestBot {
//
//	private static final float mmPerInch = 25.4f;
//	private static final float mmFTCFieldWidth = (12 * 6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
//	private static final float mmTargetHeight = (6) * mmPerInch;          // the height of the center of the target image above the floor
//	// Select which camera you want use.  The FRONT camera is the one on the same side as the screen.
//	// Valid choices are:  BACK or FRONT
//	//public OpenGLMatrix lastLocation = null;
//	DcMotor RightFront, RightBack, LeftFront, LeftBack;
//	DcMotor Pivot, Succ, Pivot2;
//	//public GoldAlignDetector detector;
//	HardwareMap hwMap;
//	boolean targetVisible;
//	//Dogeforia vuforia;
//	//WebcamName webcamName;
//	//List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
//
//	//GoldAlignDetector detector2;
//
//	HardwareTestBot() {
//
//	}
//
//	public void init(HardwareMap ahwMap) {
//
//		hwMap = ahwMap;
//		LeftFront = hwMap.get(DcMotor.class, "left_front");
//		RightFront = hwMap.get(DcMotor.class, "right_front");
//		LeftBack = hwMap.get(DcMotor.class, "left_back");
//		RightBack = hwMap.get(DcMotor.class, "right_back");
//
//		//Pivot = hwMap.get(DcMotor.class, "pivot");
//		//Succ = hwMap.get(DcMotor.class, "succ");
//		//Pivot2 = hwMap.get(DcMotor.class, "pivot2");
//		//Pivot3 = hwMap.get(DcMotor.class, "pivot3");
//
//		LeftBack.setDirection(DcMotorSimple.Direction.REVERSE);
//		LeftFront.setDirection(DcMotorSimple.Direction.REVERSE);
//
//		//Pivot3.setDirection(DcMotorSimple.Direction.REVERSE);
//
//		LeftFront.setPower(0);
//		RightFront.setPower(0);
//		LeftBack.setPower(0);
//		RightBack.setPower(0);
//
//		LeftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//		RightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//		LeftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//		RightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//
////        detector = new GoldAlignDetector();
////        detector.init(hwMap.appContext, CameraViewDisplay.getInstance());
////        detector.useDefaults();
////
////        // Optional Tuning
////        detector.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
////        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
////        detector.downscale = 0.4; // How much to downscale the input frames
////
////        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
////        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
////        detector.maxAreaScorer.weight = 0.005;
////
////        detector.ratioScorer.weight = 5;
////        detector.ratioScorer.perfectRatio = 1.0;
////
////        detector.enable();
//
//
//	}
//}
