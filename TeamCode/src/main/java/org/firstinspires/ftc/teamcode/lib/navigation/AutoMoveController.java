package org.firstinspires.ftc.teamcode.lib.navigation;

import org.firstinspires.ftc.teamcode.lib.robot.IRobot;
import org.firstinspires.ftc.teamcode.lib.robot.MotorSetPower;
import org.firstinspires.ftc.teamcode.lib.timer.Timer;

import static org.firstinspires.ftc.teamcode.config.NavigationConstants.USE_XY_PID;

/**
 * Puts AutoMoveCalculator and RobotLocationTracker together.
 * Records robot position and allows the robot to automatically move to a specified target position
 */
@SuppressWarnings("unused")
public class AutoMoveController {
	
	private final IRobot               robot;
	private final AutoMoveCalculator   autoMoveCalculator;
	private final PositionTracker      positionTracker;
	private final RampedMoveController rampedMoveController;
	private final Timer                timer;
	private       XYR                  targetPosition = XYR.ZERO;
	private       Magnitudes           maxVelocities  = Magnitudes.ZERO;
	
	public AutoMoveController(IRobot robot, PositionTracker positionTracker,
	                          RampedMoveController rampedMoveController,
	                          Timer timer) {
		this.robot = robot;
		this.positionTracker = positionTracker;
		this.rampedMoveController = rampedMoveController;
		this.timer = timer;
		//todo: maybe change.
		autoMoveCalculator =
				USE_XY_PID ? new WorldAxesXYPIDMoveCalc() : new WorldAxesDualPIDMoveCalc();
	}
	
	/**
	 * Sets the target position to the current recorded position.
	 */
	public void setTargetPositionHere() {
		targetPosition = positionTracker.getCurrentPosition();
		updateTargetPosition();
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
	public void moveToTarget() {
		double elapsedTime = timer.getSecondsAndReset();
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
	
	public void setTargetPosition(XYR pos) {
		targetPosition = pos;
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
	private XY getLocationError() {
		return targetPosition.xy.subtract(positionTracker.getCurrentPosition().xy);
	}
	
	private double getAngularError() {
		return targetPosition.angle - positionTracker.getCurrentPosition().angle;
	}
	
	//
//	public void setTranslationPIDCoefficients(PIDCoefficients coefficients) {
//		autoMoveCalculator.setTranslationPIDCoefficients(coefficients);
//	}
//
//	public void setAngularPIDCoefficients(PIDCoefficients coefficients) {
//		autoMoveCalculator.setAngularPIDCoefficients(coefficients);
//	}
	
	public XYR getCurrentPosition() {
		return positionTracker.getCurrentPosition();
	}
	
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
	
	public void resetInternalTimer() {
		timer.reset();
	}
	
	public void stopRobot() {
		robot.getWheels().stop();
	}
	
	private static double modAngleTowards(double currentAngle, double targetAngle) {
		return Math.round((targetAngle - currentAngle) / (2 * Math.PI)) * 2 * Math.PI +
		       currentAngle;
	}
}
