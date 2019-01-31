package org.firstinspires.ftc.teamcode.RoverRuckus.old;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


public class GoldLookSingle {
	private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
	private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
	private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
	
	private static final String VUFORIA_KEY = "Aavay7//////AAABmS26wV70nE/XoqC91tMM/rlwbqInv/YUads4QRll085q/yT" +
			"+qW0qdyrUwXPXbvwDkGhnffFMGIizzvfrXviNCbfAAgJzSwDJuL0MJl3LRE2FU4JMKKU2v7V" +
			"+XGChhH91BXriKEtx4PDCq5DwSpCT1TP3XSJrouflaIEdqxTcUz/LaIEh4phJs35awBUu+g" +
			"+4i3EKMJBsYWyJ0V9jdI5DLCVhXkKtBpKgJbO3XFx40Ig/HFXES1iUaOk2fj9SG/jRUsWLH1cs35" +
			"/g289Xs6BTQTHnGpX9bcOvK0m4NkhogjqbT7S76O91jeheUZwazesROu848shb317YhWIclBSR/vV9/I2fT+485YdwnaxuS8K9";
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
	
	public void stop() {
		tfod.shutdown();
	}
	
	/**
	 * returns 1 if current screen is gold, 0 if is white, -1 if none detected.
	 */
	public int look() {
		List<Recognition> recognitions = tfod.getUpdatedRecognitions();
		if (recognitions == null) return -1;
		//Collections.sort(recognitions,byConfidence);
		boolean white = false, found = false;
		for (Recognition recognition : recognitions) {
			if (recognition.getConfidence() < 0.65) continue;
			found = true;
			if (recognition.getLabel().equals(LABEL_SILVER_MINERAL)) white = true;
		}
		if (!found) return -1;
		return white ? 0 : 1;
	}
	
	/**
	 * Initialize the Vuforia localization engine.
	 */
	private void initVuforia() {
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
		parameters.vuforiaLicenseKey = VUFORIA_KEY;
		parameters.cameraDirection = CameraDirection.FRONT;
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