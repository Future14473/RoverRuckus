package org.firstinspires.ftc.teamcode.RoverRuckus.simpletests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.HardwareTestBot;

@Autonomous(name = "PutMarkerInDepot", group = "Test")
public class PutMarkerInDepot extends LinearOpMode {
	private HardwareTestBot robot = new HardwareTestBot();
	
	@Override
	public void runOpMode() throws InterruptedException {
		waitForStart();
		while (opModeIsActive()) {
			robot.init(hardwareMap);
			//robot.drive.move(260f, 1f, 2f);
			//robot.drive.turn(90, 0.5f);
			robot.Marker.setPosition(0.84);
			sleep(2000);
			robot.Flicker.setPosition(0.65);
			sleep(5000);
		}
	}
}
