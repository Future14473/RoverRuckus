package org.firstinspires.ftc.teamcode.RoverRuckus;

import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.Magnitudes;

import static com.qualcomm.robotcore.hardware.MotorControlAlgorithm.LegacyPID;

@SuppressWarnings("deprecation")
public class Constants {
	//Encoder limits -----------------------
	//"at home" position for all encoders
	public static final int        MOTOR_MIN                     = 20;
	//maximum arm extension
	public static final int        COLLECT_ARM_MAX               = 1200; //changed
	public static final int        COLLECT_ARM_INITIAL_EXTENSION = 600; //changed
	public static final Integer    COLLECT_ARM_AWAY              = 30;
	public static final int        SCORE_ARM_MAX                 = 1800;
	public static final int        SCORE_ARM_INITIAL_EXTENSION   = SCORE_ARM_MAX;
	public static final int        DUMP_ALLOW_POSITION           = SCORE_ARM_MAX / 2;
	public static final int        AUTO_DUMP_MIN_POSITION        = SCORE_ARM_MAX * 4 / 5;
	public static final int        HOOK_MAX                      = 11000;
	public static final int        HOOK_NULLIFY                  = 1000;
	public static final int        HOOK_INITIAL                  = 1300;
	//yes, is negative, due to different implementations
	public static final int        HOOK_TURN_START_LOOK          = -7000;
	//Powers -----------------------
	public static final double     SCOOPER_IDLE_POWER            = 0.2;
	public static final double     SCORE_ARM_IN_POWER            = -0.6;
	public static final double     COLLECT_ARM_IN_POWER          = -0.35;
	public static final double     COLLECT_ARM_MAX_IDLE_POWER    = 0.6;
	//Auto dump
	public static final Magnitudes AUTO_DUMP_TOLERANCE           =
			new Magnitudes(6,
			               Math.toRadians(6));
	// Servo positions -----------------------
	
	public static final double           PARKER_POSITION_OUT       = 0;
	public static final double           MARKER_DOOR_OPEN          = 0.9;
	public static final double           FLICKER_OUT               = 0.1;
	//Collect door positions
	public static final double           COLLECT_DOOR_CLOSED       = 0.71;
	public static final double           COLLECT_DOOR_OPEN         = 0.30;
	//Score dump positions
	public static final double           SCORE_DUMP_HOME           = 0.58;
	public static final double           SCORE_DUMP_DOWN           = 0;
	//parker
	public static final double           PARKER_HOME               = 0.6;
	public static final double           SPEED_MULT_NORM           = 1.3;
	public static final double           SPEED_MULT_SLOW           = 0.4;
	//Other constants -----------------------
	public static final int              TRANSFER_SLEEP_TIME       = 0;
	//after being down for so long, how long to wait before auto transfer
	public static final int              AUTO_DUMP_TRANSFER_TIME   = 1000;
	public static final int              INTERVAL_TIME             = 500;
	public static final int              UPDATE_LOCATION_TIME      = 150;
	//Motor stuff ---------------------------
	public static final PIDFCoefficients RUN_USING_ENCODER_PIDF    =
			new PIDFCoefficients(10, 3, 0, 0, LegacyPID);
	public static final PIDFCoefficients RUN_TO_POSITION_PIDF      =
			new PIDFCoefficients(10, 0.05, 0, 0, LegacyPID);
	//For movement tracking -----------------------
	public static final double           ENCODER_TICKS_PER_INCH    = 125;
	public static final double           DEFAULT_MAX_ACCELERATION  = 4;
	public static final Magnitudes       DEFAULT_MAX_ACCELERATIONS =
			new Magnitudes(DEFAULT_MAX_ACCELERATION);
	
	//For PIDs -----------------------
	public static final PIDCoefficients PID_COEFFICIENTS_TRANSLATIONAL =
			new PIDCoefficients(0.18, 0, 1.5);
	public static final PIDCoefficients PID_COEFFICIENTS_ANGULAR       =
			new PIDCoefficients(1.6, 0, 3.5);
	
	public static final double  MAX_ANGULAR_ERROR       = Math.PI / 2;
	public static final double  MAX_TRANSLATIONAL_ERROR = 16;
	public static final double  MAX_ELAPSED_TIME        = 0.1;
	public static final boolean USE_XY_PID              = true;
	//Debug info
	public static final boolean DEBUG_LOG               = true;
	public static final boolean VERBOSE_LOG             = false;
	@SuppressWarnings("SpellCheckingInspection")
	public static final String  VUFORIA_KEY             =
			"Aavay7//////AAABmS26wV70nE/XoqC91tMM/rlwbqInv/YUads4QRll085q/yT" +
			"+qW0qdyrUwXPXbvwDkGhnffFMGIizzvfrXviNCbfAAgJzSwDJuL0MJl3LRE2FU4JMKKU2v7V" +
			"+XGChhH91BXriKEtx4PDCq5DwSpCT1TP3XSJrouflaIEdqxTcUz/LaIEh4phJs35awBUu+g" +
			"+4i3EKMJBsYWyJ0V9jdI5DLCVhXkKtBpKgJbO3XFx40Ig/HFXES1iUaOk2fj9SG/jRUsWLH1cs35" +
			"/g289Xs6BTQTHnGpX9bcOvK0m4NkhogjqbT7S76O91jeheUZwazesROu848shb317YhWIclBSR/vV9/I2fT" +
			"+485YdwnaxuS8K9";
	
	private Constants() {
	}
}
