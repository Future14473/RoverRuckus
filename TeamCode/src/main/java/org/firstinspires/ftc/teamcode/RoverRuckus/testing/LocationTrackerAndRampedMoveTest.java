package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.LocationTracker;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDriveBetter;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.RampedMoveController;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.SheetMetalRobot;

import static java.util.concurrent.TimeUnit.SECONDS;

@TeleOp(group = "test")
public class LocationTrackerAndRampedMoveTest extends OurLinearOpMode {
	private final Button               a                    = new Button(() -> gamepad1.a);
	private       SheetMetalRobot      robot;
	private       LocationTracker      locationTracker      =
			new LocationTracker(new MecanumDriveBetter.Parameters().ticksPerUnit);
	private       RampedMoveController rampedMoveController = new RampedMoveController(6);
	
	@Override
	protected void initialize() throws InterruptedException {
		robot = new SheetMetalRobot(hardwareMap);
		robot.initIMU();
		waitUntil(robot::imuInitted, 3, SECONDS);
	}
	
	@Override
	protected void run() {
		robot.wheels.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
		robot.wheels.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		locationTracker.reset(robot.getAngle(), robot.wheels.getCurrentPosition());
		while (opModeIsActive()) {
			MotorSetPower power = rampedMoveController.getPower(
					new XY(gamepad1.left_stick_x, -gamepad1.left_stick_y).scale(2),
					gamepad1.right_stick_x * 2);
			robot.wheels.setPower(power);
			locationTracker.updateLocation(robot.getAngle(),
			                               robot.wheels.getCurrentPosition());
			telemetry.addData("Current location", locationTracker.getCurrentLocation());
			telemetry.addData("Current Angle", Math.toDegrees(locationTracker.getCurrentAngle()));
			telemetry.update();
		}
	}
}
