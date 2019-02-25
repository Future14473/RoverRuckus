package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.CycleTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.*;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.CurRobot;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.DEFAULT_MAX_ACCELERATIONS;

@TeleOp(group = "test")
public class AutoMoveControllerTest extends OurLinearOpMode {
	private static final Magnitudes MAX_VELOCITIES = new Magnitudes(0.8, 0.5);
	private static final XY         FORWARD        = new XY(0, 12);
	private static final XY         BACKWARD       = new XY(0, -12);
	
	private final Button               a           = new Button(() -> gamepad1.a);
	private final Button               b           = new Button(() -> gamepad1.b);
	private final Button               x           = new Button(() -> gamepad1.x);
	private final Button               y           = new Button(() -> gamepad1.y);
	private final Button               rb          =
			new Button(() -> gamepad1.right_bumper);
	private       CurRobot             robot;
	private       boolean              manualDrive = false;
	private       AutoMoveController   autoMoveController;
	private       ManualMoveController manualMoveController;
	private       CycleTime            time        = new CycleTime();
	
	@Override
	protected void initialize() throws InterruptedException {
		robot = new CurRobot(hardwareMap);
		RampedMoveController rampedMoveController =
				new RampedMoveController(DEFAULT_MAX_ACCELERATIONS);
		autoMoveController =
				new AutoMoveController(robot, new PositionTracker(), rampedMoveController);
		manualMoveController = new ManualMoveController(robot, rampedMoveController);
		robot.initIMU();
		waitUntil(robot::imuIsGyroCalibrated, 3, SECONDS);
	}
	
	@Override
	protected void run() {
		autoMoveController.resetTargetPosition();
		autoMoveController.setMaxVelocities(MAX_VELOCITIES);
		time.reset();
		while (opModeIsActive()) {
			if (a.pressed()) autoMoveController.addToTargetXY(FORWARD);
			if (b.pressed()) autoMoveController.addToTargetXY(BACKWARD);
			if (x.pressed()) autoMoveController.addToTargetAngle(Math.toRadians(90));
			if (y.pressed()) autoMoveController.resetTargetPosition();
			if (rb.pressed()) manualDrive = !manualDrive;
			autoMoveController.updateLocation();
			if (manualDrive) {
				manualMoveController.driveAt(new XY(gamepad1.left_stick_x, -gamepad1.left_stick_y),
				                             gamepad1.right_stick_x * 1.5);
			} else {
				autoMoveController.moveToTarget(time.getSecondsAndReset());
			}
		}
	}
}
