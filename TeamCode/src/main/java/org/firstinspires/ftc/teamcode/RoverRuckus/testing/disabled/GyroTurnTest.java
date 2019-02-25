package org.firstinspires.ftc.teamcode.RoverRuckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDriveAdapter;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.CurRobot;

@TeleOp(group = "test")
@Disabled
public class GyroTurnTest extends OurLinearOpMode {
	private CurRobot            robot;
	private MecanumDriveAdapter drive;
	
	private Button gp1a = new Button(() -> gamepad1.a);
	
	@Override
	protected void initialize() {
		robot = new CurRobot(hardwareMap);
		robot.initIMU();
		drive = new MecanumDriveAdapter(robot);
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
