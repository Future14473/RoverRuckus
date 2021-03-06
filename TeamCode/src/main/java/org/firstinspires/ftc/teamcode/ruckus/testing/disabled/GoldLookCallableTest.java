package org.firstinspires.ftc.teamcode.ruckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.lib.opmode.Button;
import org.firstinspires.ftc.teamcode.lib.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.ruckus.goldlook.GoldLookDoubleCallable;

@TeleOp(group = "test")
@Disabled
public class GoldLookCallableTest extends OurLinearOpMode {
	private int                    v = -1;
	private GoldLookDoubleCallable goldLookDoubleCallable;
	private Button                 a = new Button(() -> gamepad1.a);
	
	@Override
	protected void initialize() {
		goldLookDoubleCallable = new GoldLookDoubleCallable(hardwareMap);
	}
	
	@Override
	protected void run() {
		while (opModeIsActive()) {
			if (a.pressed()) {
				v = -1;
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
