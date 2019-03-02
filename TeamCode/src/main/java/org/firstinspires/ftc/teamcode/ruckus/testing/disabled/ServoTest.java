package org.firstinspires.ftc.teamcode.ruckus.testing.disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.lib.robot.CurRobot;

@TeleOp(group = "test")
@Disabled
public class ServoTest extends LinearOpMode {
	
	@Override
	public void runOpMode() {
		
		CurRobot robot = new CurRobot(hardwareMap);
		waitForStart();
		
		robot.markerDoor.setPosition(0.9);
		sleep(1000);
		robot.flicker.setPosition(0.65);
		sleep(1000);
	}
}
