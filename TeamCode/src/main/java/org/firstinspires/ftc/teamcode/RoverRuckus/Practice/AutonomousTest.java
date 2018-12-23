package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RoverRuckus.assets.HardwareTestBot;


@Autonomous(name = "AutonomousTest", group = "Test")
public class AutonomousTest extends LinearOpMode{

    HardwareTestBot robot = new HardwareTestBot();
    
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        while(opModeIsActive()) {
            waitForStart();
            DropFromLander();
        }
    }

    public void DropFromLander() {
        robot.Hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.Hooke.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.Hooke.setTargetPosition(1000);
        robot.Hooke.setPower(1);
        while(robot.Hooke.isBusy()) {
            //wait until target position is reached
        }
        robot.Hooke.setPower(0);
        robot.Hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void KnockOffGold(ColorVisionTest c) {
        
    }
}
