package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.*;

/**
 * Calculates movement given target and current direction and locations, using PID and
 * acceleration capping to calculate power output.
 */
public class WorldAxesXYPIDMoveCalc implements AutoMoveCalculator {
	
	//wheels 1, 4
	private final PIDXY translationalPID = new PIDXY(PID_COEFFICIENTS_TRANSLATIONAL);
	//rotational
	private final PID   anglePID         = new PID(PID_COEFFICIENTS_ANGULAR);
	
	public WorldAxesXYPIDMoveCalc() {
		anglePID.setMaxError(MAX_ANGULAR_ERROR);
		translationalPID.setMaxError(MAX_TRANSLATIONAL_ERROR);
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
