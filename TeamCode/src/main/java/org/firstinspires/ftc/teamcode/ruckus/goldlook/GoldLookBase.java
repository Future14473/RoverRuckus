package org.firstinspires.ftc.teamcode.ruckus.goldlook;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.config.Constants;

import java.util.Comparator;
import java.util.List;

public class GoldLookBase {
	public static final  String           LABEL_GOLD_MINERAL   = "Gold Mineral";
	private static final String           TFOD_MODEL_ASSET     = "RoverRuckus.tflite";
	private static final String           LABEL_SILVER_MINERAL = "Silver Mineral";
	private static final double           MIN_CONFIDENCE       = 0.5;
	private static final double           SQUASHNESS_MULT      = -10;
	private static final double           CONFIDENCE_MULT      = 1;
	private static final double           SIZE_MULT            = 1d / 150;
	private static final double           LEFT_MULT            = -1d / 200;
	private static final double           SILVER_BIAS          = 0.2;
	private static final double           MIN_SCORE            = 0.5;
	protected            TFObjectDetector tfod;
	private              VuforiaLocalizer vuforia;
	
	public GoldLookBase(HardwareMap hardwareMap) {
		initVuforia();
		if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
			initTfod(hardwareMap);
		} else {
			throw new UnsupportedOperationException(
					"This device is not compatible with TFOD");
		}
	}
	
	public List<Recognition> getUpdatedRecognitions() {
		return tfod.getUpdatedRecognitions();
	}
	
	public List<Recognition> getSortedRecognitions() {
		List<Recognition> updatedRecognitions = getUpdatedRecognitions();
		if (updatedRecognitions == null) return null;
		sortRecognitions(updatedRecognitions);
		return updatedRecognitions;
	}
	
	public List<Recognition> getFilteredRecognitions() {
		List<Recognition> updatedRecognitions = getUpdatedRecognitions();
		if (updatedRecognitions == null) return null;
		filterRecognitions(updatedRecognitions);
		return updatedRecognitions;
	}
	
	public void sortRecognitions(List<Recognition> updatedRecognitions) {
		double minLeft = getMinLeft(updatedRecognitions);
		updatedRecognitions.sort(Comparator.comparingDouble(
				(Recognition recognition) -> score(recognition, minLeft)).reversed());
	}
	
	public void filterRecognitions(List<Recognition> updatedRecognitions) {
		double minLeft = getMinLeft(updatedRecognitions);
		updatedRecognitions.removeIf(
				(Recognition recognition) -> score(recognition, minLeft) < MIN_SCORE);
	}
	
	protected double getMinLeft(List<Recognition> updatedRecognitions) {
		double minLeft = Double.MAX_VALUE;
		for (Recognition recognition : updatedRecognitions) {
			minLeft = Math.min(minLeft, recognition.getLeft());
		}
		return minLeft;
	}
	
	/**
	 * Initialize the Vuforia localization engine.
	 */
	private void initVuforia() {
		VuforiaLocalizer.Parameters parameters =
				new VuforiaLocalizer.Parameters();
		parameters.vuforiaLicenseKey = Constants.VUFORIA_KEY;
		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
		parameters.useExtendedTracking = false;
		//  Instantiate the Vuforia engine
		vuforia = ClassFactory.getInstance().createVuforia(parameters);
		// Loading trackables is not necessary for the Tensor Flow Object
		// Detection engine.
	}
	
	/**
	 * Initialize the Tensor Flow Object Detection engine.
	 * WHICH REQUIRES A VUFORIA to work. for some reason.
	 */
	private void initTfod(HardwareMap hardwareMap) {
		int tfodMonitorViewId = hardwareMap.appContext.getResources()
		                                              .getIdentifier(
				                                              "tfodMonitorViewId",
				                                              "id",
				                                              hardwareMap.appContext
						                                              .getPackageName());
		TFObjectDetector.Parameters tfodParameters =
				new TFObjectDetector.Parameters(tfodMonitorViewId);
		tfodParameters.minimumConfidence = MIN_CONFIDENCE;
		tfod = ClassFactory.getInstance()
		                   .createTFObjectDetector(tfodParameters, vuforia);
		tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL,
		                        LABEL_SILVER_MINERAL);
	}
	
	public void activate() {
		tfod.activate();
	}
	
	private static double squashness(Recognition recognition) {
		double a = recognition.getHeight();
		double b = recognition.getWidth();
		return Math.abs(a - b) / (a + b);
	}
	
	private static double score(Recognition recognition, double minLeft) {
		return CONFIDENCE_MULT * square(recognition.getConfidence()) + //more confidence is better
		       SQUASHNESS_MULT * square(squashness(recognition)) + //squashness is very bad
		       SIZE_MULT * Math.min(recognition.getWidth(), recognition.getHeight()) +
		       //bigger is better
		       LEFT_MULT * (recognition.getLeft() - minLeft) + //lower is better
		       //silver needs bias
		       (recognition.getLabel().equals(LABEL_SILVER_MINERAL) ? SILVER_BIAS : 0);
	}
	
	private static double square(double v) {
		return v * v;
	}
}
