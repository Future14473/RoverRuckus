package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPosition;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.ENCODER_TICKS_PER_INCH;

public class PositionTracker {
	private final double           ticksPerUnit;
	private       XYR              currentPosition;
	private       MotorSetPosition lastMotorPos = null;
	
	public PositionTracker(double ticksPerUnit) {
		this.ticksPerUnit = ticksPerUnit;
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
	
	public void updateLocation(double currentAngle, MotorSetPosition currentMotorPos) {
		XY deltaLocation = XY.ZERO;
		if (lastMotorPos != null) {
			MotorSetPosition deltaMotorPos = currentMotorPos.subtract(lastMotorPos);
			deltaLocation = deltaMotorToDeltaLocation(deltaMotorPos, currentAngle);
		}
		currentPosition = new XYR(currentPosition.xy.add(deltaLocation), currentPosition.angle);
		lastMotorPos = currentMotorPos;
	}
	
	private XY deltaMotorToDeltaLocation(MotorSetPosition delta, double currentAngle) {
		double d1 = delta.get(0), d2 = delta.get(1), d3 = delta.get(2), d4 = delta.get(3);
		double moveAngle = Math.atan2(d2 + d3, d1 + d4) + Math.PI / 4;
		double dist = Math.hypot(d2 + d3, d1 + d4) / ticksPerUnit;
		double turn = currentAngle - currentPosition.angle;
		double x, y;
		// turn on circle from (1,0) of radius r (dist/turn) and angle turn.
		if (Math.abs(turn) < 1e-5) { //limit
			x = dist;
			y = 0;
		} else {
			x = dist * (Math.sin(turn) / turn);
			y = dist * (1 - Math.cos(turn)) / turn;
		}
		XY raw = new XY(x, y).rotate(moveAngle);
		raw = new XY(raw.x / MotorSetPower.X_MULT, raw.y);
		return raw.rotate(currentPosition.angle);
	}
	
	public void updateLocation(IRobot robot) {
		updateLocation(robot.getAngle(),
		               robot.getWheels().getCurrentPosition());
	}
}
