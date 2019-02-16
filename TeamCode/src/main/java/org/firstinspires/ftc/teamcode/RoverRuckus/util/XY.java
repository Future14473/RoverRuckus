package org.firstinspires.ftc.teamcode.RoverRuckus.util;

/**
 * Represents an XY position.
 * Warning: contains math.
 * Immutable.
 */
public final class XY {
	
	public final double x, y;
	
	public XY() {
		this(0, 0);
	}
	
	public XY(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public XY add(XY other) {
		return new XY(this.x + other.x, this.y + other.y);
	}
	
	public XY subtract(XY other) {
		return new XY(this.x - other.x, this.y - other.y);
	}
	
	public XY scale(double scale) {
		return new XY(this.x * scale, this.y * scale);
	}
	
	public double magnitude() {
		return Math.hypot(x, y);
	}
	
	public XY rotate(double radians) {
		double cosA = Math.cos(radians);
		double sinA = Math.sin(radians);
		return new XY(this.x * cosA - this.y * sinA, this.x * sinA + this.y * cosA);
	}
	
	public XY limitMagnitudeTo(double max) {
		double magnitude = magnitude();
		if (magnitude <= max) return this;
		return scale(max / magnitude);
	}
	
	public XY rampTo(XY other, double rampRate) {
		return other.subtract(this).limitMagnitudeTo(rampRate).add(this);
	}
	
	public double angle() {
		return Math.atan2(y, x);
	}
	
	public static XY fromPolar(double direction, double radius) {
		return new XY(Math.cos(direction) * radius, Math.sin(direction) * radius);
	}
}
