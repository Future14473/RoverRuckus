package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(name = "Moving Test", group = "test")
public class SimpleForward extends OpMode {
	private Robot robot;
	
	public void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {
		}
	}
	@Override
	public void start() {
		robot.wheels.get(0).setPower(1);
		robot.wheels.get(1).setPower(1);
		sleep(1000);
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
