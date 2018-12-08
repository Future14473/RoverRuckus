
package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "MecanumWheelsTest", group = "Test")
public class MecanumWheelsTest extends OpMode {
    HardwareTestBot robot = new HardwareTestBot();
    boolean a = false;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
    	//replaced with drive Handler.
        float angle = (float)Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x);
        float speed = (float)Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
        float turnRate = gamepad1.right_stick_x/2;
        robot.driveHandler.moveAt(angle,speed,turnRate);
        
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
