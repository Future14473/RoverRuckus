package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

/**
 * Represents a position. with both coordinates
 * and rotation.
 * note that this <i>__IS__</i> mutable
 */
public final class XYR {
	public static final XYR ZERO = new XYR(XY.ZERO, 0);
	
	public final XY     xy;
	public final double angle;
	
	public XYR(XY xy, double angle) {
		this.xy = xy;
		this.angle = angle;
	}
	
	public XYR withNewXY(XY xy) {
		return new XYR(xy, angle);
	}
	
	public XYR withNewAngle(double angle) {
		return new XYR(xy, angle);
	}
	
	public XYR add(XYR other) {
		return new XYR(this.xy.add(other.xy), this.angle + other.angle);
	}
	
	public XYR subtract(XYR other) {
		return new XYR(this.xy.subtract(other.xy), this.angle - other.angle);
	}
	
	@Override
	public String toString() {
		return String.format("{location=%s, angle=%s}", xy, angle);
	}
	
	public XYR addToXY(XY XY) {
		return withNewXY(this.xy.add(XY));
	}
	
	public XYR addToAngle(double angle) {
		return withNewAngle(this.angle + angle);
	}
	
}
