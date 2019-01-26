package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.*;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.Button.State.*;

@TeleOp(name = "The Teleop")
public class ActualTeleOp extends OpMode {
	Button gp1y = new Button(() -> gamepad1.y);
	private Robot robot;
	private boolean reverseDrive = false;
	
	@Override
	public void init() {
		robot = new Robot(hardwareMap);
		robot.wheels.setModeTo(RUN_USING_ENCODER);
	}
	
	@Override
	public void loop() {
		/*-----------------*\
		 |    GAMEPAD 1    |
		\* ----------------*/
		
		//MOVING ROBOT
		double speedMult = gamepad1.right_bumper ? 10 : (gamepad1.left_bumper ? 1.0 / 3 : 1);
		
		double direction = Math.atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y);
		double turnRate = gamepad1.right_stick_x * speedMult;
		double speed = Math.pow(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1.7) * speedMult;
		if (gp1y.getState() == PRESSED) reverseDrive = !reverseDrive;
		if (reverseDrive) direction += Math.PI;
		robot.moveAt(direction, turnRate, speed);
		telemetry.addData("DIRECTION", reverseDrive ? "HOOK FRONT" : "ARM FRONT");
		/*-----------------*\
		 |    GAMEPAD 2    |
		\* ----------------*/
	}
}
