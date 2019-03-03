package org.firstinspires.ftc.teamcode.config;

import org.firstinspires.ftc.teamcode.lib.navigation.Magnitudes;

public final class TeleopAndAutoConstants {
	// Encoder limits ------------------------------------------------------------
	public static final int MOTOR_MIN                     = 20;
	public static final int COLLECT_ARM_MAX               = 1300;
	public static final int COLLECT_ARM_INITIAL_EXTENSION = 600;
	public static final int COLLECT_ARM_AWAY              = 30;
	public static final int SCORE_ARM_MAX                 = 1800;
	public static final int AUTO_DUMP_MIN_POSITION        = SCORE_ARM_MAX * 4 / 5;
	public static final int DUMP_ALLOW_POSITION           = SCORE_ARM_MAX / 2;
	public static final int SCORE_ARM_INITIAL_EXTENSION   = SCORE_ARM_MAX - 50;
	public static final int HOOK_MAX                      = 11000;
	public static final int HOOK_NULLIFY                  = 1000;
	public static final int HOOK_INITIAL                  = 1000;
	//following is negative
	public static final int HOOK_TURN_START_LOOK          = -7000;
	public static final int HOOK_TURN_END                 = -11000;
	
	// Powers -----------------------------------------------------------------
	public static final double SCOOPER_IDLE_POWER   = 0.2;
	public static final double SCORE_ARM_IN_POWER   = -0.6;
	public static final double COLLECT_ARM_IN_POWER = -0.35;
	public static final double BUMPER_POWER         = 0.75;
	
	//Servo positions
	public static final double MARKER_DOOR_OPEN    = 0.90;
	public static final double FLICKER_OUT         = 0.10;
	public static final double COLLECT_DOOR_CLOSED = 0.68;
	public static final double COLLECT_DOOR_OPEN   = 0.30;
	public static final double SCORE_DUMP_HOME     = 0.62;
	public static final double SCORE_DUMP_DOWN     = 0.20;
	public static final double SCORE_DUMP_WIGGLE   = 0.08;
	
	// Mults -------------------------------------------------------------------
	public static final double SPEED_MULT_NORM = 1.3;
	public static final double SPEED_MULT_SLOW = 0.4;
	
	// Times -------------------------------------------------------------------
	public static final double TRANSFER_SLEEP_TIME     = 0;
	public static final double AUTO_DUMP_TRANSFER_TIME = 1.1;
	public static final double INTERVAL_TIME           = 0.5;
	public static final double UPDATE_LOCATION_TIME    = 0.1;
	
	// Auto dump
	public static final Magnitudes AUTO_DUMP_TOLERANCE =
			new Magnitudes(3, Math.toRadians(6));
	
	private TeleopAndAutoConstants() {
	}
}
