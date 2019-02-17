package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPosition;

public class LocationTracker {
	private final double ticksPerUnit;
	private       double targetAngle, currentAngle, lastAngle;
	private XY targetLocation, currentLocation;
	private MotorSetPosition lastMotorPos = null;
	
	public LocationTracker(double ticksPerUnit) {
		this.ticksPerUnit = ticksPerUnit;
	}
	
	public void addToTargetAngle(double toTurn) {
		targetAngle += toTurn;
	}
	
	public void addToTargetLocation(XY toMove) {
		this.targetLocation = targetLocation.add(toMove);
	}
	
	public XY getTargetLocation() {
		return targetLocation;
	}
	
	public XY getCurrentLocation() {
		return currentLocation;
	}
	
	public double getTargetAngle() {
		return targetAngle;
	}
	
	public double getCurrentAngle() {
		return currentAngle;
	}
	
	public void updateLocation(double currentAngle, MotorSetPosition currentMotorPos) {
		this.currentAngle = currentAngle;
		MotorSetPosition deltaMotorPos = currentMotorPos.subtract(lastMotorPos);
		XY deltaLocation = deltaMotorToDeltaLocation(deltaMotorPos);
		currentLocation = currentLocation.add(deltaLocation);
		
		lastAngle = currentAngle;
		lastMotorPos = currentMotorPos;
	}
	
	private XY deltaMotorToDeltaLocation(MotorSetPosition delta) {
		double d1 = delta.get(0), d2 = delta.get(1), d3 = delta.get(2), d4 = delta.get(3);
		double moveAngle = Math.atan2(d2 + d3, d1 + d4);
		double dist = Math.hypot(d2 + d3, d1 + d4) / ticksPerUnit;
		moveAngle += Math.PI / 4;
		double turn = currentAngle - lastAngle;
		double x;
		double y;
		// turn on circle from (1,0) of radius r (dist/turn) and angle turn.
		if (Math.abs(turn % (Math.PI * 2)) < 1e-5) {
			x = 0;
			y = dist;
		} else {
			x = dist * (Math.cos(turn) - 1) / turn;
			y = dist * (Math.sin(turn) / turn);
		}
		return new XY(x, y).rotate(moveAngle + lastAngle);
	}
	
	public XY getLocationError() {
		return targetLocation.subtract(currentLocation);
	}
	
	public double getAngularError() {
		return targetAngle - currentAngle;
	}
}
