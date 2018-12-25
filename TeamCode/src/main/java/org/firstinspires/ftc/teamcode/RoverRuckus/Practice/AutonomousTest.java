package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLooker;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.HardwareTestBot;

@Autonomous(name = "AutonomousTest", group = "Test")
public class AutonomousTest extends LinearOpMode {
	
	HardwareTestBot robot = new HardwareTestBot();
	GoldLooker goldLooker;
	
	@Override
	public void runOpMode() {
		goldLooker = new GoldLooker(hardwareMap);
		waitForStart();
	}
	
	public void DropFromLander() {
		robot.Hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		robot.Hooke.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.Hooke.setTargetPosition(1000);
		robot.Hooke.setPower(1);
		while (robot.Hooke.isBusy()) ; //wait until target position is reached
		robot.Hooke.setPower(0);
		robot.Hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	}
	
	public void KnockOffGold() {
	
	}
	
	public void AlignWithPicture() {
	}
	
	public void PutMarkerInDepot() {
	
	}
	
	public void ParkInCrater() {
	
	}
}
