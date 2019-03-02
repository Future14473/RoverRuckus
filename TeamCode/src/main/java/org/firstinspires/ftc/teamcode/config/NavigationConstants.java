package org.firstinspires.ftc.teamcode.config;

import com.qualcomm.robotcore.hardware.PIDCoefficients;
import org.firstinspires.ftc.teamcode.lib.navigation.Magnitudes;

@SuppressWarnings("WeakerAccess")
public final class NavigationConstants {
	
	public static final double     ENCODER_TICKS_PER_INCH    = 125;
	public static final double     DEFAULT_MAX_ACCELERATION  = 4;
	public static final Magnitudes DEFAULT_MAX_ACCELERATIONS =
			new Magnitudes(DEFAULT_MAX_ACCELERATION);
	
	// For PIDs -------------------------------------------------------------------
	public static final PIDCoefficients PID_COEFFICIENTS_TRANSLATIONAL =
			new PIDCoefficients(0.18, 0, 1.5);
	public static final PIDCoefficients PID_COEFFICIENTS_ANGULAR       =
			new PIDCoefficients(1.6, 0, 3.5);
	
	public static final Magnitudes MAX_ERROR = new Magnitudes(16, Math.PI / 2);
	
	public static final double  MAX_ELAPSED_TIME = 0.25;
	public static final boolean USE_XY_PID       = true;
	
	private NavigationConstants() {
	}
}
