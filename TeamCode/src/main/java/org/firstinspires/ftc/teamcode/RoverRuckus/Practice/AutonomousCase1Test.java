
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
                Robot.leftFront.setPower(0.1);
                Robot.RightBack.setPower(0.1);
                Robot.leftBack.setPower(-0.1);
                Robot.rightFront.setPower(-0.1);
            }
            else if (Robot.detector.getXPosition() < 180) {
                Robot.rightFront.setPower(0.1);
                Robot.leftBack.setPower(0.1);
                Robot.leftFront.setPower(-0.1);
                Robot.RightBack.setPower(-0.1);
            }
            else{
                Robot.leftFront.setPower(0);
                Robot.leftBack.setPower(0);
                Robot.rightFront.setPower(0.0);
                Robot.RightBack.setPower(0.0);
            }

        }
    }
}
*/