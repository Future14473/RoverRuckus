package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.DecoratedLinearOpMode;

import java.util.function.Supplier;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.Button.State.*;

@TeleOp(group = "test")
public class TeleOpTesting extends DecoratedLinearOpMode {
	private static final int ARM_MIN = 20;
	private Supplier<Orientation> orientation;
	private boolean gyroDrive = true;
	private boolean reverseDrive = false;
	private double rotationOffSet = 0;
	private Button gp1y = new Button(() -> gamepad1.y);
	private Button gp1b = new Button(() -> gamepad1.b);
	
	@Override
	protected void initialize() throws InterruptedException {
		robot.wheels.setMode(RUN_USING_ENCODER);
		orientation = getOrientationSupplier();
		
		robot.collectArm.setMode(STOP_AND_RESET_ENCODER);
		robot.collectArm.setMode(RUN_USING_ENCODER);
		
		robot.scoreArm.setMode(STOP_AND_RESET_ENCODER);
		robot.scoreArm.setMode(RUN_USING_ENCODER);
		
		waitUntil(robot.imu::isGyroCalibrated, 5, SECONDS);
	}
	
	@Override
	protected void run() {
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
				telemetry.addLine("GYRO DRIVING");
			} else {
				if (gp1yState == PRESSED) reverseDrive = !reverseDrive;
				if (reverseDrive) direction += Math.PI;
				telemetry.addData("DIRECTION", reverseDrive ? "HOOK FRONT" : "ARM FRONT");
			}
			
			double turnRate = gamepad1.right_stick_x * speedMult;
			double speed = Math.pow(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1.7) * speedMult;
			robot.moveAt(direction, turnRate, speed);
			//collectArm
			if (!gamepad2.x && robot.collectArm.getCurrentPosition() < ARM_MIN && gamepad2.left_stick_x > 0)
				robot.collectArm.setPower(0);
			else robot.collectArm.setPower(gamepad2.left_stick_x / 2);
			//scoreArm
			if (!gamepad2.x && robot.scoreArm.getCurrentPosition() < ARM_MIN && -gamepad2.right_stick_y < 0)
				robot.scoreArm.setPower(0);
			else robot.scoreArm.setPower(gamepad2.right_stick_y / 2);
			//scooper
			if (gamepad2.right_bumper) {
				robot.scooper.setPower(1);
			} else if (gamepad2.left_bumper) {
				robot.scooper.setPower(-0.3);
			} else {
				robot.scooper.setPower(0);
			}
			//angler
			if (gamepad2.dpad_up) {
				robot.angler.setPower(0.6);
			} else if (gamepad2.dpad_down) {
				robot.angler.setPower(0.4);
			} else {
				robot.angler.setPower(0.5);
			}
			telemetry.addData("ROBOT ANGLE:", orientation.get());
			telemetry.update();
		}
	}
	
	private double getRobotDirection() {
		return orientation.get().firstAngle;
	}
	
	@Override
	protected void cleanup() {
	
	}
}
