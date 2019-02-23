package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPosition;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.ENCODER_TICKS_PER_INCH;

/**
 * Records the position of the robot on a field given gyroscope and motor encoder feedback.
 * Positive X corresponds with angle 0deg, Positive Y with angle 90deg.
 * An "up" direction is arbitrary, so only relative positions make sense.
 * Units are arbitrary, and are defined by given parameter encoderTicksPerUnit.
 */
public class PositionTracker {
	private final double           encoderTicksPerUnit;
	private       XYR              currentPosition;
	private       MotorSetPosition lastMotorPos = null;
	
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
	 * Updates the stored position of the robot based on current angle (gyro) and current motorSet
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
	 * converts a difference in MotorSetPosition and gyroscope angles to a difference in XY
	 * relative to robot.
	 * Warning: contains math
	 */
	private XY toDeltaLocation(MotorSetPosition deltaMotor, double deltaAngle) {
		/** see {@link MotorSetPower#fromPolar(double, double, double)}*/
		//inverse approx. of above
		//                                                   p is dist, a is moveAngle
		double d14 = deltaMotor.get(0) + deltaMotor.get(3); // = 2p cos a
		double d23 = deltaMotor.get(1) + deltaMotor.get(2);//  = 2p sin a;
		double moveAngle = Math.atan2(d23, d14) + Math.PI / 4;
		double dist = Math.hypot(d23, d14) / 2 / encoderTicksPerUnit;
		//double turnRate = deltaAngle;
		//I forgot to divide by 2 one day, leading to bugs
		double x, y;
		//the following assumes that we moved and turned at the same rate, and hence is modeled by:
		// going [deltaAngle] radians on a circle of radius r ( = [dist]/[targAngle]),
		// starting at (0,0).
		// negative radii/angle make sense. (curve other way/backwards)
		if (Math.abs(deltaAngle) > 1e-8) {
			x = dist * (Math.sin(deltaAngle) / deltaAngle);
			y = dist * (1 - Math.cos(deltaAngle)) / deltaAngle;
		} else { //limit as deltaAngle -> 0 (straight line). Divide by zero errors are not nice
			x = dist;
			y = 0;
		}
		//then rotate the pos the bot actually moved along this line.
		XY res = new XY(x, y).rotate(moveAngle);
		return new XY(res.x / MotorSetPower.X_MULT, res.y);
	}
	
	/**
	 * Updates location given, given a robot to read gyro and encoder info from.
	 */
	public void updateLocation(IRobot robot) {
		updateLocation(robot.getAngle(),
		               robot.getWheels().getCurrentPosition());
	}
}
