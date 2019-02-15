package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import org.firstinspires.ftc.teamcode.RoverRuckus.externalLib.MiniPID;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.MoveController;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

/**
 * Calculates movement given target and current direction and locations, ramping
 * the power output.
 */
class LocationMoveController extends MoveController {
	private final MiniPID turnPID = new MiniPID(0.035, 0, 0.13);
	
	public LocationMoveController(
		double rampRate, PIDFCoefficients pidfCoefficients) {
		super(rampRate);
		MotorControlAlgorithm algorithm = pidfCoefficients.algorithm;
		turnPID.setSetpoint(0);
		turnPID.setOutputLimits(1);
		turnPID.setOutputRampRate(rampRate);
		turnPID.setOutputFilter(0.05);
	}
	
	public MotorSetPower getPower(
		XY targetLocation, XY currentLocation, //
		double targetDirection, double curDirection, double speed) {
		double elapsedTime = getElapsedTime();
		double angleToTarget = targetLocation.subtract(currentLocation).angle()-curDirection;
		XY targetMovement = XY.fromPolar(angleToTarget, speed);
		double targetTurnRate = turnPID.getOutput(curDirection, targetDirection, elapsedTime);
		return getPower(targetMovement, targetTurnRate, elapsedTime);
	}
	
}
