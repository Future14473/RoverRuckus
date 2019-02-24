package org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.RoverRuckus.Constants;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("Duplicates")
public class GoldLookDoubleCallable implements Callable<Integer> {
	private static final double MIN_CONFIDENCE       = 0.75;
	private static final String TFOD_MODEL_ASSET     = "RoverRuckus.tflite";
	private static final String LABEL_GOLD_MINERAL   = "Gold Mineral";
	private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
	
	private VuforiaLocalizer vuforia;
	private TFObjectDetector tfod;
	
	public GoldLookDoubleCallable(HardwareMap hardwareMap) {
		initVuforia();
		if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
			initTfod(hardwareMap);
		} else {
			throw new UnsupportedOperationException(
					"This device is not compatible with TFOD");
		}
	}
	
	/**
	 * returns 2 if both is white, 1 if right is gold, 0 if left is gold, -1
	 * if none detected.
	 */
	private int tryDetect() {
		List<Recognition> recognitions = tfod.getUpdatedRecognitions();
		if (recognitions == null || recognitions.size() < 2 || recognitions.size() > 6) return -1;
//
//		Recognition[] recognitions =
//				listRecognitions.toArray(new Recognition[0]);
//		if (recognitions.length > 6) return -1;
//		int numValidRec = recognitions.length;
//		for (int i = 0; i < recognitions.length; i++) {
//			if (recognitions[i] == null) continue;
//			//if it overlaps closely with other recognitions and is silver,
//			// override it with silver.
//			for (int j = i + 1; j < recognitions.length; j++) {
//				if (recognitions[j] == null) continue;
//				if (overlap(recognitions[i], recognitions[j])) {
//					if (recognitions[j].getLabel().equals(LABEL_GOLD_MINERAL)) {
//						recognitions[j] = null;
//					} else {
//						recognitions[i] = null;
//					}
//					if (--numValidRec < 3) return -1;
//				}
//			}
//		}
//
//		if (numValidRec != 3) return -1;
		//dumb way that works
		//sort by confidence
		recognitions.sort(Comparator.comparingDouble(Recognition::getConfidence));
		float aPos = -1, bPos = -1;
		boolean aGold = false, bGold = false;
		
		for (Recognition recognition : recognitions) {
			if (recognition == null) continue; //maybe? for future implementation things?
			if (aPos == -1) {
				aGold = recognition.getLabel().equals(LABEL_GOLD_MINERAL);
				aPos = recognition.getTop();
			} else if (bPos == -1) {
				bGold = recognition.getLabel().equals(LABEL_GOLD_MINERAL);
				bPos = recognition.getTop();
			} else return -1;
		}
		if (bPos == -1 || aGold && bGold) return -1;
		if (!aGold && !bGold) return 2;
		return (aGold == aPos < bPos) ? 0 : 1;
	}
	
	/**
	 * Initialize the Vuforia localization engine.
	 */
	private void initVuforia() {
		VuforiaLocalizer.Parameters parameters =
				new VuforiaLocalizer.Parameters();
		parameters.vuforiaLicenseKey = Constants.VUFORIA_KEY;
		parameters.cameraDirection = CameraDirection.BACK;
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
	
	@Override
	public Integer call() {
		tfod.activate();
		int look = -1;
		try {
			do {
				Thread.sleep(100);
				look = tryDetect();
			} while (look == -1);
		} catch (InterruptedException e) {
			tfod.deactivate();
		}
		return look;
	}
}