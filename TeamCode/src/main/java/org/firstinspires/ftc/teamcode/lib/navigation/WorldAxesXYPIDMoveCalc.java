package org.firstinspires.ftc.teamcode.lib.navigation;

import org.firstinspires.ftc.teamcode.config.NavigationConstants;

/**
 * Calculates movement given target and current direction and locations, using PID and
 * acceleration capping to calculate power output.
 */
public class WorldAxesXYPIDMoveCalc implements AutoMoveCalculator {
	
	//wheels 1, 4
	private final PIDXY translationalPID = new PIDXY(
			NavigationConstants.PID_COEFFICIENTS_TRANSLATIONAL);
	//rotational
	private final PID   anglePID         = new PID(NavigationConstants.PID_COEFFICIENTS_ANGULAR);
	
	public WorldAxesXYPIDMoveCalc() {
		anglePID.setMaxError(NavigationConstants.MAX_ERROR.angular);
		translationalPID.setMaxError(NavigationConstants.MAX_ERROR.translational);
	}
	
	@Override
	public void setTargetPosition(XYR targetPosition) {
		translationalPID.setTarget(targetPosition.xy);
		anglePID.setTarget(targetPosition.angle);
	}
	
	@Override
	public void setMaxVelocities(Magnitudes maxVelocities) {
		translationalPID.setMaxOutput(maxVelocities.translational);
		anglePID.setMaxOutputs(maxVelocities.angular);
	}
	
	@Override
	public XYR getMovement(XYR currentPosition, double elapsedTime) {
		double turnRate = anglePID.getOutput(currentPosition.angle, elapsedTime);
		XY moveRate = translationalPID.getOutput(currentPosition.xy, elapsedTime)
		                              .rotate(-currentPosition.angle);
		//difference: rotate after. Rotating robot wont change extrinsic movement direction.
//		RobotLog.dd("XYMotorControlAlg", "Diff: %s%nMoveRate: %s",
//		            targetPosition.xy.subtract(currentPosition.xy),
//		            moveRate);
		return new XYR(moveRate, turnRate);
	}
	
}
