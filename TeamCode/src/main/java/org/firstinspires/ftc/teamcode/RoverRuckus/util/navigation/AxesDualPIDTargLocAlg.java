package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.MAX_ANGULAR_ERROR;
import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.MAX_TRANSLATIONAL_ERROR;

/**
 * Calculates movement given target and current direction and locations, using PID and
 * acceleration capping to calculate power output.
 */
public class AxesDualPIDTargLocAlg implements PIDTargLocAlg {
	private static final PIDCoefficients translationCoefficients = new PIDCoefficients(0.08, 0,
	                                                                                   0.3);
	private static final PIDCoefficients angularCoefficients     = new PIDCoefficients(2, 0, 6);
	
	//wheels 1, 4
	private final PID xPID     = new PID(translationCoefficients);
	//wheels 2, 3
	private final PID yPID     = new PID(translationCoefficients);
	//rotational
	private final PID anglePID = new PID(angularCoefficients);
	
	public AxesDualPIDTargLocAlg() {
		anglePID.setMaxError(MAX_ANGULAR_ERROR);
		xPID.setMaxError(MAX_TRANSLATIONAL_ERROR);
		yPID.setMaxError(MAX_TRANSLATIONAL_ERROR);
	}
	
	@Override
	public void setTranslationPIDCoefficients(PIDCoefficients coefficients) {
		xPID.setCoefficients(coefficients);
		yPID.setCoefficients(coefficients);
	}
	
	@Override
	public void setAngularPIDCoefficients(PIDCoefficients coefficients) {
		anglePID.setCoefficients(coefficients);
	}
	
	@Override
	public XYR getPower(XYR targetPosition, XYR currentPosition,
	                    Magnitudes maxVelocities,
	                    double elapsedTime) {
		anglePID.setMaxOutputs(maxVelocities.angular);
		anglePID.setTarget(targetPosition.angle);
		//rotate together
		//   (\) cos [x] is rightDiag, sin [y] is leftDiag
		//            1,4                  2,3
		XY diff = targetPosition.xy.subtract(currentPosition.xy);
		
		xPID.setMaxOutputs(maxVelocities.translational);
		yPID.setMaxOutputs(maxVelocities.translational);
		xPID.setTarget(diff.x);
		yPID.setTarget(diff.y);
		double turnRate = anglePID.getOutput(currentPosition.angle, elapsedTime);
		double xRate = xPID.getOutput(0, elapsedTime);
		double yRate = yPID.getOutput(0, elapsedTime);
		XY toMove = new XY(xRate, yRate).rotate(-currentPosition.angle);
		return new XYR(toMove, turnRate);
	}
	
}
