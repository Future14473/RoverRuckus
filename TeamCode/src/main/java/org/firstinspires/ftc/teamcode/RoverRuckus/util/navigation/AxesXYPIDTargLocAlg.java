package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.*;

/**
 * Calculates movement given target and current direction and locations, using PID and
 * acceleration capping to calculate power output.
 */
public class AxesXYPIDTargLocAlg implements PIDTargLocAlg {
	
	//wheels 1, 4
	private final PIDXY translationalPID = new PIDXY(PID_COEFFICIENTS_TRANSLATIONAL);
	//rotational
	private final PID   anglePID         = new PID(PID_COEFFICIENTS_ANGULAR);
	
	public AxesXYPIDTargLocAlg() {
		anglePID.setMaxError(MAX_ANGULAR_ERROR);
		translationalPID.setMaxError(MAX_TRANSLATIONAL_ERROR);
	}
	
	@Override
	public void setTranslationPIDCoefficients(PIDCoefficients coefficients) {
		translationalPID.setCoefficients(coefficients);
	}
	
	@Override
	public void setAngularPIDCoefficients(PIDCoefficients coefficients) {
		anglePID.setCoefficients(coefficients);
	}
	
	@Override
	public XYR getPower(XYR targetPosition, XYR currentPosition,
	                    Magnitudes maxVelocities,
	                    double elapsedTime) {
		translationalPID.setMaxOutput(maxVelocities.translational);
		translationalPID.setTarget(targetPosition.xy);
		anglePID.setMaxOutputs(maxVelocities.angular);
		anglePID.setTarget(targetPosition.angle);
		
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
