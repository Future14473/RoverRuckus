package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.PIDControl;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.PIDControl2D;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

/**
 * Calculates movement given target and current direction and locations, using PID and
 * acceleration capping to calculate power output.
 */
class PIDMoveController {
	private static final double MAX_ELAPSED_TIME = 0.1;
	
	private final PIDControl   anglePID       = new PIDControl(0.035, 0, 0.13);
	private final PIDControl2D translationPID = new PIDControl2D(0.1, 0, 0.1);
	private final ElapsedTime  elapsedTime    = new ElapsedTime();
	
	public PIDMoveController(double maxAngularAcceleration, double maxTranslationalAcceleration) {
		//rampedMoveController = new RampedMoveController();
		anglePID.setMaxOutputs(1);
		anglePID.setRampRate(maxAngularAcceleration);
		anglePID.setMaxError(Math.PI / 4);
		translationPID.setMaxOutput(2);
		translationPID.setRampRate(maxTranslationalAcceleration);
		translationPID.setMaxError(18);
	}
	
	public PIDMoveController(double maxAcceleration) {
		this(maxAcceleration, maxAcceleration);
	}
	
	public MotorSetPower getPower(
			XY targetLocation, XY currentLocation,
			double targetAngle, double currentAngle, double maxAngularSpeed,
			double maxTranslationSpeed) {
		translationPID.setMaxOutput(maxTranslationSpeed);
		translationPID.setTarget(targetLocation);
		anglePID.setMaxOutputs(maxAngularSpeed);
		anglePID.setTarget(targetAngle);
		
		double elapsedTime = this.elapsedTime.seconds();
		this.elapsedTime.reset();
		if (elapsedTime > MAX_ELAPSED_TIME) elapsedTime = MAX_ELAPSED_TIME;
		double turnRate = anglePID.getOutput(currentAngle, elapsedTime);
		XY moveRate = translationPID.getOutput(currentLocation, elapsedTime);
		return MotorSetPower.fromXYT(moveRate, turnRate);
	}
	
	public MotorSetPower getPower(
			LocationTracker locationTracker, double maxAngularSpeed,
			double maxTranslationalSpeed) {
		return getPower(locationTracker.getTargetLocation(),
		                locationTracker.getCurrentLocation(),
		                locationTracker.getTargetAngle(),
		                locationTracker.getCurrentAngle(),
		                maxAngularSpeed,
		                maxTranslationalSpeed);
	}
	
}
