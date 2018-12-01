package org.firstinspires.ftc.teamcode.RoverRuckus.Practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "ShooterTest", group = "Test")
public class ShooterTest extends OpMode {

    DcMotor shooter;

    @Override
    public void init() {
        shooter = hardwareMap.get(DcMotor.class, "shooter");
    }

    @Override
    public void loop() {
        if(gamepad1.b) {
            shooter.setPower(1);
        } else {
            shooter.setPower(0);
        }
    }
}
