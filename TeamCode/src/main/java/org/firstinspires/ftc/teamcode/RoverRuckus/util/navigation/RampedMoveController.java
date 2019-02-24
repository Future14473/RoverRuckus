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
	 * Calculates a ramped motor powers given target movement (translational and angular), and
	 * elapsed time (in seconds) to determine ramp level.
	 */
	public MotorSetPower getPower(XYR targetMovement, double elapsedTime) {
		if (elapsedTime > MAX_ELAPSED_TIME) elapsedTime = 0;
		//do x expansion, rotate to form diagonals
		XY targDiagonals =
				new XY(targetMovement.xy.x * MotorSetPower.X_MULT, targetMovement.xy.y).rotate(
						-Math.PI / 4);
		double targetAngular = targetMovement.angle;
		double translationalRamp = maxAccelerations.translational * elapsedTime;
		double angularRamp = maxAccelerations.angular * elapsedTime;
		//left and right are independent (wheels)
		this.rightRate = ramp(this.rightRate, targDiagonals.x, translationalRamp);
		this.leftRate = ramp(this.leftRate, targDiagonals.y, translationalRamp);
		this.turnRate = ramp(turnRate, targetAngular, angularRamp);
		return MotorSetPower.fromDiagonals(this.rightRate, this.leftRate, this.turnRate);
	}
	
	private static double ramp(double current, double target, double ramp) {
		return constrain(target, current - ramp, current + ramp);
	}
	
	private static double constrain(double v, double min, double max) {
		return v < min ? min : v > max ? max : v;
	}
}
