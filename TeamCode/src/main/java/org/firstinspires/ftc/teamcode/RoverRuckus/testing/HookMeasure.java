package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(name = "Hook Measurement", group = "measure")
@Disabled
public class HookMeasure extends OpMode {
	private Robot robot = new Robot();
	
	@Override
	public void init() {
		robot.init(hardwareMap);
	}
	
	@Override
	public void loop() {
		if (gamepad1.a) {
			robot.hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			robot.hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
		if (gamepad1.x) {
			robot.hooke.setPower(1);
		} else if (gamepad1.y) {
			robot.hooke.setPower(-1);
		} else robot.hooke.setPower(0);
		/*if(gamepad1.b){
			robot.hooke.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			robot.hooke.setTargetPosition(-26000);
			robot.hooke.setPower(1);
			while(robot.hooke.isBusy());
		}*/
		telemetry.addData("Hook pos:", robot.hooke.getCurrentPosition());
	}
}
