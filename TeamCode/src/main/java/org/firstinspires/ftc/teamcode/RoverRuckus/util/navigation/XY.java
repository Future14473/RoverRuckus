package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

/**
 * Represents an XY position.
 * Warning: contains math.
 * Immutable.
 */
public final class XY {
	public static final XY ZERO = new XY();
	
	public final double x, y;
	
	public XY() {
		this(0, 0);
	}
	
	public XY(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public XY add(XY other) {
		if (other == null) return this;
		return new XY(this.x + other.x, this.y + other.y);
	}
	
	public XY subtract(XY other) {
		if (other == null) return this;
		return new XY(this.x - other.x, this.y - other.y);
	}
	
	public XY scale(double scale) {
		return new XY(this.x * scale, this.y * scale);
	}
	
	public double magnitude() {
		return Math.hypot(x, y);
	}
	
	public XY rotate(double angleRadians) {
		double cosA = Math.cos(angleRadians);
		double sinA = Math.sin(angleRadians);
		return new XY(this.x * cosA - this.y * sinA, this.x * sinA + this.y * cosA);
	}
	
	@Override
	public String toString() {
		return String.format("XY{%.5f, %.5f}", x, y);
	}
	
	public XY limitMagnitudeTo(double max) {
		double magnitude = magnitude();
		if (magnitude <= max) return this;
		return this.scale(max / magnitude);
	}
	
	public XY rampTo(XY other, double rampRate) {
		if (other == null) return rampTo(ZERO, rampRate);
		return other.subtract(this).limitMagnitudeTo(rampRate).add(this);
	}
	
	public double angleTo(XY o) {
		if (o == null) return angle();
		return Math.atan2(this.x * o.y - this.y * o.x, this.x * o.x + this.y * o.y);
	}
	
	public double angle() {
		return Math.atan2(y, x);
	}
	
	public double dot(XY o) {
		if (o == null) return 0;
		return x * o.x + y * o.y;
	}
	
	public static XY fromPolar(double magnitude, double angle) {
		return new XY(Math.cos(angle) * magnitude, Math.sin(angle) * magnitude);
	}
}
