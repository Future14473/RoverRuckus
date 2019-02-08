package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(group = "test")
@Disabled
public class MovingTest extends OpMode {
	private Robot robot;
	
	private void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ignored) {
		}
	}
	
	@Override
	public void start() {
		robot.wheels.get(0).setPower(1);
		robot.wheels.get(1).setPower(1);
		sleep();
		robot.wheels.stop();
	}
	
	@Override
	public void init() {
		robot = new Robot(hardwareMap);
	}
	
	@Override
	public void loop() {
	
	}
	
	@Override
	public void stop() {
	}
}
