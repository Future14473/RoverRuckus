package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.CycleTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.DEFAULT_MAX_ACCELERATIONS;

/**
 * A wrapper around a RampedMoveController with utilities to facilitate direct gamepad to robot
 * input.
 */
public class ManualMoveController {
	
	private final RampedMoveController rampedMoveController;
	private final IRobot               robot;
	private final CycleTime            time = new CycleTime();
	
	public ManualMoveController(
			IRobot robot, RampedMoveController rampedMoveController) {
		this.rampedMoveController = rampedMoveController;
		this.robot = robot;
	}
	
	public ManualMoveController(IRobot robot, Magnitudes maxAccelerations) {
		this(robot, new RampedMoveController(maxAccelerations));
	}
	
	public ManualMoveController(IRobot robot) {
		this(robot, new RampedMoveController(DEFAULT_MAX_ACCELERATIONS));
	}
	
	public void driveAt(XY moveRate, double turnRate, double translationalVelocity,
	                    double angularVelocity) {
		robot.getWheels().setPower(
				rampedMoveController.getPower(new XYR(moveRate, turnRate),
				                              new Magnitudes(translationalVelocity,
				                                             angularVelocity),
				                              time.getSecondsAndReset()));
	}
	
}
