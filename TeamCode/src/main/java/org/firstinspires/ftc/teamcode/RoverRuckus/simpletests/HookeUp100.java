package org.firstinspires.ftc.teamcode.RoverRuckus.simpletests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.HardwareTestBot;

@Autonomous(name = "Up100" , group = "Test")
public class HookeUp100 extends LinearOpMode {
    HardwareTestBot robot = new HardwareTestBot();
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        robot.init(hardwareMap);

        robot.Hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.Hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        runto(robot.Hooke.getCurrentPosition()-1000,robot.Hooke);
    }

    public boolean runto(int encoder,DcMotor motor){
        int dir=0;

        if(motor.getCurrentPosition()>encoder)dir=1;
        if(motor.getCurrentPosition()<encoder)dir=-1;
    //dir=1;
        motor.setPower(dir);
        //decreasing
        while(!((-1*dir*(motor.getCurrentPosition()-encoder))>100)){
            //wait
        }

        motor.setPower(0);

        return true;
    }
}
