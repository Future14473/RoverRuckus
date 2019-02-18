package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;

/**
 * Puts PIDMoveCalculator and RobotLocationTracker together, storing
 * and updating target location and angles and moving robot motors
 * to get there.
 */
public class RobotMoveController {
	
	private final PIDMoveCalculator pidMoveCalculator;
	private final LocationTracker   locationTracker;
	private final IRobot            robot;
	private       double            targetAngle    = 0;
	private       XY                targetLocation = XY.ZERO;
	
	public RobotMoveController(IRobot robot, double ticksPerUnit, double maxAngularAcceleration,
	                           double maxTranslationalAcceleration) {
		this.robot = robot;
		pidMoveCalculator =
				new PIDMoveCalculator(maxAngularAcceleration, maxTranslationalAcceleration);
		locationTracker = new LocationTracker(ticksPerUnit);
	}
	
	public RobotMoveController(IRobot robot, double ticksPerUnit, double maxAcceleration) {
		this.robot = robot;
		pidMoveCalculator = new PIDMoveCalculator(maxAcceleration);
		locationTracker = new LocationTracker(ticksPerUnit);
	}
	
	public void reset() {
		locationTracker.reset();
		targetLocation = XY.ZERO;
	}
	
	public void updateLocation() {
		locationTracker.updateLocation(robot.getAngle(),
		                               robot.getWheels().getCurrentPosition());
	}
	
	public void moveToTarget(double maxAngularSpeed, double maxTranslationSpeed) {
		robot.getWheels().setPower(pidMoveCalculator.getPower(
				targetLocation,
				locationTracker.getCurrentLocation(),
				targetAngle,
				locationTracker.getCurrentAngle(),
				maxAngularSpeed, maxTranslationSpeed));
		
	}
	
	public void updateAndMove(double maxAngularSpeed, double maxTranslationalSpeed) {
		updateLocation();
		moveToTarget(maxAngularSpeed, maxTranslationalSpeed);
	}
	
	public double getTargetAngle() {
		return targetAngle;
	}
	
	public void addToTargetAngle(double toAdd) {
		this.targetAngle += toAdd;
	}
	
	public XY getTargetLocation() {
		return targetLocation;
	}
	
	public void addToTargetLocation(XY toAdd) {
		this.targetLocation = this.targetLocation.add(toAdd);
	}
	
	public XY getLocationError() {
		return targetLocation.subtract(locationTracker.getCurrentLocation());
	}
	
	public double getAngularError() {
		return targetAngle - locationTracker.getCurrentAngle();
	}
}
