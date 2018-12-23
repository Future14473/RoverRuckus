/* Copyright (c) 2018 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This 2018-2019 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the gold and silver minerals.
 * <p>
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 * <p>
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@TeleOp(name = "Test: TensorFlow Object Detection1", group = "Test")
public class ObjectDetectionTest extends LinearOpMode {
	private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
	private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
	private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
	
	/*
	 * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
	 * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
	 * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
	 * web site at https://developer.vuforia.com/license-manager.
	 *
	 * Vuforia license keys are always 380 characters long, and look as if they contain mostly
	 * random data. As an example, here is a example of a fragment of a valid key:
	 *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
	 * Once you've obtained a license key, copy the string from the Vuforia web site
	 * and paste it in to your code on the next line, between the double quotes.
	 */
	private static final String VUFORIA_KEY = "Aavay7//////AAABmS26wV70nE/XoqC91tMM/rlwbqInv/YUads4QRll085q/yT" +
		                                          "+qW0qdyrUwXPXbvwDkGhnffFMGIizzvfrXviNCbfAAgJzSwDJuL0MJl3LRE2FU4JMKKU2v7V+XGChhH91BXriKEtx4PDCq5DwSpCT1TP3XSJrouflaIEdqxTcUz/LaIEh4phJs35awBUu+g+4i3EKMJBsYWyJ0V9jdI5DLCVhXkKtBpKgJbO3XFx40Ig/HFXES1iUaOk2fj9SG/jRUsWLH1cs35/g289Xs6BTQTHnGpX9bcOvK0m4NkhogjqbT7S76O91jeheUZwazesROu848shb317YhWIclBSR/vV9/I2fT+485YdwnaxuS8K9";
	
	/**
	 * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
	 * localization engine.
	 */
	private VuforiaLocalizer vuforia;
	private static final int maxDiff = 20;
	
	private static class RecognitionComparator implements Comparator<Recognition> {
		
		@Override
		public int compare(Recognition lhs, Recognition rhs) {
			return (int) (lhs.getBottom() - rhs.getBottom());
		}
	}
	
	RecognitionComparator recognitionComparator = new RecognitionComparator();
	/**
	 * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
	 * Detection engine.
	 */
	private TFObjectDetector tfod;
	
	@Override
	public void runOpMode() {
		// The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
		// first.
		/*
		 * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
		 */
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
		
		parameters.vuforiaLicenseKey = VUFORIA_KEY;
		parameters.cameraDirection = CameraDirection.BACK;
		
		//  Instantiate the Vuforia engine
		vuforia = ClassFactory.getInstance().createVuforia(parameters);
		telemetry.addLine("hello worod");
		telemetry.update();
		// Loading trackables is not necessary for the Tensor Flow Object Detection engine.
		//*
		if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
			initTfod();
		} else {
			telemetry.addData("Sorry!", "This device is not compatible with TFOD");
		}
		
		//** Wait for the game to begin *//*
		telemetry.addData(">", "Press Play to start tracking");
		telemetry.update();
		waitForStart();
		
		if (opModeIsActive()) {
			//** Activate Tensor Flow Object Detection. *//*
			if (tfod != null) {
				tfod.activate();
			}
			
			while (opModeIsActive()) if (tfod != null) {
				// getUpdatedRecognitions() will return null if no new information is available since
				// the last time that call was made.
				List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
				if (updatedRecognitions != null) {
					telemetry.addData("# Object Detected", updatedRecognitions.size());
					Collections.sort(updatedRecognitions, recognitionComparator);
					int numValid = 0;
					float prevTop = 0;
					int goldOccur = -1;
					for (Recognition recognition : updatedRecognitions) {
						if (recognition.getConfidence() < 0.7) continue;
						if (numValid == 0) {
							numValid++;
							prevTop = recognition.getTop();
						} else if (Math.abs(recognition.getTop() - prevTop) > maxDiff) {
							numValid = 0;
							goldOccur = -1;
							continue;
						} else prevTop = recognition.getTop();
						//recog is valid.
						if (recognition.getLabel().equals(LABEL_GOLD_MINERAL))
							if (goldOccur >= 0) {
								numValid = 0;
								goldOccur = -1;
							} else
								goldOccur = numValid - 1;
						if (++numValid == 3) break;
					}
					if (numValid == 3) {
						//celebrate!!!!
					}
				}
			}
		}
		
		if (tfod != null) {
			tfod.shutdown();
		}
	}
	
	/**
	 * Initialize the Vuforia localization engine.
	 */
	private void initVuforia() {
	
	}
	
	/**
	 * Initialize the Tensor Flow Object Detection engine.
	 */
	private void initTfod() {
		int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
			"tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
		tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
		tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
	}
}
