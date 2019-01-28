package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.DecoratedLinearOpMode;

import java.util.function.Supplier;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.AdjustedCumulativeOrientation.adjustOrientation;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.Button.State.*;

@TeleOp(group = "test")
public class TeleOpTesting extends DecoratedLinearOpMode {
	private Supplier<Orientation> orientation;
	private boolean gyroDrive = true;
	private boolean reverseDrive = false;
	private double rotationOffSet = 0;
	private Button gp1y = new Button(() -> gamepad1.a);
	private Button gp1b = new Button(() -> gamepad1.b);
	
	@Override
	protected void initialize() throws InterruptedException {
		robot.wheels.setMode(RUN_USING_ENCODER);
		orientation = getOrientationSupplier();
	}
	
	@Override
	protected void run() throws InterruptedException {
		while (opModeIsActive()) {
			double speedMult = gamepad1.right_bumper ? 10 : (gamepad1.left_bumper ? 1.0 / 3 : 1);
			double direction = Math.atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y);
			
			if (gp1b.pressed()) gyroDrive = !gyroDrive;
			Button.State gp1yState = gp1y.getState();
			if (gyroDrive) {
				if (gp1yState == HELD) {
					speedMult = 0;
				} else if (gp1yState == RELEASED) {
					rotationOffSet = getRobotDirection() + direction;
				}
				direction += getRobotDirection() - rotationOffSet;
				telemetry.addLine("GYRO DRIVING.");
			} else {
				if (gp1yState == PRESSED) reverseDrive = !reverseDrive;
				if (reverseDrive) direction += Math.PI;
				telemetry.addData("DIRECTION", reverseDrive ? "HOOK FRONT" : "ARM FRONT");
			}
			
			double turnRate = gamepad1.right_stick_x * speedMult;
			double speed = Math.pow(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1.7) * speedMult;
			robot.moveAt(direction, turnRate, speed);
			
			telemetry.update();
		}
	}
	
	private double getRobotDirection() {
		return Math.toRadians(adjustOrientation(orientation.get()).firstAngle);
	}
	
	@Override
	protected void cleanup() {
	
	}
}
