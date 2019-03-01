package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import android.annotation.SuppressLint;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.GoldLookBase;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.OurLinearOpMode;

import java.util.List;

@TeleOp(group = "test")
public class GoldLookTest extends OurLinearOpMode {
	private GoldLookBase look;
	
	@Override
	protected void initialize() throws InterruptedException {
		look = new GoldLookBase(hardwareMap);
	}
	
	@SuppressLint("DefaultLocale")
	@Override
	protected void run() throws InterruptedException {
		look.activate();
		while (opModeIsActive()) {
			List<Recognition> updatedRecognitions = look.getFilteredRecognitions();
			if (updatedRecognitions != null) {
				for (int i = 0; i < updatedRecognitions.size(); i++) {
					Recognition recognition = updatedRecognitions.get(i);
					telemetry.addLine(String.format("Recognition %d:", i))
					         .addData("label", recognition.getLabel())
					         .addData("bottom", "%4.2f", recognition.getBottom())
					         .addData("left", "%4.2f", recognition.getLeft())
					         .addData("height", "%4.2f", recognition.getHeight())
					         .addData("width", "%4.2f", recognition.getWidth())
					         .addData("conf", "%4.2f", recognition.getConfidence());
				}
				telemetry.update();
			}
		}
	}
}
