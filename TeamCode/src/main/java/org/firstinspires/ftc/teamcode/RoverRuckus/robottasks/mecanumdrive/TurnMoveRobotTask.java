package org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.mecanumdrive;

import org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.IRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.MotorSetPosition;

import static org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.MotorSetPower.calcPower;

/**
 * A {@link RobotTaskAdapter} that turns the robot that utilizes a orientation sensor
 * for accurate rotations.
 */
class TurnMoveRobotTask extends UniformMoveRobotTask {
	private double targAngle;
	private double degreesToTurn;
	
	TurnMoveRobotTask(double degreesToTurn, double speed) {
		super(calcPower(0, 0, Math.signum(degreesToTurn)), speed, MOVE_MULT);
		this.degreesToTurn = degreesToTurn;
	}
	
	@Override
	public void start(IRobot robot) {
		super.start(robot);
		targAngle = getDirection() + degreesToTurn;
		//RobotLog.v("STARTING TURNMOVETASK...");
		//RobotLog.v("TARG ANGLE: %f", targAngle);
	}
	
	private double getDirection() {
		return -Math.toDegrees(robot.getDirection());
	}
	
	@Override
	public boolean loop() {
		updateCurPos();
		double curAngle = getDirection();
		double angleProgress = 1 - (targAngle - curAngle) / degreesToTurn;
		double targPosMult = getAvgProgress() / angleProgress;
		if (!Double.isFinite(targPosMult) || targPosMult < 0) targPosMult = 1;
		else targPosMult = Math.pow(targPosMult, 0.5) / 2 + 0.5;
		//RobotLog.v("AngleProgress %f", angleProgress);
		//RobotLog.v("TargPosMult: %f", targPosMult);
		MotorSetPosition targPos = getTargPos();
		for (int i = 0; i < 4; i++) {
			targPos.position[i] *= targPosMult;
		}
		motors.setTargetPosition(targPos);
		//RobotLog.v("TARG POS: %s", targPos.toString());
		//RobotLog.v("CUR POS: %s", getCurPos().toString());
		setPower(getAvgProgress());
		return Math.abs(curAngle - targAngle) < 10;
	}
	
}
