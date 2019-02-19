package org.firstinspires.ftc.teamcode.RoverRuckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDrive;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.PrintedRobot;

@TeleOp(group = "test")
@Disabled
public class GyroTurnTest extends OurLinearOpMode {
	private PrintedRobot robot;
	private MecanumDrive drive;
	
	private Button gp1a = new Button(() -> gamepad1.a);
	
	@Override
	protected void initialize() {
		robot = new PrintedRobot(hardwareMap);
		robot.initIMU();
		drive = new MecanumDrive(robot, new MecanumDrive.Parameters());
	}
	
	@Override
	protected void run() {
		while (opModeIsActive()) {
			if (gp1a.pressed()) {
				drive.rotate(180, 0.7);
			}
			double speed = 0.05;
			telemetry.addData("SPEED", speed);
			telemetry.addData("IS DONE", drive.isDone());
			telemetry.update();
		}
	}
}
