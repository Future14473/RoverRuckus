package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MecanumDrive;

@TeleOp(group = "test")
@Disabled
public class WaitTest extends OurLinearOpMode {
	
	protected MecanumDrive drive;
	
	@Override
	protected void initialize() throws InterruptedException {}
	
	@Override
	protected void run() throws InterruptedException {
		while (opModeIsActive()) {
			waitUntil(() -> gamepad1.a);
			drive.move(0, 0.1, 1);
			waitUntil(() -> !gamepad1.a);
			drive.move(0, -0.1, 1);
		}
	}
	
	@Override
	protected void cleanup() {
	
	}
}
