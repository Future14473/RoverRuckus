package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import org.firstinspires.ftc.teamcode.RoverRuckus.Constants;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;

/**
 * Puts TargetLocationAlgorithm and RobotLocationTracker together, storing
 * and updating target location and angles and moving robot motors
 * to get there.
 */
@SuppressWarnings("unused")
public class AutoMoveController {
	
	private final TargetLocationAlgorithm targetLocationAlgorithm;
	private final LocationTracker         locationTracker;
	private final IRobot                  robot;
	private       double                  targetAngle    = 0;
	private       XY                      targetLocation = XY.ZERO;
	
	public AutoMoveController(IRobot robot, double ticksPerUnit, double maxAngularAcceleration,
	                          double maxTranslationalAcceleration) {
		this.robot = robot;
		targetLocationAlgorithm =
				Constants.USE_XY_PID ?
				new PIDXYTargetLocationAlgorithm(maxAngularAcceleration,
				                                 maxTranslationalAcceleration) :
				new DualPIDTargetLocationAlgorithm(maxAngularAcceleration,
				                                   maxTranslationalAcceleration);
		locationTracker = new LocationTracker(ticksPerUnit);
	}
	
	public AutoMoveController(IRobot robot, double ticksPerUnit, double maxAcceleration) {
		this.robot = robot;
		targetLocationAlgorithm = new DualPIDTargetLocationAlgorithm(maxAcceleration);
		locationTracker = new LocationTracker(ticksPerUnit);
	}
	
	public void reset() {
		locationTracker.reset();
		targetLocation = XY.ZERO;
		targetAngle = locationTracker.getCurrentAngle();
	}
	
	public void updateLocation() {
		double angle = robot.getAngle();
		locationTracker.updateLocation(angle,
		                               robot.getWheels().getCurrentPosition());
		if (Math.abs(targetAngle - angle) > Math.PI)
			this.targetAngle =
					Math.round((angle - targetAngle) / (2 * Math.PI)) * 2 * Math.PI +
					targetAngle;
	}
	
	public void moveToTarget(double maxAngularSpeed, double maxTranslationSpeed) {
		robot.getWheels().setPower(targetLocationAlgorithm.getPower(
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
		this.targetAngle += Math.toRadians(toAdd);
	}
	
	public XY getTargetLocation() {
		return targetLocation;
	}
	
	public void addToTargetLocation(XY toAdd) {
		this.targetLocation = this.targetLocation.add(toAdd);
	}
	
	public void setTargetAngle(double setAngle) {
		setAngle = Math.toRadians(setAngle);
		
		this.targetAngle =
				Math.round((this.targetAngle - setAngle) / (2 * Math.PI)) * 2 * Math.PI +
				setAngle;
	}
	
	public void setTargetLocation(XY targetLocation) {
		this.targetLocation = targetLocation;
	}
	
	public XY getLocationError() {
		return targetLocation.subtract(locationTracker.getCurrentLocation());
	}
	
	public double getAngularError() {
		return targetAngle - locationTracker.getCurrentAngle();
	}
}
