package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.DecoratedLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MecanumDrive;

import java.util.function.Supplier;

@Autonomous
public class GyroTurnTest extends DecoratedLinearOpMode {
	private MecanumDrive drive;
	private Button gp1a = new Button(() -> gamepad1.a);
	private double speed = 0.05;
	
	@Override
	protected void initialize() throws InterruptedException {
		Supplier<Orientation> orientation = getOrientationSupplier();
		drive = new MecanumDrive(robot.wheels, orientation);
	}
	
	@Override
	protected void run() throws InterruptedException {
		while (opModeIsActive()) {
			if (gp1a.pressed()) {
				speed = Math.pow(this.speed, 0.5);
				drive.turn(45, speed);
			}
		}
	}
	
	@Override
	protected void cleanup() {
		drive.stop();
	}
}
