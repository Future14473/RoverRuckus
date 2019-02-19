package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.Constants;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

/**
 * Calculates movement given target and current direction and locations, using PID and
 * acceleration capping to calculate power output.
 */
public class DualPIDTargetLocationAlgorithm implements TargetLocationAlgorithm {
	private static final PIDCoefficients translationCoefficients = new PIDCoefficients(0.08, 0,
	                                                                                   0.3);
	private static final PIDCoefficients angularCoefficients     = new PIDCoefficients(2, 0, 6);
	
	//wheels 1, 4
	private final PID         rightDiagPID = new PID(translationCoefficients);
	//wheels 2, 3
	private final PID         leftDiagPID  = new PID(translationCoefficients);
	//rotational
	private final PID         anglePID     = new PID(angularCoefficients);
	private final ElapsedTime elapsedTime  = new ElapsedTime();
	
	public DualPIDTargetLocationAlgorithm(double maxAngularAcceleration,
	                                      double maxTranslationalAcceleration) {
		anglePID.setRampRate(maxAngularAcceleration);
		anglePID.setMaxError(Constants.MAX_ANGULAR_ERROR);
		rightDiagPID.setRampRate(maxTranslationalAcceleration);
		leftDiagPID.setRampRate(maxTranslationalAcceleration);
		rightDiagPID.setMaxError(Constants.MAX_TRANSLATIONAL_ERROR);
		leftDiagPID.setMaxError(Constants.MAX_TRANSLATIONAL_ERROR);
	}
	
	public DualPIDTargetLocationAlgorithm(double maxAcceleration) {
		this(maxAcceleration, maxAcceleration);
	}
	
	@Override
	public MotorSetPower getPower(
			XY targetLocation, XY currentLocation,
			double targetAngle, double currentAngle,
			double maxAngularSpeed, double maxTranslationSpeed) {
		anglePID.setMaxOutputs(maxAngularSpeed);
		anglePID.setTarget(targetAngle);
		//rotate together
		//   (\) cos [x] is rightDiag, sin [y] is leftDiag
		//            1,4                  2,3
		XY diagonalDiff =
				targetLocation.subtract(currentLocation).rotate(-currentAngle - Math.PI / 4);
		rightDiagPID.setMaxOutputs(maxTranslationSpeed);
		leftDiagPID.setMaxOutputs(maxTranslationSpeed);
		rightDiagPID.setTarget(diagonalDiff.x);
		leftDiagPID.setTarget(diagonalDiff.y);
		
		double elapsedTime = this.elapsedTime.seconds();
		this.elapsedTime.reset();
		
		double turnRate = anglePID.getOutput(currentAngle, elapsedTime);
		double rightRate;
		rightRate = rightDiagPID.getOutput(0, elapsedTime);
		double leftRate;
		leftRate = leftDiagPID.getOutput(0, elapsedTime);
		return MotorSetPower.fromDiagonals(rightRate, leftRate, turnRate);
	}
	
}
