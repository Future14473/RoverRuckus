package org.firstinspires.ftc.teamcode.RoverRuckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDrive;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.Task;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.CurRobot;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@TeleOp(group = "test")
@Disabled
public class GyroTurnTest extends OurLinearOpMode {
	private CurRobot     robot;
	private MecanumDrive drive;
	
	private Button gp1a = new Button(() -> gamepad1.a);
	
	@Override
	protected void initialize() {
		robot = new CurRobot(hardwareMap);
		robot.initIMU();
		org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot robot1 = robot;
		drive = new MecanumDrive(robot1, new MecanumDrive.Parameters()) {
			public MecanumDrive moveXY(double x, double y, double speed) {
				return (MecanumDrive) goMove(x * 36, y * 36, speed);
			}
			
			@Override
			public MecanumDrive then(Task task) {
				return (MecanumDrive) super.then(task);
			}
		};
	}
	
	@Override
	protected void run() {
		while (opModeIsActive()) {
			if (gp1a.pressed()) {
				drive.goTurn((double) 180, DEGREES, 0.7);
			}
			double speed = 0.05;
			telemetry.addData("SPEED", speed);
			telemetry.addData("IS DONE", drive.isDone());
			telemetry.update();
		}
	}
}
