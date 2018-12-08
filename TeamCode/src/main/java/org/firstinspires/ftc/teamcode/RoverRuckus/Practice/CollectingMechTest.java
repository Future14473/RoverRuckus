/*
package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "CollectingMechTest", group = "Test")
public class CollectingMechTest extends OpMode {

    HardwareTestBot robot = new HardwareTestBot();
    HardwareMap hardwareMap;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        if(gamepad1.a){
            robot.Succ.setPower(0.4);
        } else if(gamepad1.b) {
            robot.Succ.setPower(-0.4);
        } else {
            robot.Succ.setPower(0);
        }

        if(gamepad1.dpad_up){
            robot.Pivot.setPower(0.7);
        } else if(gamepad1.dpad_down){
            robot.Pivot.setPower(-0.7);
        } else {
            robot.Pivot.setPower(0);
        }

        if(gamepad1.right_bumper) {
            robot.Pivot2.setPower(0.7);
            //robot.Pivot3.setPower(0.7);
        } else if(gamepad1.left_bumper) {
            robot.Pivot2.setPower(-0.7);
            //robot.Pivot3.setPower(-0.7);
        } else {
            robot.Pivot2.setPower(0);
            //robot.Pivot3.setPower(0);
        }
    }
}*/