package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

/**
 * Represents a set of both
 * translational and angular magnitudes.
 * Immutable.
 */
public class Magnitudes {
	public static final Magnitudes ZERO = new Magnitudes(0);
	public final        double     translational;
	public final        double     angular;
	
	public Magnitudes(double translational, double angular) {
		this.translational = translational;
		this.angular = angular;
	}
	
	public Magnitudes(double magnitude) {
		this.translational = magnitude;
		this.angular = magnitude;
	}
}
