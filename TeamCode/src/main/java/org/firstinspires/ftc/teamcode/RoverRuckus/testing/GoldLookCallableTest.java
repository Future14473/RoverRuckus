package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.GoldLookDoubleCallable;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.OurLinearOpMode;
@TeleOp(group = "test")
public class GoldLookCallableTest extends OurLinearOpMode {
	int v = -1;
	private GoldLookDoubleCallable goldLookDoubleCallable;
	private Button                 a = new Button(() -> gamepad1.a);
	
	@Override
	protected void initialize() throws InterruptedException {
		goldLookDoubleCallable = new GoldLookDoubleCallable(hardwareMap);
	}
	
	@Override
	protected void run() throws InterruptedException {
		while (opModeIsActive()) {
			if (a.pressed()) {
				v=-1;
				telemetry.addData("Look value", "looking");
				telemetry.update();
				v = goldLookDoubleCallable.call();
			}
			telemetry.addData("Look value:", v);
			telemetry.update();
		}
	}
	
	@Override
	protected void cleanup() {
	}
}
