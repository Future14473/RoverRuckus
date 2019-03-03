package org.firstinspires.ftc.teamcode.ruckus.real;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.config.TeleopAndAutoConstants;
import org.firstinspires.ftc.teamcode.lib.opmode.Button;
import org.firstinspires.ftc.teamcode.lib.opmode.LimitedMotor;
import org.firstinspires.ftc.teamcode.lib.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.lib.robot.CurRobot;

import static org.firstinspires.ftc.teamcode.config.TeleopAndAutoConstants.*;

@TeleOp(name = "Reset Hook", group = "reset")
public class ResetHook extends OurLinearOpMode {
	private Button       a    = new Button(() -> gamepad1.a);
	private Button       up   = new Button(() -> gamepad1.dpad_up);
	private Button       down = new Button(() -> gamepad1.dpad_down);
	private LimitedMotor hook;
	private Button       y    = new Button(() -> gamepad1.y);
	
	@Override
	public void initialize() {
		CurRobot robot = new CurRobot(hardwareMap);
		hook = new LimitedMotor(robot.hook,
		                        0, HOOK_INITIAL, true);
	}
	
	@Override
	public void run() {
		while (opModeIsActive()) {
			hook.setPowerLimited(up.down() ? 0.4 : down.down() ? -0.6 : 0, true);
			telemetry.addData("Current Position:", hook.getLastPosition());
			if (y.pressed()) {
				hook.resetEncoder();
				while (hook.getLastState() != LimitedMotor.State.UPPER) {
					hook.setPowerLimited(0.2, false);
					idle();
				}
				break;
			}
		}
	}
}
