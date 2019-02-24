package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.Task;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

/**
 * (temporary) adapter that works with the old mecanum drive.
 */
public class MecanumDriveAdapter extends MecanumDriveBetter {
	
	public MecanumDriveAdapter(IRobot robot) {
		super(robot, new Parameters());
	}
	
	public MecanumDriveAdapter moveXY(double x, double y, double speed) {
		return (MecanumDriveAdapter) goMove(new XY(x, y).scale(1d / 36), speed);
	}
	
	public MecanumDriveAdapter rotate(double degreesToTurn, double speed) {
		goTurn(degreesToTurn, DEGREES, speed);
		return this;
	}
	
	@Override
	public MecanumDriveAdapter then(Task task) {
		return (MecanumDriveAdapter) super.then(task);
	}
}
