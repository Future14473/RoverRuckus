package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPosition;

public class LocationTracker {
	private final double           ticksPerUnit;
	private       double           lastAngle;
	private       MotorSetPosition lastMotorPos    = null;
	private       XY               currentLocation = XY.ZERO;
	
	public LocationTracker(double ticksPerUnit) {
		this.ticksPerUnit = ticksPerUnit;
	}
	
	public XY getCurrentLocation() {
		return currentLocation;
	}
	
	public double getCurrentAngle() {
		return lastAngle;
	}
	
	public void reset() {
		this.lastMotorPos = null;
		currentLocation = XY.ZERO;
	}
	
	public void updateLocation(double currentAngle, MotorSetPosition currentMotorPos) {
		if (lastMotorPos != null) {
			MotorSetPosition deltaMotorPos = currentMotorPos.subtract(lastMotorPos);
			XY deltaLocation = deltaMotorToDeltaLocation(deltaMotorPos, currentAngle);
			currentLocation = deltaLocation.add(currentLocation);
		}
		lastAngle = currentAngle;
		lastMotorPos = currentMotorPos;
	}
	
	private XY deltaMotorToDeltaLocation(MotorSetPosition delta, double currentAngle) {
		double d1 = delta.get(0), d2 = delta.get(1), d3 = delta.get(2), d4 = delta.get(3);
		double moveAngle = Math.atan2(d2 + d3, d1 + d4) + Math.PI / 4;
		double dist = Math.hypot(d2 + d3, d1 + d4) / ticksPerUnit;
		double turn = currentAngle - lastAngle;
		double x, y;
		// turn on circle from (1,0) of radius r (dist/turn) and angle turn.
		if (Math.abs(turn) < 1e-5) { //limit
			x = dist;
			y = 0;
		} else {
			x = dist * (Math.sin(turn) / turn);
			y = dist * (1 - Math.cos(turn)) / turn;
		}
		return new XY(x, y).rotate(moveAngle + lastAngle);
	}
	
	public void normalizeTargetLocation(double angle) {
	
	}
}
