package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "TeleOpReal", group = "Real")
public class TeleOpReal extends OpMode {

    HardwareTestBot robot = new HardwareTestBot();
    HardwareMap hwMap;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        if(gamepad2.a){
            robot.Succ.setPower(0.4);
        } else if(gamepad2.b) {
            robot.Succ.setPower(-0.4);
        } else {
            robot.Succ.setPower(0);
        }

        float n;
        n = 1 + gamepad2.right_trigger;

        if(gamepad2.dpad_up){
            robot.Pivot.setPower(n*0.4);
        } else if(gamepad2.dpad_down){
            robot.Pivot.setPower(n*(-0.4));
        } else {
            robot.Pivot.setPower(0);
        }

        if(gamepad2.right_bumper) {
            robot.Pivot2.setPower(n*0.4);
            //robot.Pivot3.setPower(0.4);
        } else if(gamepad2.left_bumper) {
            robot.Pivot2.setPower(n*(-0.4));
            //robot.Pivot3.setPower(-0.7);
        } else {
            robot.Pivot2.setPower(0);
            //robot.Pivot3.setPower(0);
        }
        double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
        double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
        double rightX = gamepad1.right_stick_x;
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        robot.LeftFront.setPower(v1);
        robot.RightFront.setPower(v2);
        robot.LeftBack.setPower(v3);
        robot.RightBack.setPower(v4);
    }
}
