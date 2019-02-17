package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.LocationTracker;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDriveBetter;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.SheetMetalRobot;

@TeleOp(group = "test")
public class LocationTrackerTest extends OurLinearOpMode {
	private SheetMetalRobot robot;
	private LocationTracker locationTracker =
			new LocationTracker(new MecanumDriveBetter.Parameters().ticksPerUnit);
	
	@Override
	protected void initialize() {
		robot = new SheetMetalRobot(hardwareMap);
	}
	
	@Override
	protected void run() {
		while (opModeIsActive()) {
			locationTracker.updateLocation(robot.getAngle(),
			                               robot.getWheels().getCurrentPosition());
			telemetry.addData("Current location", locationTracker.getCurrentLocation());
			telemetry.addData("Current Angle", locationTracker.getCurrentAngle());
		}
	}
}
