package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(name = "THE ACTUAL TELEOP", group = "teleop")
public class TeleOpReal extends OpMode {
	private Robot robot = new Robot();
	private int yay = 0;
	private boolean pastGamepad1y;
	private boolean pastGamepad2a, pastGamepad2x;
	private boolean reverseDrive = false;
	
	@Override
	public void init() {
		robot.init(hardwareMap);
		//FOR FUN, sounds
		yay = hardwareMap.appContext.getResources().getIdentifier("yay", "raw",
				hardwareMap.appContext.getPackageName());
		if (yay != 0) SoundPlayer.getInstance().preload(hardwareMap.appContext, yay);
		SoundPlayer.getInstance().setMasterVolume(1);
	}
	
	
	@Override
	public void loop() {
		//GAMEPAD 1
		//REVERSE
		if (gamepad1.y && !pastGamepad1y) {
			reverseDrive = !reverseDrive;
		}
		pastGamepad1y = gamepad1.y;
		//MOVE BOT
		double speedMult = gamepad1.right_bumper ? 10 : (gamepad1.left_bumper ? 1.0 / 3 : 1);
		double angle = Math.atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y);
		if (reverseDrive) angle += Math.PI;
		double speed = Math.pow(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1.7) * speedMult;
		double turnRate = gamepad1.right_stick_x * speedMult * 2; //prioritize turning over moving.
		robot.drive.moveAt(angle, speed, turnRate);
		telemetry.addData("DIRECTION", reverseDrive ? "HOOK FRONT" : "ARM FRONT");
		telemetry.update();
		//TAPE
		if (gamepad1.a) {
			robot.tape.setPower(-1); //out
		} else if (gamepad1.b) {
			robot.tape.setPower(1); //in
		} else {
			robot.tape.setPower(0);
		}
		//HOOK
		if (gamepad2.dpad_down) {
			robot.hooke.setPower(-1); //hook up, robot down
		} else if (gamepad2.dpad_up) {
			robot.hooke.setPower(1); //hook down, robot up
		} else {
			robot.hooke.setPower(0);
		}
		
		//GAMEPAD 2
		//ARM
		robot.arm.setPower(gamepad2.left_stick_y);
		//ROTATION, WITH LIMITS.
		if (gamepad2.x || !(gamepad2.right_stick_y > 0 && robot.rotation.getCurrentPosition() < 0) //
				&& !(gamepad2.right_stick_y < 0 && robot.rotation.getCurrentPosition() > 5480)) {
			robot.rotation.setPower(gamepad2.right_stick_y / 1.5);
			//negative = knob up, arm up
			//positive = knob down, arm down
		} else {
			robot.rotation.setPower(0);
		}
		//resetting limits.
		if (!gamepad2.x && pastGamepad2x) {
			robot.rotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			robot.rotation.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
		pastGamepad2x = gamepad2.x;
		
		//COLLECTION
		if (gamepad2.left_bumper) {
			robot.collection.setPower(0.8); //in
		} else if (gamepad2.right_bumper) {
			robot.collection.setPower(-0.8); //out
		} else {
			robot.collection.setPower(0);
		}
		//FUN HOUSE
		if (gamepad2.a && !pastGamepad2a) {
			SoundPlayer.getInstance().stopPlayingAll();
			SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, yay);
		}
		pastGamepad2a = gamepad2.a;
	}
}
