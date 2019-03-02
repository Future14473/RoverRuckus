package org.firstinspires.ftc.teamcode.RoverRuckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.lib.navigation.ManualMoveController;
import org.firstinspires.ftc.teamcode.lib.navigation.PositionTracker;
import org.firstinspires.ftc.teamcode.lib.navigation.XY;
import org.firstinspires.ftc.teamcode.lib.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.lib.robot.CurRobot;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.firstinspires.ftc.teamcode.config.NavigationConstants.ENCODER_TICKS_PER_INCH;

@TeleOp(group = "test")
@Disabled
public class LocationTrackerAndRampedMoveTest extends OurLinearOpMode {
	private CurRobot             robot;
	private PositionTracker      positionTracker;
	private ManualMoveController manualMoveController;
	
	@Override
	protected void initialize() throws InterruptedException {
		robot = new CurRobot(hardwareMap);
		positionTracker = new PositionTracker(ENCODER_TICKS_PER_INCH);
		manualMoveController = new ManualMoveController(robot);
		robot.initIMU();
		waitUntil(robot::imuIsGyroCalibrated, 3, SECONDS);
	}
	
	@Override
	protected void run() {
		robot.wheels.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
		robot.wheels.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		positionTracker.reset();
		while (opModeIsActive()) {
			manualMoveController.driveAt(
					new XY(gamepad1.left_stick_x, -gamepad1.left_stick_y).scale(2),
					gamepad1.right_stick_x * 2);
			positionTracker.updateLocation(robot);
			telemetry.addData("Current Position: ", positionTracker.getCurrentPosition());
			telemetry.update();
		}
	}
}
