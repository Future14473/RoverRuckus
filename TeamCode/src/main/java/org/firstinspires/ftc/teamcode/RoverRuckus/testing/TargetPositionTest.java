package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.Button;

@TeleOp(group = "test")
public class TargetPositionTest extends OpMode {
	private int       targPos = 0;
	private Button    a       = new Button(() -> gamepad1.a);
	private Button    up      = new Button(() -> gamepad1.dpad_up);
	private Button    down    = new Button(() -> gamepad1.dpad_down);
	private DcMotorEx motorEx;
	
	@Override
	public void init() {
		motorEx = hardwareMap.get(DcMotorEx.class, "CollectArm");
		motorEx.setMotorDisable();
		motorEx.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	}
	
	@Override
	public void loop() {
		if (a.pressed()) motorEx.setMotorEnable();
		if (a.released()) motorEx.setMotorDisable();
		if (up.pressed()) targPos += 10;
		if (down.pressed()) targPos += 10;
		motorEx.setTargetPosition(targPos);
		telemetry.addData("TargPos:", targPos);
	}
}
