package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.USE_XY_PID;

/**
 * Puts AutoMoveCalculator and RobotLocationTracker together.
 * Records robot position and allows the robot to automatically move to a specified target position
 */
public class AutoMoveController {
	
	private final IRobot               robot;
	private final AutoMoveCalculator   autoMoveCalculator;
	private final PositionTracker      positionTracker;
	private final RampedMoveController rampedMoveController;
	
	private XYR        targetPosition = XYR.ZERO;
	private Magnitudes maxVelocities  = Magnitudes.ZERO;
	
	public AutoMoveController(IRobot robot, PositionTracker positionTracker,
	                          RampedMoveController rampedMoveController) {
		this.robot = robot;
		this.positionTracker = positionTracker;
		this.rampedMoveController = rampedMoveController;
		//todo: maybe change.
		autoMoveCalculator =
				USE_XY_PID ? new WorldAxesXYPIDMoveCalc() : new WorldAxesDualPIDMoveCalc();
	}
	
	/**
	 * Sets the target position to the current recorded position.
	 */
	public void resetTargetPosition() {
		targetPosition = positionTracker.getCurrentPosition();
	}
	
	/** Updates robot location. */
	public void updateLocation() {
		double curAngle = robot.getAngle();
		positionTracker.updateLocation(curAngle, robot.getWheels().getCurrentPosition());
		targetPosition =
				targetPosition.withNewAngle(modAngleTowards(targetPosition.angle, curAngle));
	}
	
	public void setMaxVelocities(Magnitudes maxVelocities) {
		this.maxVelocities = maxVelocities;
		updateMaxVelocities();
	}
	
	/**
	 * Sets motor power to move robot towards target, using the current autoMoveCalculator
	 * with rampedMoveController.
	 */
	public void moveToTarget(double elapsedTime) {
		XYR targetMovement =
				autoMoveCalculator.getMovement(positionTracker.getCurrentPosition(), elapsedTime);
		MotorSetPower power = rampedMoveController.getPower(targetMovement, elapsedTime);
		robot.getWheels().setPower(power);
	}
	
	/**
	 * Returns true if the current and target positions are close enough, determined the given
	 * tolerances parameter for max allowed translational and angular error.
	 */
	public boolean isOnTarget(Magnitudes tolerances) {
		return (tolerances.translational <= 0 ||
		        getLocationError().magnitude() < tolerances.translational) &&
		       (tolerances.angular <= 0 || Math.abs(getAngularError()) < tolerances.angular);
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
	
	public XYR getCurrentPosition() {
		return positionTracker.getCurrentPosition();
	}
	
	//
//	public void setTranslationPIDCoefficients(PIDCoefficients coefficients) {
//		autoMoveCalculator.setTranslationPIDCoefficients(coefficients);
//	}
//
//	public void setAngularPIDCoefficients(PIDCoefficients coefficients) {
//		autoMoveCalculator.setAngularPIDCoefficients(coefficients);
//	}
	
	public void addToTargetPosition(XYR toAdd) {
		targetPosition = targetPosition.add(toAdd);
		updateTargetPosition();
	}
	
	public void addToTargetPositionRelative(XYR toMove) {
		addToTargetPosition(toMove.withNewXY(toMove.xy.rotate(targetPosition.angle)));
	}
	
	public void addToTargetXY(XY xy) {
		this.targetPosition = targetPosition.addToXY(xy);
		updateTargetPosition();
	}
	
	public void addToTargetXYRelative(XY xy) {
		addToTargetXY(xy.rotate(targetPosition.angle));
	}
	
	public void addToTargetAngle(double angle) {
		targetPosition = targetPosition.addToAngle(angle);
		updateTargetPosition();
	}
	
	private void updateTargetPosition() {
		autoMoveCalculator.setTargetPosition(targetPosition);
	}
	
	private void updateMaxVelocities() {
		autoMoveCalculator.setMaxVelocities(maxVelocities);
	}
	
	private static double modAngleTowards(double currentAngle, double targetAngle) {
		return Math.round((targetAngle - currentAngle) / (2 * Math.PI)) * 2 * Math.PI +
		       currentAngle;
	}
}
