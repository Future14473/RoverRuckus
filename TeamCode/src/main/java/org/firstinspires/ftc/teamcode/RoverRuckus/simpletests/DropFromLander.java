package org.firstinspires.ftc.teamcode.RoverRuckus.simpletests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Robot;

@Autonomous(name = "DropFromLander", group = "Test")
public class DropFromLander extends LinearOpMode {
    Robot robot = new Robot();

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        robot.init(hardwareMap);
        robot.Hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.Hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        runTo(robot.Hooke.getCurrentPosition()-31000,robot.Hooke);

        robot.drive.move(270, 0.5, 0.1);
        robot.drive.move(0, 0.5, 0.1);
        robot.drive.move(90, 0.5, 0.1);

    }
    public boolean runTo(int encoder, DcMotor motor){
        int dir=0;

        if(motor.getCurrentPosition()>encoder)dir=1;
        if(motor.getCurrentPosition()<encoder)dir=-1;
        //dir=1;
        motor.setPower(dir);
        //decreasing
        while(!((-1*dir*(motor.getCurrentPosition()-encoder))>100)){
            //wait

        }
        waitForDone(robot.Hooke);
        motor.setPower(0);

        return true;
    }
    public void waitForDone(DcMotor motor) {
        while (motor.isBusy()) ;
    }
}
