package org.firstinspires.ftc.teamcode.RoverRuckus;

import com.qualcomm.robotcore.hardware.PIDCoefficients;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.Magnitudes;

public class Constants {
	//Encoder limits -----------------------
	//"at home" position for all encoders
	public static final int        MOTOR_MIN                     = 20;
	//maximum arm extension
	public static final int        SCORE_ARM_MAX                 = 1750;
	public static final int        COLLECT_ARM_MAX               = 4450;
	//maximum hook extension
	public static final int        HOOK_MAX                      = 26000;
	public static final int        HOOK_NULLIFY                  = 1000;
	//initial extensions during auto extensions
	public static final int        COLLECT_ARM_INITIAL_EXTENSION = 2000;
	public static final int        SCORE_ARM_INITIAL_EXTENSION   = SCORE_ARM_MAX;
	public static final Integer    COLLECT_ARM_AWAY              = 100;
	// Servo positions -----------------------
	//Collect door positions
	public static final double     COLLECT_DOOR_CLOSED           = 0.71;
	public static final double     COLLECT_DOOR_OPEN             = 0.30;
	//Score dump positions
	public static final double     SCORE_DUMP_HOME               = 0.58;
	public static final double     SCORE_DUMP_DOWN               = 0;
	//parker
	public static final double     PARKER_HOME                   = 0.6;
	//Mults -----------------------
	public static final double     SPEED_MULT_FAST               = 100;
	public static final double     SPEED_MULT_NORM               = 1;
	public static final double     SPEED_MULT_SLOW               = 0.4;
	//Powers -----------------------
	public static final double     IDLE_POWER_IN                 = -0.6;
	public static final double     IDLE_POWER_OUT                = 0.8;
	public static final double     IDLE_POWER_SCOOPER            = 0.6;
	//Other constants -----------------------
	public static final int        TRANSFER_SLEEP_TIME           = 200;
	//For movement tracking -----------------------
	public static final double     ENCODER_TICKS_PER_INCH        = 125;
	public static final double     DEFAULT_MAX_ACCELERATION      = 4;
	public static final Magnitudes DEFAULT_MAX_ACCELERATIONS     =
			new Magnitudes(DEFAULT_MAX_ACCELERATION);
	
	//For PIDs -----------------------
	public static final PIDCoefficients PID_COEFFICIENTS_TRANSLATIONAL =
			new PIDCoefficients(0.15, 0, 1.2);
	public static final PIDCoefficients PID_COEFFICIENTS_ANGULAR       =
			new PIDCoefficients(1.3, 0, 3);
	
	public static final double  MAX_ANGULAR_ERROR       = Math.PI / 4;
	public static final double  MAX_TRANSLATIONAL_ERROR = 12;
	public static final double  MAX_ELAPSED_TIME        = 0.1;
	public static final boolean USE_XY_PID              = true;
	//Debug info
	public static final boolean DEBUG_LOG               = true;
	public static final boolean VERBOSE_LOG             = false;
	//----------------------- OLD ------------------------------------
//	//Encoder limits
//	public static final int    ARM_MAX                       = 4450;
//	public static final int    COLLECT_ARM_INITIAL_EXTENSION = 2000;
//	//initial
//	// extensions during auto extend
//	public static final int    SCORE_ARM_INITIAL_EXTENSION   = ARM_MAX;
//	//Servo positions
//	//Collect door
//	// positions
//	public static final double SCORE_DOOR_CLOSED             = 0.9;
//	//score door
//	// positions;
//	public static final double SCORE_DOOR_READY              = 0.85;
//	public static final double SCORE_DOOR_GOLD               = 0.79;
//	public static final double SCORE_DOOR_OPEN               = 0.65;
//	public static final double PARKER_POSITION_HOME          = 0.6;
//	//Mults
//	public static final double SPEED_FAST_MULT               = 100;
//	public static final double SPEED_NORMAL_MULT             = 1;
//	public static final double SPEED_SLOW_MULT               = 0.4;
//	//Powers
//	public static final double IDLE_IN_POWER                 = -0.6;
//	public static final double IDLE_COLLECT_ARM_POWER        = 0.05;
//	public static final double IDLE_SCORE_ARM_POWER          = 0.1;
//	public static final double          IDLE_SCOOPER_POWER = 0.6;
	
	private Constants() {
	}
}
