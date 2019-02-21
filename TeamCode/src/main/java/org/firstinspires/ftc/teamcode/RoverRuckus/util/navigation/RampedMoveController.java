package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.MAX_ELAPSED_TIME;

/**
 * A ramped move controller, based on wheel power diagonals.
 */
public class RampedMoveController {
	protected final Magnitudes maxAccelerations;
	private         double     rightRate = 0;
	private         double     leftRate  = 0;
	private         double     turnRate  = 0;
	
	public RampedMoveController(Magnitudes maxAccelerations) {
		this.maxAccelerations = maxAccelerations;
	}
	
	public MotorSetPower getPower(XYR targetMovement, Magnitudes maxVelocities,
	                              double elapsedTime) {
		if (elapsedTime > MAX_ELAPSED_TIME) elapsedTime = 0;
		XY targetTranslational = targetMovement.xy;
		double targetAngular = targetMovement.angle;
		targetTranslational =
				new XY(targetTranslational.x * MotorSetPower.X_MULT,
				       targetTranslational.y).rotate(-Math.PI / 4);
		double targRightRate = limit(targetTranslational.x, maxVelocities.translational);
		double targLeftRate = limit(targetTranslational.y, maxVelocities.translational);
		targetAngular = limit(targetAngular, maxVelocities.angular);
		double translationalRamp = maxAccelerations.translational * elapsedTime;
		double angularRamp = maxAccelerations.angular * elapsedTime;
		this.rightRate = ramp(this.rightRate, targRightRate, translationalRamp);
		this.leftRate = ramp(this.leftRate, targLeftRate, translationalRamp);
		this.turnRate = ramp(turnRate, targetAngular, angularRamp);
		return MotorSetPower.fromDiagonals(this.rightRate, this.leftRate,
		                                   this.turnRate);
	}
	
	private static double ramp(double current, double target, double ramp) {
		return constrain(target,
		                 current - ramp,
		                 current + ramp);
	}
	
	private static double constrain(double v, double min, double max) {
		return v < min ? min : v > max ? max : v;
	}
	
	private static double limit(double v, double max) {
		return constrain(v, -max, max);
	}
}
