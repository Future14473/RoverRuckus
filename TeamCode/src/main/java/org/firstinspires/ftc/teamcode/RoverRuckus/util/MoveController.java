package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

public class MoveController {
	private final double rampRate;
	private       XY     lastMovement = new XY();
	private       double lastTurnRate = 0;
	private       long   lastTime     = System.nanoTime();
	
	public MoveController(
		double rampRate) {
		this.rampRate = rampRate;
	}
	
	public MotorSetPower getPower(XY targetMovement, double targetTurnRate) {
		return getPower(targetMovement, targetTurnRate, getElapsedTime());
	}
	
	public MotorSetPower getPower(
		XY targetMovement, double targetTurnRate, double elapsedTime) {
		double rampRate = this.rampRate * elapsedTime;
		XY curMovement = lastMovement.rampTo(targetMovement, rampRate);
		lastMovement = curMovement;
		lastTurnRate = constrain(targetTurnRate, lastTurnRate-rampRate, lastTurnRate+rampRate);
		return MotorSetPower.fromXY(curMovement, lastTurnRate);
	}
	
	public void resetTime() {
		lastTime = System.nanoTime();
	}
	
	protected double getElapsedTime() {
		long curTime = System.nanoTime();
		double elapsedTime = (curTime-lastTime) / 1e9;
		lastTime = curTime;
		if (elapsedTime > 1) elapsedTime = 0.1; //just in case
		return elapsedTime;
	}
	
	private double constrain(double v, double min, double max) {
		if (v < min) return min;
		if (v > max) return max;
		return v;
	}
	
	public void hardStop() {
		lastMovement = new XY();
		lastTurnRate = 0;
		resetTime();
	}
}
