package org.firstinspires.ftc.teamcode.RoverRuckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.Button;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.RUN_TO_POSITION_PIDF;
import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.RUN_USING_ENCODER_PIDF;

@TeleOp(group = "test")
@Disabled
public class ToTargetPositionTest extends OpMode {
	private int       targPos = 0;
	private Button    up      = new Button(() -> gamepad1.dpad_up);
	private Button    down    = new Button(() -> gamepad1.dpad_down);
	private DcMotorEx motorEx;
	
	@Override
	public void init() {
		motorEx = hardwareMap.get(DcMotorEx.class, "CollectArm");
		motorEx.setPIDFCoefficients(RUN_USING_ENCODER, RUN_USING_ENCODER_PIDF);
		motorEx.setPIDFCoefficients(RUN_TO_POSITION, RUN_TO_POSITION_PIDF);
		motorEx.setMode(RUN_TO_POSITION);
	}
	
	@Override
	public void loop() {
		if (up.pressed()) targPos += 10;
		if (down.pressed()) targPos -= 10;
		motorEx.setPower(-0.5);
		motorEx.setTargetPosition(targPos);
		telemetry.addData("TargPos:", targPos);
		telemetry.addData("CurPos:", motorEx.getCurrentPosition());
	}
}
