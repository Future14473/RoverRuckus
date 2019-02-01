package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

/**
 * A {@link MoveTask} that is simply turning all the wheels
 * at a constant power, uniformly, for a constant time.
 */
public class StraightMoveTask extends UniformMoveTask {
	
	StraightMoveTask(MotorSetPower targPower, double mult, double speed) {
		super(targPower, speed, mult);
	}
	
	//read motor positions and adjust them as necessary if they go off track relative to everyone else.
	@Override
	public boolean run(MotorSet motors) {
		updateCurPos(motors);
		setPower(getAvgProgress(), motors);
		return getMaxOff() < motors.getTargetPositionTolerance() * 2;
	}
	
}
