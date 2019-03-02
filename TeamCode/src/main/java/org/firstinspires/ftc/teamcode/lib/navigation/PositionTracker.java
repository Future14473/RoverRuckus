package org.firstinspires.ftc.teamcode.lib.navigation;

import org.firstinspires.ftc.teamcode.lib.robot.IRobot;
import org.firstinspires.ftc.teamcode.lib.robot.MotorSetPosition;
import org.firstinspires.ftc.teamcode.lib.robot.MotorSetPower;

import static org.firstinspires.ftc.teamcode.config.HardwareConstants.X_MULT;
import static org.firstinspires.ftc.teamcode.config.NavigationConstants.ENCODER_TICKS_PER_INCH;

/**
 * Records the position of the robot on a field given gyroscope and motor encoder readings.
 * Positive X corresponds with angle 0deg, Positive Y with angle 90deg.
 * An "up" direction is arbitrary, so only relative positions make sense.
 * Units are arbitrary, and are defined by given parameter encoderTicksPerUnit.
 */
public class PositionTracker {
	private final double           encoderTicksPerUnit;
	private       XYR              currentPosition = XYR.ZERO;
	private       MotorSetPosition lastMotorPos    = null;
	
	public PositionTracker(double encoderTicksPerUnit) {
		this.encoderTicksPerUnit = encoderTicksPerUnit;
	}
	
	public PositionTracker() {
		this(ENCODER_TICKS_PER_INCH);
	}
	
	public XYR getCurrentPosition() {
		return currentPosition;
	}
	
	public void reset() {
		currentPosition = XYR.ZERO;
	}
	
	/**
	 * Updates the stored position of the robot based on current angle (gyro) and current motor
	 * position (encoders).
	 */
	public void updateLocation(double currentAngle, MotorSetPosition currentMotorPos) {
		XY deltaLocation = XY.ZERO;
		if (lastMotorPos != null) {
			MotorSetPosition deltaMotorPos = currentMotorPos.subtract(lastMotorPos);
			deltaLocation =
					toDeltaLocation(deltaMotorPos, currentAngle - currentPosition.angle)
							.rotate(currentPosition.angle);
			//remember to rotate by direction robot is moving.
		}
		currentPosition = new XYR(currentPosition.xy.add(deltaLocation), currentAngle);
		lastMotorPos = currentMotorPos;
	}
	
	/**
	 * Converts a difference in MotorSetPosition and gyroscope angles to a difference in XY
	 * relative to robot.
	 * Warning: contains math
	 *
	 * @param deltaMotor the difference in motor position (encoders)
	 * @param deltaAngle the difference in robot angle (gyro)
	 */
	private XY toDeltaLocation(MotorSetPosition deltaMotor, double deltaAngle) {
		/** see {@link MotorSetPower#fromPolar(double, double, double)}*/
		//inverse approx. of above                            p is dist, a is moveAngle
		double d14 = deltaMotor.get(0) + deltaMotor.get(3); // = 2p cos a
		double d23 = deltaMotor.get(1) + deltaMotor.get(2);//  = 2p sin a;
		double moveAngle = Math.atan2(d23, d14) + Math.PI / 4;
		double dist = Math.hypot(d23, d14) / 2 / encoderTicksPerUnit;
		double x, y;
		//the following assumes that we moved and turned at the same rate, and hence is modeled by:
		// going [deltaAngle] radians on a circle of radius r ( = [dist]/[targAngle]),
		// starting at (0,0).
		// negative radii/angle make sense. (curve other way/backwards)
		if (Math.abs(deltaAngle) > 1e-9) {
			x = dist * (Math.sin(deltaAngle) / deltaAngle);
			y = dist * (1 - Math.cos(deltaAngle)) / deltaAngle;
		} else { //limit as deltaAngle -> 0 (straight line). Divide by zero errors are not nice
			x = dist;
			y = 0;
		}
		//then rotate this curved point in the direction the robot actually moved
		XY res = new XY(x, y).rotate(moveAngle);
		//moving sideways is less than moving forwards/backwards.
		return new XY(res.x / X_MULT, res.y);
	}
	
	/**
	 * Updates location given, given a robot to read gyro and encoder info from.
	 */
	public void updateLocation(IRobot robot) {
		updateLocation(robot.getAngle(), robot.getWheels().getCurrentPosition());
	}
}
