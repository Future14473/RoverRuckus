package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDriveBetter;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.CurRobot;

public class MecDriveBetterTest extends OurLinearOpMode {
	private CurRobot           robot;
	private MecanumDriveBetter drive;
	private Button             a = new Button(() -> gamepad1.a);
	private Button             b = new Button(() -> gamepad1.b);
	
	@Override
	protected void initialize() throws InterruptedException {
		robot = new CurRobot(hardwareMap);
		robot.initIMU();
		drive = new MecanumDriveBetter(robot, new MecanumDriveBetter.Parameters());
	}
	
	@Override
	protected void run() throws InterruptedException {
		while (opModeIsActive()) {
			if (a.pressed()) {
				drive.goMove(20, 0, 0.8)
				     .goMove(0, 20, 0.8, true)
				     .goMove(-20, -30, 0.8)
				     .move(20, 10, 0.8).turn(90, AngleUnit.DEGREES, 0.8).go(false)
				     .turn(-90, AngleUnit.DEGREES, 0.5).move(-20, 0, 0.4).go(true);
			}
			if (b.pressed()) {
				drive.goMove(-40, 0, 1)
				     .turn(-45, AngleUnit.DEGREES, 0.8).move(-20, -0.4, 0.7).go(false)
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
