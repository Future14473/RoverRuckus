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

package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


public class GoldLooker {
	private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
	private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
	private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
	
	@SuppressWarnings("SpellCheckingInspection")
	private static final String VUFORIA_KEY = "Aavay7//////AAABmS26wV70nE/XoqC91tMM/rlwbqInv/YUads4QRll085q/yT" +
		                                          "+qW0qdyrUwXPXbvwDkGhnffFMGIizzvfrXviNCbfAAgJzSwDJuL0MJl3LRE2FU4JMKKU2v7V+XGChhH91BXriKEtx4PDCq5DwSpCT1TP3XSJrouflaIEdqxTcUz/LaIEh4phJs35awBUu+g+4i3EKMJBsYWyJ0V9jdI5DLCVhXkKtBpKgJbO3XFx40Ig/HFXES1iUaOk2fj9SG/jRUsWLH1cs35/g289Xs6BTQTHnGpX9bcOvK0m4NkhogjqbT7S76O91jeheUZwazesROu848shb317YhWIclBSR/vV9/I2fT+485YdwnaxuS8K9";
	private static final int MAX_DIFF = 40;
	private VuforiaLocalizer vuforia;
	private TFObjectDetector tfod;
	
	public GoldLooker(HardwareMap hardwareMap) {
		initVuforia();
		if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
			initTfod(hardwareMap);
		} else {
			throw new UnsupportedOperationException("This device is not compatibles with TFOD");
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
		for (Recognition recognition : recognitions) {
			if (recognition.getConfidence() < 0.8) continue;
			if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) return 1;
			else return 0;
		}
		return -1;
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
		int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
			"tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
		tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
		tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
	}
}