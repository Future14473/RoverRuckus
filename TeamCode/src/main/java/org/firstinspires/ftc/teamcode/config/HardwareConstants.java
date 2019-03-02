package org.firstinspires.ftc.teamcode.config;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import static com.qualcomm.robotcore.hardware.MotorControlAlgorithm.LegacyPID;

@SuppressWarnings("deprecation")
public final class HardwareConstants {
	public static final PIDFCoefficients RUN_USING_ENCODER_PIDF =
			new PIDFCoefficients(10, 3, 0, 0, LegacyPID);
	public static final PIDFCoefficients RUN_TO_POSITION_PIDF   =
			new PIDFCoefficients(10, 0.05, 0, 0, LegacyPID);
	
	public static final double X_MULT = 1.2;
	
	private HardwareConstants() {
	}
}
