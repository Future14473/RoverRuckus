
package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RoverRuckus.assets.HardwareTestBot;

@TeleOp(name = "MecanumWheelsTest", group = "Test")
public class MecanumWheelsTest extends OpMode {
    HardwareTestBot robot = new HardwareTestBot();
    boolean a = false;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
        double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
        double rightX = gamepad1.right_stick_x;
        final double v1 = r * Math.cos(robotAngle) - rightX;
        final double v2 = r * Math.sin(robotAngle) + rightX;
        final double v3 = r * Math.sin(robotAngle) - rightX;
        final double v4 = r * Math.cos(robotAngle) + rightX;

        robot.leftFront.setPower(v1);
        robot.rightFront.setPower(v2);
        robot.leftBack.setPower(v3);
        robot.rightBack.setPower(v4);
        telemetry.update();
        robot.Hooke.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if(gamepad1.dpad_up){
            robot.Hooke.setPower(1);
            telemetry.addData("current position",robot.Hooke.getCurrentPosition());
            telemetry.update();
        } else if(gamepad1.dpad_down) {
            robot.Hooke.setPower(-1);
            telemetry.addData("current position",robot.Hooke.getCurrentPosition());
            telemetry.update();
        } else {
            robot.Hooke.setPower(0);
        }


        if(gamepad1.right_bumper) {
            a = true;
        }

        while(a) {
            robot.Hooke.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.Hooke.setTargetPosition(35500);
            robot.Hooke.setPower(1);
            if(robot.Hooke.getCurrentPosition()>35500 || gamepad1.x){
                robot.Hooke.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                robot.Hooke.setPower(0);
                a = false;
                break;
            }
            telemetry.addData("current position",robot.Hooke.getCurrentPosition());
            telemetry.update();
        }

        /*if(gamepad2.dpad_up) {robot.Arm.setPower(1);}
        else if(gamepad2.dpad_down) {robot.Arm.setPower(-1);}
        else {robot.Arm.setPower(0);}*/
    }
}
