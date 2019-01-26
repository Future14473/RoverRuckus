package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.DecoratedOpMode;

import static org.firstinspires.ftc.teamcode.RoverRuckus.util.Button.State.PRESSED;

public class IMUTest extends DecoratedOpMode {
	private Button gp1a = new Button(() -> gamepad1.a);
	
	@Override
	protected void initialize() throws InterruptedException {
		robot.imu.initialize(new BNO055IMU.Parameters() {
			{
				angleUnit = BNO055IMU.AngleUnit.DEGREES;
			}
		});
		robot.imu.startAccelerationIntegration(new Position(), new Velocity(), 10);
	}
	
	@Override
	protected void run() throws InterruptedException {
		if (gp1a.getState() == PRESSED) {
			robot.imu.startAccelerationIntegration(, new Velocity(), 10);
		}
		telemetry.addData("ACCELERATION:", robot.imu.getAcceleration());
		telemetry.addData("VELOCITY:", robot.imu.getVelocity());
		
	}
	
	@Override
	protected void cleanup() {
		robot.imu.close();
	}
}