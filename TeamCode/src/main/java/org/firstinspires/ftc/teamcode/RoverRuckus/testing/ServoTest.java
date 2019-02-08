package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@TeleOp(group = "test")
@Disabled
public class ServoTest extends LinearOpMode {
	
	@Override
	public void runOpMode() {
		
		Robot robot = new Robot(hardwareMap);
		waitForStart();
		
		robot.markerDoor.setPosition(0.9);
		sleep(1000);
		robot.flicker.setPosition(0.65);
		sleep(1000);
	}
}
