package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.USE_XY_PID;

/**
 * Puts TargetLocationAlgorithm and RobotLocationTracker together, storing
 * and updating target location and angles and moving robot motors
 * to get there.
 */
public class AutoMoveController {
	private static final double MIN_POWER = 0.02;
	
	private final IRobot                  robot;
	private final TargetLocationAlgorithm targetLocationAlgorithm;
	private final PositionTracker         positionTracker;
	private final RampedMoveController    rampedMoveController;
	private       XYR                     targetPosition = XYR.ZERO;
	
	public AutoMoveController(IRobot robot, PositionTracker positionTracker,
	                          RampedMoveController rampedMoveController) {
		this.robot = robot;
		this.positionTracker = positionTracker;
		this.rampedMoveController = rampedMoveController;
		targetLocationAlgorithm = USE_XY_PID ?
		                          new AxesXYPIDTargLocAlg() :
		                          new AxesDualPIDTargLocAlg();
	}
	
	public void reset() {
		positionTracker.reset();
		targetPosition = positionTracker.getCurrentPosition();
	}
	
	public void updateLocation() {
		double curAngle = robot.getAngle();
		positionTracker.updateLocation(curAngle,
		                               robot.getWheels().getCurrentPosition());
		targetPosition =
				targetPosition.withNewAngle(
						XYR.modAngleTowards(targetPosition.angle, curAngle));
	}
	
	public void moveToTarget(double maxAngularSpeed, double maxTranslationSpeed,
	                         double elapsedTime) {
		MotorSetPower power = targetLocationAlgorithm.getPower(
				targetPosition, positionTracker.getCurrentPosition(),
				maxAngularSpeed, maxTranslationSpeed, elapsedTime);
		if (power.getMaxPower() < MIN_POWER) robot.getWheels().stop();
		robot.getWheels().setPower(power);
	}
	
	public boolean isOnTarget(double translationalTolerance, double angularTolerance) {
		return (translationalTolerance <= 0 ||
		        getLocationError().magnitude() < translationalTolerance) &&
		       (angularTolerance <= 0 ||
		        Math.abs(getAngularError()) < angularTolerance);
	}
	
	public void updateAndMove(double maxAngularSpeed, double maxTranslationalSpeed,
	                          double elapsedTime) {
		updateLocation();
		moveToTarget(maxAngularSpeed, maxTranslationalSpeed, elapsedTime);
	}
	
	public void addToTargetPosition(XYR toAdd) {
		targetPosition = targetPosition.add(toAdd);
	}
	
	public XYR getTargetPosition() {
		return targetPosition;
	}
	
	//
//	public void setTargetAngle(double setAngle) {
//		setAngle = Math.toRadians(setAngle);
//
//		this.targetPosition.angle =
//				Math.round((this.targetPosition.angle - setAngle) / (2 * Math.PI)) * 2 * Math.PI +
//				setAngle;
//	}
//
//	public void setTargetLocation(XY targetLocation) {
//		this.targetLocation = targetLocation;
//	}
//
	public XY getLocationError() {
		return targetPosition.xy.subtract(positionTracker.getCurrentPosition().xy);
	}
	
	public double getAngularError() {
		return targetPosition.angle - positionTracker.getCurrentPosition().angle;
	}
	
	public XYR getError() {
		return targetPosition.subtract(positionTracker.getCurrentPosition());
	}
	
	public XYR getCurrentPosition() {
		return positionTracker.getCurrentPosition();
	}
//
//	public void setTranslationPIDCoefficients(PIDCoefficients coefficients) {
//		targetLocationAlgorithm.setTranslationPIDCoefficients(coefficients);
//	}
//
//	public void setAngularPIDCoefficients(PIDCoefficients coefficients) {
//		targetLocationAlgorithm.setAngularPIDCoefficients(coefficients);
//	}
	
	public void addToTargetLocation(XY XY) {
		this.targetPosition =
				targetPosition.addToXY(XY);
	}
	
	public void addToTargetAngle(double angle) {
		targetPosition = targetPosition.addToAngle(angle);
	}
}
