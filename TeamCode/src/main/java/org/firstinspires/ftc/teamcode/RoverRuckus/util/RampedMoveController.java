package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

@SuppressWarnings("WeakerAccess")
public class RampedMoveController {
	private static final double MAX_ELAPSED_TIME   = 0.15;
	private static final double RESET_ELAPSED_TIME = 0.02;
	
	private final double      maxTranslationAcceleration;
	private final double      maxAngularAcceleration;
	private final ElapsedTime elapsedTime              = new ElapsedTime();
	private       XY          curTranslationalVelocity = new XY();
	private       double      curAngularVelocity       = 0;
	
	public RampedMoveController(double maxAcceleration) {
		this(maxAcceleration, maxAcceleration);
	}
	
	/**
	 * Creates a new ramped move controller with the given parameters:
	 *
	 * @param maxTranslationAcceleration the maximum magnitude of the difference in power-level
	 *                                   pseudo-velocity vector per second. If you don't
	 *                                   understand that, that's fine, I almost didn't either,
	 *                                   just plug in a similar value to maxAngularAcceleration
	 * @param maxAngularAcceleration     The maximum difference in power level per second due to
	 *                                   turning.
	 * @implNote The actual difference in power levels may be a combination of the above two.
	 * 		unless some genius comes along and does some math to calibrate/adjust the two into one
	 * 		parameter without making the wheels slide, its staying that way
	 */
	public RampedMoveController(double maxTranslationAcceleration, double maxAngularAcceleration) {
		this.maxTranslationAcceleration = maxTranslationAcceleration;
		this.maxAngularAcceleration = maxAngularAcceleration;
	}
	
	public MotorSetPower getPower(
			XY targetTranslationalVelocity, double targetAngularVelocity) {
		double elapsedTime = this.elapsedTime.seconds();
		this.elapsedTime.reset();
		return getPower(targetTranslationalVelocity, targetAngularVelocity, elapsedTime);
	}
	
	public MotorSetPower getPower(
			XY targetTranslationalVelocity, double targetAngularVelocity, double elapsedTime) {
		
		if (elapsedTime > MAX_ELAPSED_TIME) elapsedTime = RESET_ELAPSED_TIME;
		double translationalRamp = this.maxTranslationAcceleration * elapsedTime;
		curTranslationalVelocity =
				curTranslationalVelocity.rampTo(targetTranslationalVelocity, translationalRamp);
		double angularRamp = this.maxAngularAcceleration * elapsedTime;
		curAngularVelocity = constrain(targetAngularVelocity,
		                               curAngularVelocity - angularRamp,
		                               curAngularVelocity + angularRamp);
		return MotorSetPower.fromXYT(curTranslationalVelocity, curAngularVelocity);
	}
	
	private double constrain(double v, double min, double max) {
		return v < min ? min : v > max ? max : v;
	}
	
}
