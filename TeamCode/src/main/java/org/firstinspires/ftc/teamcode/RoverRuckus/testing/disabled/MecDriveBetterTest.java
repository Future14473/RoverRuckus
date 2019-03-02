package org.firstinspires.ftc.teamcode.RoverRuckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.lib.navigation.MecanumDrive;
import org.firstinspires.ftc.teamcode.lib.opmode.Button;
import org.firstinspires.ftc.teamcode.lib.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.lib.robot.CurRobot;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.INCH;

@TeleOp(group = "test")
@Disabled
public class MecDriveBetterTest extends OurLinearOpMode {
	private CurRobot     robot;
	private MecanumDrive drive;
	private Button       a = new Button(() -> gamepad1.a);
	private Button       b = new Button(() -> gamepad1.b);
	
	@Override
	protected void initialize() {
		robot = new CurRobot(hardwareMap);
		robot.initIMU();
		drive = new MecanumDrive(robot, new MecanumDrive.Parameters());
	}
	
	@Override
	protected void run() {
		while (opModeIsActive()) {
			if (a.pressed()) {
				drive.goMove(20, 0, INCH, 0.8)
				     .goMove(0, 20, INCH, 0.8, true)
				     .goMove(-20, -30, 0.8)
				     .move(20, 10, INCH,0.8).turn(90, DEGREES, 0.8).go()
				     .turn(-90, DEGREES, 0.5).move(-20, 0, INCH,0.4).go(true);
			}
			if (b.pressed()) {
				drive.goMove(-40, 0, 1)
				     .turn(-45, DEGREES, 0.8).move(-20, -0.4, 0.7).go(false)
				     .goMove(-10, -50, 1);
			}
			telemetry.addData("Done:", drive.isDone());
		}
	}
	
	@Override
	protected void cleanup() {
		drive.stop();
	}
}
