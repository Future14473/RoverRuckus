package org.firstinspires.ftc.teamcode.lib.navigation;

import org.firstinspires.ftc.teamcode.config.NavigationConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Calculates movement given target and current direction and locations, using PID and
 * acceleration capping to calculate power output.
 */
public class WorldAxesDualPIDMoveCalc implements AutoMoveCalculator {
	
	//wheels 1, 4
	private final PID xPID     = new PID(NavigationConstants.PID_COEFFICIENTS_TRANSLATIONAL);
	//wheels 2, 3
	private final PID yPID     = new PID(NavigationConstants.PID_COEFFICIENTS_TRANSLATIONAL);
	//rotational
	private final PID anglePID = new PID(NavigationConstants.PID_COEFFICIENTS_ANGULAR);
	
	public WorldAxesDualPIDMoveCalc() {
		anglePID.setMaxError(NavigationConstants.MAX_ERROR.angular);
		xPID.setMaxError(NavigationConstants.MAX_ERROR.translational);
		yPID.setMaxError(NavigationConstants.MAX_ERROR.translational);
	}
	
	@Override
	public void setTargetPosition(XYR targetPosition) {
		xPID.setTarget(targetPosition.xy.x);
		yPID.setTarget(targetPosition.xy.y);
		anglePID.setTarget(targetPosition.angle);
	}
	
	@Override
	public void setMaxVelocities(@NotNull Magnitudes maxVelocities) {
		xPID.setMaxOutputs(maxVelocities.translational);
		yPID.setMaxOutputs(maxVelocities.translational);
		anglePID.setMaxOutputs(maxVelocities.angular);
	}
	
	@Override
	public XYR getMovement(XYR currentPosition, double elapsedTime) {
		//rotate together
		//   (\) cos [x] is rightDiag, sin [y] is leftDiag
		//            1,4                  2,3
		double turnRate = anglePID.getOutput(currentPosition.angle, elapsedTime);
		XY targLoc = currentPosition.xy;
		double xRate = xPID.getOutput(targLoc.x, elapsedTime);
		double yRate = yPID.getOutput(targLoc.y, elapsedTime);
		XY toMove = new XY(xRate, yRate).rotate(-currentPosition.angle);
		return new XYR(toMove, turnRate);
	}
	
}
