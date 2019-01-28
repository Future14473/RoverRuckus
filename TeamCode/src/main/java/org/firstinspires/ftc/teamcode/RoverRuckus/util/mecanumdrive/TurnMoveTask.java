package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.function.Supplier;

/**
 * A {@link MoveTask} that turns the robot that utilizes a orientation sensor
 * for accurate rotations.
 */
public class TurnMoveTask extends UniformMoveTask {
	private final Supplier<Orientation> theOrientation;
	private double targAngle;
	private double degreesToTurn;
	
	TurnMoveTask(double degreesToTurn, double speed, Supplier<Orientation> theOrientation) {
		super(MotorSetPower.calcPower(0, Math.signum(degreesToTurn), speed), MotorSetPosition.MOVE_MULT, speed);
		this.theOrientation = theOrientation;
		this.degreesToTurn = degreesToTurn;
	}
	
	@Override
	public void start(MotorSet motors) {
		super.start(motors);
		targAngle = getAngle() + degreesToTurn;
	}
	
	@Override
	public boolean run(MotorSet motors) {
		updateCurPos(motors);
		
		float curAngle = getAngle();
		double angleProgress = Range.clip((targAngle - curAngle) / degreesToTurn, 0, 1);
		double targPosMult = getAvgProgress() / angleProgress;
		MotorSetPosition targPos = getTargPos();
		for (int i = 0; i < 4; i++) {
			targPos.position[i] *= targPosMult;
		}
		motors.setTargetPosition(targPos);
		setPower(getAvgProgress(), motors);
		return Math.abs(curAngle - targAngle) < 10;
	}
	
	private float getAngle() {
		return -theOrientation.get().firstAngle;
	}
}
