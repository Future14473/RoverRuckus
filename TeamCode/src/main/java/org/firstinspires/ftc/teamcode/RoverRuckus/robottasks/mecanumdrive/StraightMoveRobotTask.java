package org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.mecanumdrive;

import org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.MotorSetPower;

/**
 * A {@link RobotTaskAdapter} that is simply turning all the wheels
 * at a constant power, uniformly, for a constant time.
 */
class StraightMoveRobotTask extends UniformMoveRobotTask {
	
	StraightMoveRobotTask(MotorSetPower targPower, double mult, double speed) {
		super(targPower, speed, mult);
	}
	
	//read motor positions and adjust them as necessary if they go off track relative to everyone else.
	@Override
	public boolean loop() {
		updateCurPos();
		setPower(getAvgProgress());
		return getMaxOff() < motors.getTargetPositionTolerance() * 2;
	}
	
}
