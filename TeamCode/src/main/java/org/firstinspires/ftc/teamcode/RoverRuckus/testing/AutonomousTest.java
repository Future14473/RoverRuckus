package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GoldLooker;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.HardwareTestBot;

@TeleOp(name = "AutonomousTest", group = "Test")
public class AutonomousTest extends LinearOpMode {

    private HardwareTestBot robot = new HardwareTestBot();
    private GoldLooker goldLooker;
    private boolean found = false;

    @Override
    public void runOpMode() {
        telemetry.addLine("Init started...");
        telemetry.addLine("Pls wate thx");
        telemetry.update();
        robot.init(hardwareMap);
        goldLooker = new GoldLooker(hardwareMap);
        robot.Hooke.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.Hooke.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addLine("Init done");
        telemetry.update();
        waitForStart();
        //runto(-32000);
        goldLooker.start();
        telemetry.addLine("Point A");
        telemetry.update();

        robot.drive.move(270, 1, .1);
        robot.drive.move(55, 1, .57); //move forwards
        robot.drive.waitForDone();
        telemetry.addLine("Done initial move");
        telemetry.update();
        int i;
        int look = -1; // -1 means nothing, 0 means white, 1 means gold
        for (i = 1; i >= -1; i--) {// -1 is left, 0 is center, 1 is right position
            telemetry.addData("i is:", i);
            telemetry.addLine("looking...");
            telemetry.update();
            look = closerLook();
            telemetry.addData("look is:", look);
            telemetry.update();
            if (look == 1) { //found gold
                robot.drive.move(0, .5, .3); // move forwards to hit gold
                robot.drive.move(180, .5, .3); // move back
                robot.drive.waitForDone();
                found = true;
                break;
            }

            if (i != -1) {// has not traverse the 3 positions yet
                robot.drive.move(270, 1, 17.0 / 36);
                robot.drive.waitForDone();
            }
        }
        if (!found) {
            telemetry.addData("NOT FOUND", "NOT FOUND");
            telemetry.update();
        }

        //robot.drive.move(90, 1, (1 - i) * 17.0 / 36);

        if (i == 1) {
            robot.drive.move(270, 1, 34 / 36);
        } else if (i == 0) {
            robot.drive.move(270, 1, 17.0 / 36);
        }
        robot.drive.move(260f, 1, 2f);
        robot.drive.turn(80, 0.5f);
        robot.drive.waitForDone();
        robot.Marker.setPosition(0.84);
        sleep(2000);
        robot.Flicker.setPosition(0.65);
        sleep(5000);

        robot.drive.move(0, 1, 2f);
        robot.Arm.setPower(-1);
        sleep(1000);
        robot.Arm.setPower(0);
        robot.Rotation.setPower(1);
        sleep(1000);
        robot.Rotation.setPower(0);
        while (opModeIsActive()) {
        }
    }

    private void runto(int encoder) {
        robot.Hooke.setPower(1);
        //decreasing
        while (Math.abs(robot.Hooke.getCurrentPosition() - encoder) > 100) {
            //wait
        }
        robot.Hooke.setPower(0);
    }

    private int closerLook() {
        int look = -1;
        while(look == -1) {
            if(!robot.drive.hasTasks()) {
                robot.drive.move(0, 0.3f, 2.0 / 36);
                robot.drive.move(180, 0.3f, 2.0 / 36);
                robot.drive.move(270, 0.3f, 2.0 / 36);
                robot.drive.move(0, 0.3f, 2.0 / 36);
                robot.drive.move(180, 0.3f, 2.0 / 36);
                robot.drive.move(90, 0.3f, 4.0 / 36);
                robot.drive.move(0, 0.3f, 2.0 / 36);
            }
            look = goldLooker.look();
        }
        robot.drive.cancelTasks();
        return look;
    }

}
