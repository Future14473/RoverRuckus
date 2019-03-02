package org.firstinspires.ftc.teamcode.lib.navigation;

import org.firstinspires.ftc.teamcode.lib.robot.IRobot;
import org.firstinspires.ftc.teamcode.lib.timer.Timer;
import org.firstinspires.ftc.teamcode.lib.timer.SingleTimer;

import static org.firstinspires.ftc.teamcode.config.NavigationConstants.DEFAULT_MAX_ACCELERATIONS;

/**
 * A wrapper around a RampedMoveController with utilities to facilitate direct gamepad to robot
 * input.
 */
public class ManualMoveController {
	
	private final RampedMoveController rampedMoveController;
	private final IRobot               robot;
	private final Timer                timer;
	
	public ManualMoveController(IRobot robot, RampedMoveController rampedMoveController,
	                            Timer timer) {
		this.rampedMoveController = rampedMoveController;
		this.robot = robot;
		this.timer = timer;
	}
	
	public ManualMoveController(IRobot robot, Magnitudes maxAccelerations) {
		this(robot, new RampedMoveController(maxAccelerations), new SingleTimer());
	}
	
	public ManualMoveController(IRobot robot) {
		this(robot, new RampedMoveController(DEFAULT_MAX_ACCELERATIONS),
		     new SingleTimer());
	}
	
	public void driveAt(XY moveRate, double turnRate) {
		robot.getWheels()
		     .setPower(rampedMoveController.getPower(new XYR(moveRate, turnRate),
		                                             timer.getSecondsAndReset()));
	}
}
