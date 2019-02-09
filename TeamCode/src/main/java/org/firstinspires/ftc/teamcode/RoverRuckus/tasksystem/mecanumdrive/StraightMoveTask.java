package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.mecanumdrive;

import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.MotorSetPower;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.IRobot;

/**
 * A {@link RobotTaskAdapter} that is simply turning all the wheels
 * at a constant power, uniformly, for a constant time.
 */
class StraightMoveTask extends UniformMoveTask {
	
	StraightMoveTask(IRobot robot, MotorSetPower targPower, double mult, double speed) {
		super(robot, targPower, speed, mult);
	}
	
	//read motor positions and adjust them as necessary if they go off track relative to everyone else.
	@Override
	public boolean loop() {
		updateCurPos();
		setPower(getAvgProgress());
		return getMaxOff() < motors.getTargetPositionTolerance() * 2;
	}
	
}
