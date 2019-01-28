package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.ModifiedLinearOpMode;

@TeleOp(group = "test")
@Disabled
public class IMUTest extends ModifiedLinearOpMode {
	private Button gp1a = new Button(() -> gamepad1.a);
	private BNO055IMU imu;
	
	@Override
	protected void runOpMode() throws InterruptedException {
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
		waitForStart();
		
		while (opModeIsActive()) {
			telemetry.addData("ANGULAR ORIENTATION:",
					imu.getAngularOrientation().toAxesOrder(AxesOrder.ZYX).toString());
			telemetry.update();
		}
	}
	
	@Override
	protected void cleanup() {
		imu.close();
	}
}