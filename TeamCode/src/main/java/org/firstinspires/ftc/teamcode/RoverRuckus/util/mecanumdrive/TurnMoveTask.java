package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

import java.util.function.DoubleSupplier;

import static org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MotorSetPosition.MOVE_MULT;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MotorSetPower.calcPower;

/**
 * A {@link MoveTask} that turns the robot that utilizes a orientation sensor
 * for accurate rotations.
 */
public class TurnMoveTask extends UniformMoveTask {
	private final DoubleSupplier direction;
	private double targAngle;
	private double degreesToTurn;
	
	TurnMoveTask(double degreesToTurn, double speed, DoubleSupplier direction) {
		super(calcPower(0, 0, Math.signum(degreesToTurn)), speed, MOVE_MULT);
		this.direction = direction;
		this.degreesToTurn = degreesToTurn;
	}
	
	@Override
	public void start(MotorSet motors) {
		super.start(motors);
		targAngle = getDirection() + degreesToTurn;
		//RobotLog.v("STARTING TURNMOVETASK...");
		//RobotLog.v("TARG ANGLE: %f", targAngle);
	}
	
	private double getDirection() {
		return -Math.toDegrees(direction.getAsDouble());
	}
	
	@Override
	public boolean run(MotorSet motors) {
		updateCurPos(motors);
		
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
		setPower(getAvgProgress(), motors);
		return Math.abs(curAngle - targAngle) < 10;
	}
	
}
