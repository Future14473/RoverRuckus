package org.firstinspires.ftc.teamcode.RoverRuckus;

public class Constants {
	//Encoder limits
	//"at home" position for all encoders
	public static final int    MOTOR_MIN                     = 100;
	//maximum arm extension (both arms)
	public static final int    ARM_MAX                       = 4450;
	//maximum hook extension
	public static final int    HOOK_MAX                      = 26000;
	public static final int    HOOK_NULLIFY                  = 1500;
	//initial extensions during auto extend
	public static final int    INITIAL_EXTENSION_COLLECT_ARM = 2000;
	public static final int    INITIAL_EXTENSION_SCORE_ARM_  = ARM_MAX;
	//Servo positions
	//Collect door positions
	public static final double COLLECT_DOOR_CLOSED           = 0.71;
	public static final double COLLECT_DOOR_OPEN             = 0.39;
	//Score dump positions
	public static final double SCORE_DUMP_HOME               = 0.65;
	public static final double SCORE_DUMP_DOWN               = 0;
	//parker
	public static final double PARKER_HOME                   = 0.6;
	//Mults
	public static final double SPEED_MULT_FAST               = 100;
	public static final double SPEED_MULT_NORM               = 1;
	public static final double SPEED_MULT_SLOW               = 0.4;
	//Powers
	public static final double IDLE_POWER_IN                 = -0.6;
	public static final double IDLE_POWER_COLLECT_ARM        = 0.05;
	public static final double IDLE_POWER_SCORE_ARM          = 0.1;
	public static final double IDLE_POWER_SCOOPER            = 0.6;
	//Other constants
	public static final int    TRANSFER_SLEEP_TIME           = 200;
	//----------------------- OLD ------------------------------------
	//Encoder limits
	public static final int    COLLECT_ARM_INITIAL_EXTENSION = 2000;
	//initial
	// extensions during auto extend
	public static final int    SCORE_ARM_INITIAL_EXTENSION   = ARM_MAX;
	//Servo positions
	//Collect door
	// positions
	public static final double SCORE_DOOR_CLOSED             = 0.9;
	//score door
	// positions;
	public static final double SCORE_DOOR_READY              = 0.85;
	public static final double SCORE_DOOR_GOLD               = 0.79;
	public static final double SCORE_DOOR_OPEN               = 0.65;
	public static final double PARKER_POSITION_HOME          = 0.6;
	//Mults
	public static final double SPEED_FAST_MULT               = 100;
	public static final double SPEED_NORMAL_MULT             = 1;
	public static final double SPEED_SLOW_MULT               = 0.4;
	//Powers
	public static final double IDLE_IN_POWER                 = -0.6;
	public static final double IDLE_COLLECT_ARM_POWER        = 0.05;
	public static final double IDLE_SCORE_ARM_POWER          = 0.1;
	public static final double IDLE_SCOOPER_POWER            = 0.6;
	//For movement tracking
	public static final double ENCODER_TICKS_PER_INCH        = 125;
	public static final double DEFAULT_MAX_ACCELERATION      = 5;
	//For movePID
	public static final double MAX_ANGULAR_ERROR             = Math.PI / 4;
	public static final double MAX_TRANSLATIONAL_ERROR       = 12;
	public static final double MAX_ELAPSED_TIME              = 0.1;
	
	private Constants() {
	}
}
