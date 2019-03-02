package org.firstinspires.ftc.teamcode.RoverRuckus.goldlook;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("Duplicates")
public class GoldLookDoubleCallable extends GoldLookBase implements Callable<Integer> {
	
	public GoldLookDoubleCallable(HardwareMap hardwareMap) {
		super(hardwareMap);
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
		} catch (InterruptedException ignored) {
		} finally {
			tfod.deactivate();
		}
		return look;
	}
	
	/**
	 * returns 2 if both is white, 1 if right is gold, 0 if left is gold, -1
	 * if none detected.
	 */
	protected int tryDetect() {
		List<Recognition> recognitions = getUpdatedRecognitions();
		if (recognitions == null || recognitions.size() < 2) return -1;
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
		filterRecognitions(recognitions);
		if (recognitions.size() != 2) return -1;
		float aPos = -1, bPos = -1;
		boolean aGold = false, bGold = false;
		
		for (Recognition recognition : recognitions) {
			if (aPos == -1) {
				aGold = recognition.getLabel().equals(LABEL_GOLD_MINERAL);
				aPos = recognition.getTop();
			} else {
				bGold = recognition.getLabel().equals(LABEL_GOLD_MINERAL);
				bPos = recognition.getTop();
			}
		}
		if (bPos == -1 || aGold && bGold) return -1;
		if (!aGold && !bGold) return 2;
		return (aGold == aPos < bPos) ? 0 : 1;
	}
}