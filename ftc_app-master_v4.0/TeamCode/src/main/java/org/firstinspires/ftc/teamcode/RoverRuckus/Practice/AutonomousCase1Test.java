
/*
package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AutonomousCase1Test", group = "Test")
public class AutonomousCase1Test extends LinearOpMode{

    HardwareTestBot Robot = new HardwareTestBot();

    @Override
    public void runOpMode() throws InterruptedException {
        Robot.init(hardwareMap);
        waitForStart();
        while(opModeIsActive()) {
            if (Robot.detector.getXPosition() > 280) {
                Robot.LeftFront.setPower(0.1);
                Robot.RightBack.setPower(0.1);
                Robot.LeftBack.setPower(-0.1);
                Robot.RightFront.setPower(-0.1);
            }
            else if (Robot.detector.getXPosition() < 180) {
                Robot.RightFront.setPower(0.1);
                Robot.LeftBack.setPower(0.1);
                Robot.LeftFront.setPower(-0.1);
                Robot.RightBack.setPower(-0.1);
            }
            else{
                Robot.LeftFront.setPower(0);
                Robot.LeftBack.setPower(0);
                Robot.RightFront.setPower(0.0);
                Robot.RightBack.setPower(0.0);
            }

        }
    }
}
*/