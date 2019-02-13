package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("Duplicates")
public class GoldLookDoubleCallable implements Callable<Integer> {
	private static final double MIN_CONFIDENCE       = 0.75;
	private static final String TFOD_MODEL_ASSET     = "RoverRuckus.tflite";
	private static final String LABEL_GOLD_MINERAL   = "Gold Mineral";
	private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
	@SuppressWarnings("SpellCheckingInspection")
	private static final String VUFORIA_KEY          =
			"Aavay7//////AAABmS26wV70nE" + "/XoqC91tMM" +
			"/rlwbqInv/YUads4QRll085q/yT" +
			"+qW0qdyrUwXPXbvwDkGhnffFMGIizzvfrXviNCbfAAgJzSwDJuL0MJl3LRE2FU4JMKKU2v7V" +
			"+XGChhH91BXriKEtx4PDCq5DwSpCT1TP3XSJrouflaIEdqxTcUz" +
			"/LaIEh4phJs35awBUu+g" +
			"+4i3EKMJBsYWyJ0V9jdI5DLCVhXkKtBpKgJbO3XFx40Ig/HFXES1iUaOk2fj9SG" +
			"/jRUsWLH1cs35" +
			"/g289Xs6BTQTHnGpX9bcOvK0m4NkhogjqbT7S76O91jeheUZwazesROu848shb317YhWIclBSR/vV9/I" +
			"2fT+485YdwnaxuS8K9";
	
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
		List<Recognition> listRecognitions = tfod.getUpdatedRecognitions();
		if (listRecognitions == null || listRecognitions.size() < 2) return -1;
		int ax = -1, bx = -1;
		boolean ag = false, bg = false;
		
		Recognition[] recognitions =
				listRecognitions.toArray(new Recognition[0]);
		int numRec = Math.min(recognitions.length, 6);
		int numValidRec = numRec;
		for (int i = 0; i < numRec; i++) {
			//if it overlaps closely with other recognitions and is silver,
			// override it with silver.
			for (int j = i + 1; j < numRec; j++) {
				if (recognitions[j] == null) continue;
				if (Math.hypot(
						recognitions[j].getTop() - recognitions[i].getTop(),
						recognitions[j].getBottom() -
						recognitions[i].getBottom()) <
				    Math.max(recognitions[i].getHeight(),
				             recognitions[j].getHeight())) {
					if (recognitions[j].getLabel().equals(LABEL_GOLD_MINERAL))
						recognitions[j] = null;
					else recognitions[i] = null;
					if (--numValidRec < 3) return -1;
				}
			}
		}
		if (numValidRec != 3) return -1;
		for (int i = 0; i < numRec; i++) {
			if (recognitions[i] == null) continue;
			if (ax == -1) {
				ag = recognitions[i].getLabel().equals(LABEL_GOLD_MINERAL);
				ax = (int) recognitions[i].getTop();
			} else if (bx == -1) {
				bg = recognitions[i].getLabel().equals(LABEL_GOLD_MINERAL);
				bx = (int) recognitions[i].getTop();
			} else return -1;
		}
		if (bx == -1 || ag && bg) return -1;
		if (!(ag || bg)) return 2;
		return (ag == ax < bx) ? 0 : 1;
	}
	
	/**
	 * Initialize the Vuforia localization engine.
	 */
	private void initVuforia() {
		VuforiaLocalizer.Parameters parameters =
				new VuforiaLocalizer.Parameters();
		parameters.vuforiaLicenseKey = VUFORIA_KEY;
		parameters.cameraDirection = CameraDirection.BACK;
		parameters.useExtendedTracking = false;
		//  Instantiate the Vuforia engine
		vuforia = ClassFactory.getInstance().createVuforia(parameters);
		// Loading trackables is not necessary for the Tensor Flow Object
		// Detection engine.
	}
	
	/**
	 * Initialize the Tensor Flow Object Detection engine.
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