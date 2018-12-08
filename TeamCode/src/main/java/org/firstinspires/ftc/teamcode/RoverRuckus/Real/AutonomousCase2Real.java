package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Autonomous(name = "AutonomousNextToDepotReal", group = "Real")
public class AutonomousCase2Real extends LinearOpMode{


HardwareTestBot robot = new HardwareTestBot();

@Override
public void runOpMode() throws InterruptedException {
    robot.init(hardwareMap);
    waitForStart();
    while (opModeIsActive()) {
        /*robot.Pivot2.setPower(0.3);
        sleep(500);*/

        robot.LeftBack.setPower(-0.5);
        robot.RightFront.setPower(-0.5);
        robot.LeftFront.setPower(-0.5);
        robot.RightBack.setPower(-0.5);
        sleep(1000);

        robot.Succ.setPower(-0.2);
        robot.LeftBack.setPower(0);
        robot.RightFront.setPower(0);
        robot.LeftFront.setPower(0);
        robot.RightBack.setPower(0);
        sleep(10000);

        robot.Succ.setPower(0);
        stop();
    }
}}