package org.firstinspires.ftc.teamcode.RoverRuckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.lib.navigation.MecanumDrive;
import org.firstinspires.ftc.teamcode.lib.tasks.Task;
import org.firstinspires.ftc.teamcode.lib.opmode.Button;
import org.firstinspires.ftc.teamcode.lib.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.lib.robot.CurRobot;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@TeleOp(group = "test")
@Disabled
public class PIDGyroCalibration extends OurLinearOpMode {
	private static boolean      shouldComplete = true;
	private        double       p              = 0.35;
	private        double       d              = 0.13;
	private        MecanumDrive drive;
	
	private Button a     = new Button(() -> gamepad1.a);
	private Button b     = new Button(() -> gamepad1.b);
	private Button up    = new Button(() -> gamepad1.dpad_up);
	private Button down  = new Button(() -> gamepad1.dpad_down);
	private Button left  = new Button(() -> gamepad1.dpad_left);
	private Button right = new Button(() -> gamepad1.dpad_right);
	
	@Override
	protected void initialize() throws InterruptedException {
		CurRobot robot = new CurRobot(hardwareMap);
		robot.initIMU();
		org.firstinspires.ftc.teamcode.lib.robot.IRobot robot1 = robot;
		drive = new MecanumDrive(robot1, new MecanumDrive.Parameters()) {
			public MecanumDrive moveXY(double x, double y, double speed) {
				return (MecanumDrive) goMove(x * 36, y * 36, speed);
			}
			
			@Override
			public MecanumDrive then(Task task) {
				return (MecanumDrive) super.then(task);
			}
		};
		waitUntil(robot::imuIsGyroCalibrated, 3, SECONDS);
	}
	
	@Override
	protected void run() {
		while (opModeIsActive()) {
			if (a.pressed()) {
				drive.goTurn((double) 45, DEGREES, (double) 1);
			}
			if (b.pressed()) shouldComplete = !shouldComplete;
			if (up.down()) {
				p += 0.0001;
			} else if (down.down()) {
				p -= 0.0001;
			}
			if (right.down()) {
				d += 0.0003;
			} else if (left.down()) {
				d -= 0.0003;
			}
//			GyroRotateTask.pid.setP(p);
//			GyroRotateTask.pid.setD(d);
//			GyroRotateTask.pid.setI(0);
//			GyroRotateTask.pid.setF(0);
			telemetry.addData("PARAMS:", "P: %.4f, D: %.4f", p, d);
			telemetry.addData("DO COMPLETE:", shouldComplete);
			telemetry.update();
			
		}
	}
	
	@Override
	protected void cleanup() {
		drive.stop();
		super.cleanup();
	}
}
