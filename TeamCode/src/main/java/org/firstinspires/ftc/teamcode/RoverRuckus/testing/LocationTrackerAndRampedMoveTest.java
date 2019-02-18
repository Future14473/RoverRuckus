package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.Constants;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.LocationTracker;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.RampedMoveController;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.SheetMetalRobot;

import static java.util.concurrent.TimeUnit.SECONDS;

@TeleOp(group = "test")
public class LocationTrackerAndRampedMoveTest extends OurLinearOpMode {
	private SheetMetalRobot      robot;
	private LocationTracker      locationTracker;
	private RampedMoveController rampedMoveController =
			new RampedMoveController(Constants.DEFAULT_MAX_ACCELERATION);
	
	@Override
	protected void initialize() throws InterruptedException {
		robot = new SheetMetalRobot(hardwareMap);
		locationTracker = new LocationTracker(Constants.ENCODER_TICKS_PER_INCH);
		robot.initIMU();
		waitUntil(robot::imuInitted, 3, SECONDS);
	}
	
	@Override
	protected void run() {
		robot.wheels.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
		robot.wheels.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		locationTracker.reset();
		while (opModeIsActive()) {
			MotorSetPower power = rampedMoveController.getPower(
					new XY(gamepad1.left_stick_x, -gamepad1.left_stick_y).scale(2),
					gamepad1.right_stick_x * 2);
			robot.wheels.setPower(power);
			locationTracker.updateLocation(robot.getAngle(),
			                               robot.getWheels().getCurrentPosition());
			telemetry.addData("Current location", locationTracker.getCurrentLocation());
			telemetry.addData("Current Angle", Math.toDegrees(locationTracker.getCurrentAngle()));
			telemetry.update();
		}
	}
}
