package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.MAX_ELAPSED_TIME;

/**
 * A ramped move controller, based on wheel power diagonals.
 */
public class RampedMoveController {
	private final Magnitudes maxAccelerations;
	private       double     rightRate = 0;
	private       double     leftRate  = 0;
	private       double     turnRate  = 0;
	
	public RampedMoveController(Magnitudes maxAccelerations) {
		this.maxAccelerations = maxAccelerations;
	}
	
	/**
	 * Calculates power given target movement and max velocities.
	 */
	public MotorSetPower getPower(XYR targetMovement, Magnitudes maxVelocities,
	                              double elapsedTime) {
		if (elapsedTime > MAX_ELAPSED_TIME) elapsedTime = 0;
		//do x expansion, rotate to form diagonals
		XY targDiagonals = new XY(targetMovement.xy.x * MotorSetPower.X_MULT,
		                          targetMovement.xy.y).rotate(-Math.PI / 4);
		double targetAngular = targetMovement.angle;
		double translationalRamp = maxAccelerations.translational * elapsedTime;
		double angularRamp = maxAccelerations.angular * elapsedTime;
		//calculated limited and ramped rates
		this.rightRate = limitAndRamp(targDiagonals.x, maxVelocities.translational,
		                              this.rightRate, translationalRamp);
		this.leftRate = limitAndRamp(targDiagonals.y, maxVelocities.translational,
		                             this.leftRate, translationalRamp);
		this.turnRate = limitAndRamp(targetAngular, maxVelocities.angular, turnRate, angularRamp);
		return MotorSetPower.fromDiagonals(this.rightRate, this.leftRate,
		                                   this.turnRate);
	}
	
	private double limitAndRamp(double target, double max, double current, double ramp) {
		return ramp(current, constrain(target, -max, max), ramp);
	}
	
	private static double ramp(double current, double target, double ramp) {
		return constrain(target,
		                 current - ramp,
		                 current + ramp);
	}
	
	private static double constrain(double v, double min, double max) {
		return v < min ? min : v > max ? max : v;
	}
}
