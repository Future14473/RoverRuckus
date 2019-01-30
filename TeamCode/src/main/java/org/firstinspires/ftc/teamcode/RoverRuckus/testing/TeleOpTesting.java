package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ThreadPool;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;

import java.util.concurrent.ScheduledExecutorService;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.Button.State.*;

@TeleOp(group = "test")
public class TeleOpTesting extends OurLinearOpMode {
	//driving
	private boolean gyroDrive = true;
	private boolean reverseDrive = false;
	private double rotationOffSet = 0;
	//arm
	private static final int MOTOR_MIN = 100, ARM_MAX = 4500, HOOK_MAX = 26000;
	//buttons
	private Button gp1y = new Button(() -> gamepad1.y);
	private Button gp1b = new Button(() -> gamepad1.b);
	private Button gp2lsb = new Button(() -> gamepad2.left_stick_button);
	private Button gp2rsb = new Button(() -> gamepad2.right_stick_button);
	private Button gp2dpadlr = new Button(() -> gamepad2.dpad_left || gamepad2.dpad_right);
	//auto parts/separate threads
	private ScheduledExecutorService scheduledExecutorService = ThreadPool.newScheduledExecutor(3, "TeleOp Threads");
	
	@Override
	protected void initialize() throws InterruptedException {
		robot.initIMU();
		robot.wheels.setMode(RUN_USING_ENCODER);
		
		robot.scoreArm.setMode(STOP_AND_RESET_ENCODER);
		robot.scoreArm.setMode(RUN_USING_ENCODER);
		
		waitUntil(robot.imu::isGyroCalibrated, 2500, MILLISECONDS);
	}
	
	@Override
	protected void run() {
		while (opModeIsActive()) {
			/*-----------------*\
		    |    GAMEPAD 1    |
			\* ----------------*/
			double speedMult = gamepad1.right_bumper ? 10 : (gamepad1.left_bumper ? 1.0 / 3 : 1);
			double direction = Math.atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y);
			
			if (gp1b.pressed()) gyroDrive = !gyroDrive;
			Button.State gp1yState = gp1y.getState();
			if (gyroDrive) {
				if (gp1yState == DOWN) {
					speedMult = 0;
				} else if (gp1yState == RELEASED) {
					rotationOffSet = robot.getDirection() + direction;
				}
				direction += robot.getDirection() - rotationOffSet;
				telemetry.addData("DIRECTION", "GYRO");
			} else {
				if (gp1yState == PRESSED) reverseDrive = !reverseDrive;
				if (reverseDrive) direction += Math.PI;
				telemetry.addData("DIRECTION", reverseDrive ? "HOOK FRONT" : "ARM FRONT");
			}
			
			double turnRate = gamepad1.right_stick_x * speedMult;
			double speed = Math.pow(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1.7) * speedMult;
			robot.moveAt(direction, speed, turnRate);
			/*-----------------*\
			 |    GAMEPAD 2    |
			\* ----------------*/
			//collectArm
			if (!gamepad2.x && (robot.collectArm.getCurrentPosition() < MOTOR_MIN && -gamepad2.left_stick_x < 0 //
					|| robot.collectArm.getCurrentPosition() > ARM_MAX && -gamepad2.left_stick_x > 0)) {
				robot.collectArm.setPower(0);
			} else {
				robot.collectArm.setPower(gamepad2.left_stick_x / 2); //double negation
			}
			if (gp2lsb.pressed()) {
				robot.collectArm.setMode(STOP_AND_RESET_ENCODER);
				robot.collectArm.setMode(RUN_USING_ENCODER);
			}
			telemetry.addData("COLLECT ARM:", robot.collectArm.getCurrentPosition());
			//scoreArm : negate stick y value
			if (!gamepad2.x && (robot.scoreArm.getCurrentPosition() < MOTOR_MIN && -gamepad2.right_stick_y < 0 ||//
					robot.scoreArm.getCurrentPosition() > ARM_MAX && gamepad2.right_stick_y > 0))
				robot.scoreArm.setPower(0);
			else robot.scoreArm.setPower(gamepad2.right_stick_y / 2); //double negation
			if (gp2rsb.pressed()) {
				robot.scoreArm.setMode(STOP_AND_RESET_ENCODER);
				robot.scoreArm.setMode(RUN_USING_ENCODER);
			}
			//scooper
			robot.scooper.setPower(gamepad2.right_bumper ? 1 : gamepad2.left_bumper ? -0.3 : 0);
			//angler
			robot.angler.setPower(-gamepad2.left_stick_y);
			//hook
			if (gamepad2.dpad_up && (-robot.hook.getCurrentPosition() <= HOOK_MAX || gamepad2.x)) {
				robot.hook.setPower(1);
			} else if (gamepad2.dpad_down && (-robot.hook.getCurrentPosition() >= MOTOR_MIN || gamepad2.x)) {
				robot.hook.setPower(-1);
			} else robot.hook.setPower(0);
			if (gp2dpadlr.pressed()) {
				robot.hook.setMode(STOP_AND_RESET_ENCODER);
				robot.hook.setMode(RUN_USING_ENCODER);
			}
			
			telemetry.update();
		}
	}
	
	@Override
	protected void cleanup() {
	
	}
}
