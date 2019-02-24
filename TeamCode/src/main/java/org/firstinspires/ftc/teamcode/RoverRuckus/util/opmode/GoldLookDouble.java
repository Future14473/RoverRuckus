package org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.RoverRuckus.Constants;

import java.util.List;
import java.util.concurrent.*;

@SuppressWarnings("Duplicates")
public class GoldLookDouble {
	private static final double           MIN_CONFIDENCE       = 0.75;
	private static final String           TFOD_MODEL_ASSET     =
			"RoverRuckus.tflite";
	private static final String           LABEL_GOLD_MINERAL   =
			"Gold " + "Mineral";
	private static final String           LABEL_SILVER_MINERAL =
			"Silver Mineral";
	private final        ExecutorService  executorService      =
			Executors.newSingleThreadExecutor();
	private              VuforiaLocalizer vuforia;
	private              TFObjectDetector tfod;
	private              Future<Integer>  theFuture;
	
	public void init(HardwareMap hardwareMap) {
		initVuforia();
		if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
			initTfod(hardwareMap);
		} else {
			throw new UnsupportedOperationException(
					"This device is not " + "compatible with TFOD");
		}
	}
	
	public boolean hasDetected() {
		return theFuture != null && theFuture.isDone();
	}
	
	public void start() {
		if (theFuture != null) theFuture.cancel(true);
		theFuture = executorService.submit(this::doDetect);
	}
	
	public void stop() {
		executorService.shutdownNow();
		tfod.shutdown();
	}
	
	@Deprecated
	public int getLook() {
		return -1;
	}
	
	/**
	 * Gets the look value, waiting up to the specified timeout.
	 *
	 * @return the look value, if found, or -1, if not found
	 * @throws InterruptedException if the thread is interrupted while waiting.
	 */
	public int getLook(long timeout, TimeUnit unit)
			throws InterruptedException {
		if (theFuture == null) start();
		try {
			return theFuture.get(timeout, unit);
		} catch (ExecutionException e) {
			e.printStackTrace();
			return -1;
		} catch (TimeoutException e) {
			return -1;
		}
	}
	
	/**
	 * returns 2 if both is white, 1 if right is gold, 0 if left is gold, -1
	 * if none detected.
	 */
	public int detect() {
		List<Recognition> listRecognitions = tfod.getUpdatedRecognitions();
		if (listRecognitions == null || listRecognitions.size() < 2) return -1;
		int ax = -1, bx = -1;
		boolean ag = false, bg = false;
		
		Recognition[] recognitions =
				listRecognitions.toArray(new Recognition[0]);
		
		for (int i = 0; i < Math.min(recognitions.length, 6); i++) {
			if (recognitions[i].getConfidence() < MIN_CONFIDENCE) {
				recognitions[i] = null;
			}
		}
		for (int i = 0; i < Math.min(recognitions.length, 6); i++) {
			if (recognitions[i] == null) continue;
			//if it overlaps closely with other recognitions and is silver,
			// override it with silver.
			for (int j = i + 1; j < Math.min(recognitions.length, 6); j++) {
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
					break;
				}
			}
		}
		for (int i = 0; i < Math.min(recognitions.length, 6); i++) {
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
		if (!(ag || bg)) return 0;
		return (ag == ax < bx) ? 1 : 2;
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
	 */
	private void initTfod(HardwareMap hardwareMap) {
		int tfodMonitorViewId = hardwareMap.appContext
				.getResources().getIdentifier(
						"tfodMonitorViewId", "id",
						hardwareMap.appContext.getPackageName());
		TFObjectDetector.Parameters tfodParameters =
				new TFObjectDetector.Parameters(tfodMonitorViewId);
		tfod = ClassFactory.getInstance()
		                   .createTFObjectDetector(tfodParameters, vuforia);
		tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL,
		                        LABEL_SILVER_MINERAL);
	}
	
	public void activate() {
		tfod.activate();
	}
	
	private Integer doDetect() {
		tfod.activate();
		int look;
		do look = detect(); while (look == -1 && !Thread.interrupted());
		tfod.shutdown();
		executorService.shutdown();
		return look;
	}
}