package org.firstinspires.ftc.teamcode.RoverRuckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.MecanumDrive;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GlobalVars;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpModePrinted;

import static java.util.concurrent.TimeUnit.SECONDS;

@TeleOp(group = "test")
@Disabled
public class PIDGyroCalibration extends OurLinearOpModePrinted {
	double p = 0.35, d = 0.13, i = 0;
	private MecanumDrive drive;
	
	private Button a = new Button(() -> gamepad1.a);
	
	private Button b = new Button(() -> gamepad1.b);
	
	private Button up = new Button(() -> gamepad1.dpad_up);
	
	private Button down = new Button(() -> gamepad1.dpad_down);
	
	private Button left = new Button(() -> gamepad1.dpad_left);
	
	private Button right = new Button(() -> gamepad1.dpad_right);
	private MecanumDrive.GyroRotateTask turnTask;
	
	@Override
	protected void initialize() throws InterruptedException {
		robot.initIMU();
		MecanumDrive.Parameters parameters = new MecanumDrive.Parameters();
		parameters.useGyro = true;
		drive = new MecanumDrive(robot, parameters);
		waitUntil(robot.imu::isGyroCalibrated, 3, SECONDS);
	}
	
	@Override
	protected void run() throws InterruptedException {
		while (opModeIsActive()) {
			if (a.pressed()) {
				drive.rotate(34, 1);
			}
			if (b.pressed())
				GlobalVars.shouldComplete = !GlobalVars.shouldComplete;
			if (up.held()) {
				p += 0.0001;
			} else if (down.held()) {
				p -= 0.0001;
			}
			if (right.held()) {
				d += 0.0003;
			} else if (left.held()) {
				d -= 0.0003;
			}
//			GyroRotateTask.pid.setP(p);
//			GyroRotateTask.pid.setD(d);
//			GyroRotateTask.pid.setI(0);
//			GyroRotateTask.pid.setF(0);
			if (turnTask != null) {
				telemetry.addData("CUR POS", robot.getAngle());
//				telemetry.addData("TARG POS", turnTask.targetAngle);
			}
			telemetry.addData("PARAMS:", "P: %.4f, D: %.4f", p, d);
			telemetry.addData("DO COMPLETE:", GlobalVars.shouldComplete);
			telemetry.update();
			
		}
	}
	
	@Override
	protected void cleanup() {
		drive.stop();
		super.cleanup();
	}
}
