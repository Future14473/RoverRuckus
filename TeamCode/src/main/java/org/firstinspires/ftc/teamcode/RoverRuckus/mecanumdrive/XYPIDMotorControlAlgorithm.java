package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.PIDController;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.PIDControllerXY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.*;

/**
 * Calculates movement given target and current direction and locations, using PID and
 * acceleration capping to calculate power output.
 */
public class XYPIDMotorControlAlgorithm implements MotorControlAlgorithm {
	
	//wheels 1, 4
	private final PIDControllerXY translationalPID =
			new PIDControllerXY(PID_COEFFICIENTS_TRANSLATIONAL);
	//rotational
	private final PIDController   anglePID         = new PIDController(PID_COEFFICIENTS_ANGULAR);
	
	private final ElapsedTime elapsedTime = new ElapsedTime();
	
	public XYPIDMotorControlAlgorithm(double maxAngularAcceleration,
	                                  double maxTranslationalAcceleration) {
		anglePID.setRampRate(maxAngularAcceleration);
		anglePID.setMaxError(MAX_ANGULAR_ERROR);
		translationalPID.setRampRate(maxTranslationalAcceleration * Math.sqrt(2));
		translationalPID.setMaxError(MAX_TRANSLATIONAL_ERROR);
	}
	
	public XYPIDMotorControlAlgorithm(double maxAcceleration) {
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
		translationalPID.setMaxOutput(maxTranslationSpeed);
		translationalPID.setTarget(diagonalDiff);
		
		double elapsedTime = this.elapsedTime.seconds();
		this.elapsedTime.reset();
		
		double turnRate = anglePID.getOutput(currentAngle, elapsedTime);
		XY moveRate = translationalPID.getOutput(XY.ZERO, elapsedTime);
		RobotLog.dd("XYMotorControlAlg", "Diagonal diff: %s%nMoveRate: %s", diagonalDiff,
		            moveRate);
		//noinspection SuspiciousNameCombination
		return MotorSetPower.fromDiagonals(moveRate.x, moveRate.y, turnRate);
	}
	
}
