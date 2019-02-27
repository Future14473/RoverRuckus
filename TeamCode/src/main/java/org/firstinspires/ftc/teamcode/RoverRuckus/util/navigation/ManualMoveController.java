package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.timer.SimpleTimer;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.timer.SingleSimpleTimer;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.DEFAULT_MAX_ACCELERATIONS;

/**
 * A wrapper around a RampedMoveController with utilities to facilitate direct gamepad to robot
 * input.
 */
public class ManualMoveController {
	
	private final RampedMoveController rampedMoveController;
	private final IRobot               robot;
	private final SimpleTimer          timer;
	
	public ManualMoveController(IRobot robot, RampedMoveController rampedMoveController,
	                            SimpleTimer timer) {
		this.rampedMoveController = rampedMoveController;
		this.robot = robot;
		this.timer = timer;
	}
	
	public ManualMoveController(IRobot robot, Magnitudes maxAccelerations) {
		this(robot, new RampedMoveController(maxAccelerations), new SingleSimpleTimer());
	}
	
	public ManualMoveController(IRobot robot) {
		this(robot, new RampedMoveController(DEFAULT_MAX_ACCELERATIONS),
		     new SingleSimpleTimer());
	}
	
	public void driveAt(XY moveRate, double turnRate) {
		robot.getWheels()
		     .setPower(rampedMoveController.getPower(new XYR(moveRate, turnRate),
		                                             timer.getSecondsAndReset()));
	}
}
