package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.Constants;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.PIDController;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

/**
 * Calculates movement given target and current direction and locations, using PID and
 * acceleration capping to calculate power output.
 */
public class PIDMoveCalculator {
	
	//wheels 1, 4
	private final PIDController rightDiagPID = new PIDController(0.1, 0, 0.1);
	//wheels 2, 3
	private final PIDController leftDiagPID  = new PIDController(0.1, 0, 0.1);
	private final PIDController anglePID     = new PIDController(0.035, 0, 0.13);
	private final ElapsedTime   elapsedTime  = new ElapsedTime();
	
	public PIDMoveCalculator(double maxAngularAcceleration, double maxTranslationalAcceleration) {
		anglePID.setRampRate(maxAngularAcceleration);
		anglePID.setMaxError(Constants.MAX_ANGULAR_ERROR);
		rightDiagPID.setRampRate(maxTranslationalAcceleration);
		rightDiagPID.setMaxError(Constants.MAX_TRANSLATIONAL_ERROR);
		leftDiagPID.setRampRate(maxTranslationalAcceleration);
		leftDiagPID.setMaxError(Constants.MAX_TRANSLATIONAL_ERROR);
	}
	
	public PIDMoveCalculator(double maxAcceleration) {
		this(maxAcceleration, maxAcceleration);
	}
	
	public MotorSetPower getPower(
			XY targetLocation, XY currentLocation,
			double targetAngle, double currentAngle,
			double maxAngularSpeed, double maxTranslationSpeed) {
		anglePID.setMaxOutputs(maxAngularSpeed);
		anglePID.setTarget(targetAngle);
		XY diagonalDiff =
				targetLocation.subtract(currentLocation).rotate(-currentAngle - Math.PI / 4);
		//rotate together
		//   (\) cos [x] is rightDiag, sin [y] is leftDiag
		//            1,4                  2,3
		rightDiagPID.setMaxOutputs(maxTranslationSpeed);
		leftDiagPID.setMaxOutputs(maxTranslationSpeed);
		rightDiagPID.setTarget(diagonalDiff.x);
		leftDiagPID.setTarget(diagonalDiff.y);
		
		double elapsedTime = this.elapsedTime.seconds();
		this.elapsedTime.reset();
		
		double turnRate = anglePID.getOutput(currentAngle, elapsedTime);
		double rightRate = rightDiagPID.getOutput(0, elapsedTime);
		double leftRate = leftDiagPID.getOutput(0, elapsedTime);
		return MotorSetPower.fromDiagonals(rightRate, leftRate, turnRate);
	}
	
}
