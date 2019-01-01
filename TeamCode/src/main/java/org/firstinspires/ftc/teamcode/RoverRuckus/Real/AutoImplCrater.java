package org.firstinspires.ftc.teamcode.RoverRuckus.Real;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Test Auto Next to Crater", group = "Autonomous2")
public class AutoImplCrater extends  AbstractAuto{
	@Override
	protected void position() throws InterruptedException {
		robot.drive.turn(45, 10);
		robot.drive.moveXY(-0.2, 0, 10);
	}
}
