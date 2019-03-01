package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import android.annotation.SuppressLint;

/**
 * Represents a set of both
 * translational and angular magnitudes, 0 or positive.
 * Immutable.
 */
public final class Magnitudes {
	public static final Magnitudes ZERO = new Magnitudes(0);
	public final        double     translational;
	public final        double     angular;
	
	public Magnitudes(double translational, double angular) {
		this.translational = translational;
		this.angular = angular;
	}
	
	public Magnitudes(double magnitude) {
		if (magnitude < 0) throw new IllegalArgumentException();
		this.translational = magnitude;
		this.angular = magnitude;
	}
	
	@SuppressLint("DefaultLocale")
	@Override
	public String toString() {
		return String.format("{translational=%.4f, angular=%.4f}", translational, angular);
	}
}
