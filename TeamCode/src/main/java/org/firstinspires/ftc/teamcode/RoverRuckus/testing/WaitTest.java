package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.ModifiedLinearOpMode;

@TeleOp(group = "test")
public class WaitTest extends ModifiedLinearOpMode {
	
	@Override
	protected void runOpMode() throws InterruptedException {
		while(opModeIsActive()) {
			telemetry.addLine("HELLO!!");
			telemetry.update();
			waitUntil(() -> gamepad1.a);
			telemetry.addLine("HEY!!!");
			telemetry.update();
			waitUntil(() -> !gamepad1.a);
		}
	}
	
	@Override
	protected void cleanup() {
	
	}
}
