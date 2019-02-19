package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.Constants;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.AutoMoveController;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.RampedMoveController;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.CurRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

import static java.util.concurrent.TimeUnit.SECONDS;

@TeleOp(group = "test")
public class AutoMoveControllerTest extends OurLinearOpMode {
	private static final XY FORWARD  = new XY(0, 12);
	private static final XY BACKWARD = new XY(0, -12);
	
	private       CurRobot             robot;
	private final Button               a           = new Button(() -> gamepad1.a);
	private final Button               b           = new Button(() -> gamepad1.b);
	private final Button               x           = new Button(() -> gamepad1.x);
	private final Button               y           = new Button(() -> gamepad1.y);
	private final Button               rb          =
			new Button(() -> gamepad1.right_bumper);
	private       boolean              manualDrive = false;
	private       AutoMoveController   moveController;
	private       RampedMoveController rampedMoveController;
	
	@Override
	protected void initialize() throws InterruptedException {
		robot = new CurRobot(hardwareMap);
		moveController = new AutoMoveController(robot, Constants.ENCODER_TICKS_PER_INCH,
		                                        2);
		rampedMoveController = new RampedMoveController(2);
		robot.initIMU();
		waitUntil(robot::imuIsGyroCalibrated, 3, SECONDS);
	}
	
	@Override
	protected void run() throws InterruptedException {
		moveController.reset();
		while (opModeIsActive()) {
			if (a.pressed()) moveController.addToTargetLocation(FORWARD);
			if (b.pressed()) moveController.addToTargetLocation(BACKWARD);
			if (x.pressed()) moveController.addToTargetAngle(90);
			if (y.pressed()) moveController.reset();
			if (rb.pressed()) manualDrive = !manualDrive;
			moveController.updateLocation();
			if (manualDrive) {
				MotorSetPower power = rampedMoveController.getPower(
						new XY(gamepad1.left_stick_x, -gamepad1.left_stick_y).scale(1.5),
						gamepad1.right_stick_x * 1.5, 0.8, 0.4);
				robot.wheels.setPower(power);
			} else
				moveController.moveToTarget(0.3, 0.8);
		}
	}
}
