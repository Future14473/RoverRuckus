package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AutonomousNextToCraterReal", group = "Real")
public class AutonomousCase1Real extends LinearOpMode {

    HardwareTestBot robot = new HardwareTestBot();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        waitForStart();
        while(opModeIsActive()) {
            robot.Pivot2.setPower(0.3);
            sleep(500);
            robot.LeftBack.setPower(-0.4);
            robot.RightFront.setPower(-0.4);
            robot.LeftFront.setPower(-0.4);
            robot.RightBack.setPower(-0.4);
            sleep(500);
            robot.LeftFront.setPower(-0.4);
            robot.RightBack.setPower(-0.4);
            sleep(1000);

//            if (robot.detector.getXPosition() > 280) {
//                robot.LeftFront.setPower(0.4);
//                robot.RightBack.setPower(0.4);
//                robot.LeftBack.setPower(-0.4);
//                robot.RightFront.setPower(-0.4);
//            }
//            else if (robot.detector.getXPosition() < 180) {
//                robot.RightFront.setPower(0.4);
//                robot.LeftBack.setPower(0.4);
//                robot.LeftFront.setPower(-0.4);
//                robot.RightBack.setPower(-0.4);
//            }
//            else{
//                break;
//            }
            robot.RightFront.setPower(-0.4);
            robot.LeftBack.setPower(-0.4);
            robot.LeftFront.setPower(-0.4);
            robot.RightBack.setPower(-0.4);
            sleep(1000);

            robot.LeftFront.setPower(0.4);
            robot.LeftBack.setPower(0.4);
            robot.RightBack.setPower(-0.4);
            robot.RightFront.setPower(-0.4);
            sleep(1000);

            robot.RightFront.setPower(-0.4);
            robot.LeftBack.setPower(-0.4);
            robot.LeftFront.setPower(-0.4);
            robot.RightBack.setPower(-0.4);
            sleep(15000);

            robot.Succ.setPower(-1);
            sleep(500);

            robot.RightFront.setPower(0.4);
            robot.LeftBack.setPower(0.4);
            robot.LeftFront.setPower(0.4);
            robot.RightBack.setPower(0.4);
            sleep(5000);

        }

    }
}
