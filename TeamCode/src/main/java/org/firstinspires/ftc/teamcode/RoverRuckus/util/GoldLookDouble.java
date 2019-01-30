package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


public class GoldLookDouble {
	private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
	private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
	private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
	
	private static final String VUFORIA_KEY = "Aavay7//////AAABmS26wV70nE/XoqC91tMM/rlwbqInv/YUads4QRll085q/yT" +
			"+qW0qdyrUwXPXbvwDkGhnffFMGIizzvfrXviNCbfAAgJzSwDJuL0MJl3LRE2FU4JMKKU2v7V" +
			"+XGChhH91BXriKEtx4PDCq5DwSpCT1TP3XSJrouflaIEdqxTcUz/LaIEh4phJs35awBUu+g" +
			"+4i3EKMJBsYWyJ0V9jdI5DLCVhXkKtBpKgJbO3XFx40Ig/HFXES1iUaOk2fj9SG/jRUsWLH1cs35" + "/g289Xs6BTQTHnGpX9bcOvK0m4NkhogjqbT7S76O91jeheUZwazesROu848shb317YhWIclBSR/vV9/I2fT+485YdwnaxuS8K9";
	private VuforiaLocalizer vuforia;
	private TFObjectDetector tfod;
	
	public void init(HardwareMap hardwareMap) {
		initVuforia();
		if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
			initTfod(hardwareMap);
		} else {
			throw new UnsupportedOperationException("This device is not compatible with TFOD");
		}
	}
	
	public void start() {
		tfod.activate();
	}
	
	public void pause() {
		tfod.deactivate();
	}
	
	public void stop() {
		tfod.shutdown();
	}
	
	
	/**
	 * returns 1 if current screen is gold, 0 if is white, -1 if none detected.
	 */
	public int look() {
		List<Recognition> listRecognitions = tfod.getUpdatedRecognitions();
		if (listRecognitions == null || listRecognitions.size() < 2) return -1;
		int ax = -1, bx = -1;
		boolean ag = false, bg = false;
		
		Recognition[] recognitions = listRecognitions.toArray(new Recognition[0]);
		for (int i = 0; i < Math.min(recognitions.length, 6); i++) {
			if (recognitions[i] == null) continue;
			//if it overlaps closely with other recognitions and is silver, override it with silver.
			for (int j = i + 1; j < Math.min(recognitions.length, 6); j++) {
				if (recognitions[j] == null) continue;
				if (Math.hypot(recognitions[j].getTop() - recognitions[i].getTop(),
						recognitions[j].getBottom() - recognitions[i].getBottom()) < Math.max(recognitions[i].getHeight(), recognitions[j].getHeight())) {
					if (recognitions[j].getLabel().equals(LABEL_GOLD_MINERAL)) recognitions[j] = null;
					else recognitions[i] = null;
					break;
				}
			}
		}
		for (Recognition recognition : recognitions) {
			if (recognition == null || recognition.getConfidence() < 0.65) continue;
			if (ax == -1) {
				ag = recognition.getLabel().equals(LABEL_GOLD_MINERAL);
				ax = (int) recognition.getTop();
			} else if (bx == -1) {
				bg = recognition.getLabel().equals(LABEL_GOLD_MINERAL);
				bx = (int) recognition.getTop();
			} else return -1;
		}
		if (bx == -1 || ag && bg) return -1;
		if (!(ag || bg)) return 0;
		return (ag == ax < bx) ? 1 : 2;
	}
	
	/**
	 * Initialize the Vuforia localization engine.
	 */
	private void initVuforia() {
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
		parameters.vuforiaLicenseKey = VUFORIA_KEY;
		parameters.cameraDirection = CameraDirection.BACK;
		//  Instantiate the Vuforia engine
		vuforia = ClassFactory.getInstance().createVuforia(parameters);
		// Loading trackables is not necessary for the Tensor Flow Object Detection engine.
	}
	
	/**
	 * Initialize the Tensor Flow Object Detection engine.
	 */
	private void initTfod(HardwareMap hardwareMap) {
		int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id",
				hardwareMap.appContext.getPackageName());
		TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
		tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
		tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
	}
}