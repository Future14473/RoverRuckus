package org.firstinspires.ftc.teamcode.ruckus.testing.disabled;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.lib.opmode.OurLinearOpMode;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@TeleOp(group = "test")
@Disabled
public class IMUTest extends OurLinearOpMode {
	private BNO055IMU imu;
	
	@Override
	protected void initialize() throws InterruptedException {
		imu = hardwareMap.get(BNO055IMU.class, "imu");
		imu.initialize(new BNO055IMU.Parameters() {
			{
				angleUnit = BNO055IMU.AngleUnit.DEGREES;
			}
		});
		telemetry.addLine("Calibrating...");
		telemetry.update();
		waitUntil(() -> imu.isGyroCalibrated() || gamepad1.a);
		telemetry.addLine("Calibration done, waiting for start");
		telemetry.update();
	}
	
	@Override
	protected void run() {
		while (opModeIsActive()) {
			telemetry.addData("ANGULAR ORIENTATION:",
			                  imu.getAngularOrientation().toAngleUnit(DEGREES));
			telemetry.update();
		}
	}
}