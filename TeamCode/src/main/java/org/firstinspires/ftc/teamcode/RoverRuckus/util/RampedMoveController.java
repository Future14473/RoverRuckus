package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.Constants;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

@SuppressWarnings("WeakerAccess")
public class RampedMoveController {
	
	private final double      maxAngularAcceleration;
	private final double      maxTranslationalAcceleration;
	private final ElapsedTime elapsedTime = new ElapsedTime();
	private       double      rightRate   = 0;
	private       double      leftRate    = 0;
	private       double      turnRate    = 0;
	
	public RampedMoveController(double maxAcceleration) {
		this(maxAcceleration, maxAcceleration);
	}
	
	/**
	 * Creates a new ramped move controller with the given parameters:
	 *
	 * @param maxTranslationalAcceleration the maximum magnitude of the difference in power-level
	 *                                     pseudo-velocity vector per second. If you don't
	 *                                     understand that, that's fine, I almost didn't either,
	 *                                     just plug in a similar value to maxAngularAcceleration
	 * @param maxAngularAcceleration       The maximum difference in power level per second due to
	 *                                     turning.
	 * @implNote The actual difference in power levels may be a combination of the above two.
	 * 		unless some genius comes along and does some math to calibrate/adjust the two into one
	 * 		parameter without making the wheels slide, its staying that way
	 */
	public RampedMoveController(double maxTranslationalAcceleration,
	                            double maxAngularAcceleration) {
		this.maxTranslationalAcceleration = maxTranslationalAcceleration;
		this.maxAngularAcceleration = maxAngularAcceleration;
	}
	
	public MotorSetPower getPower(
			XY targetTranslationalVelocity, double targetAngularVelocity,
			double maxTranslationalVelocity, double maxAngularVelocity) {
		double elapsedTime = this.elapsedTime.seconds();
		this.elapsedTime.reset();
		return getPower(targetTranslationalVelocity, targetAngularVelocity,
		                maxTranslationalVelocity, maxAngularVelocity, elapsedTime);
	}
	
	public MotorSetPower getPower(
			XY targetTranslationalVelocity, double targetAngularVelocity,
			double maxTranslationalVelocity, double maxAngularVelocity, double elapsedTime) {
		targetTranslationalVelocity = targetTranslationalVelocity.rotate(-Math.PI / 4);
		double targRightRate = limit(targetTranslationalVelocity.x, maxTranslationalVelocity);
		double targLeftRate = limit(targetTranslationalVelocity.y, maxTranslationalVelocity);
		targetAngularVelocity = limit(targetAngularVelocity, maxAngularVelocity);
		return getPower(targRightRate, targLeftRate, targetAngularVelocity, elapsedTime);
	}
	
	private MotorSetPower getPower(double targRightRate, double targLeftRate,
	                               double targetAngularVelocity, double elapsedTime) {
		if (elapsedTime > Constants.MAX_ELAPSED_TIME) elapsedTime = 0;
		double translationalRamp = this.maxTranslationalAcceleration * elapsedTime;
		double angularRamp = this.maxAngularAcceleration * elapsedTime;
		rightRate = ramp(rightRate, targRightRate, translationalRamp);
		leftRate = ramp(leftRate, targLeftRate, translationalRamp);
		turnRate = ramp(turnRate, targetAngularVelocity, angularRamp);
		return MotorSetPower.fromDiagonals(rightRate, leftRate,
		                                   turnRate);
	}
	
	private double ramp(double current, double target, double ramp) {
		return constrain(target,
		                 current - ramp,
		                 current + ramp);
	}
	
	private double constrain(double v, double min, double max) {
		return v < min ? min : v > max ? max : v;
	}
	
	private double limit(double v, double max) {
		return constrain(v, -max, max);
	}
	
}
