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
