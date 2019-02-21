package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import com.qualcomm.robotcore.hardware.PIDCoefficients;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

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
	public MotorSetPower getPower(XYR targetPosition, XYR currentPosition,
	                              double maxAngularSpeed, double maxTranslationSpeed,
	                              double elapsedTime) {
		anglePID.setMaxOutputs(maxAngularSpeed);
		anglePID.setTarget(targetPosition.angle);
		//rotate together
		//   (\) cos [x] is rightDiag, sin [y] is leftDiag
		//            1,4                  2,3
		XY axesDiff =
				targetPosition.xy.subtract(currentPosition.xy);
		translationalPID.setMaxOutput(maxTranslationSpeed);
		translationalPID.setTarget(axesDiff);
		
		
		double turnRate = anglePID.getOutput(currentPosition.angle, elapsedTime);
		XY moveRate = translationalPID.getOutput(XY.ZERO, elapsedTime)
		                              .rotate(-currentPosition.angle);
		//difference: rotate after. Rotating robot wont change extrinsic movement direction.
//		RobotLog.dd("XYMotorControlAlg", "Diagonal diff: %s%nMoveRate: %s", diagonalDiff,
//		            moveRate);
		return MotorSetPower.fromXY(moveRate.x, moveRate.y, turnRate);
	}
	
}
