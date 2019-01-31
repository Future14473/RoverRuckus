package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

/**
 * A wrapper around a DcMotor that limits the motor's range of motion by reading
 * encoder values.
 */
public class LimitedMotor {
	public final DcMotor motor;
	private final Integer lowerLimit;
	private final Integer upperLimit;
	private final int encoderMult;
	
	private int targetPosition;
	private LimitState lastLimitState = LimitState.NONE;
	
	/**
	 * @param motor          the motor
	 * @param lowerLimit     encoder lower limit;
	 * @param upperLimit     encoder upper limit;
	 * @param encoderReverse if the encoder values are negated (workaround hardware issues).
	 */
	public LimitedMotor(DcMotor motor, int lowerLimit, int upperLimit, boolean encoderReverse) {
		if (upperLimit < lowerLimit) throw new IllegalArgumentException();
		this.motor = motor;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.encoderMult = encoderReverse ? -1 : 1;
		motor.setMode(RUN_USING_ENCODER);
	}
	
	public void resetEncoder() {
		motor.setMode(STOP_AND_RESET_ENCODER);
		motor.setMode(RUN_USING_ENCODER);
	}
	
	public LimitState setPowerLimited(double power) {
		return setPowerLimited(power, null, null, false);
	}
	
	public LimitState setPowerLimited(double power, boolean override) {
		return setPowerLimited(power, null, null, override);
	}
	
	public LimitState setPowerLimited(double power, Integer lowerLimit, Integer upperLimit) {
		return setPowerLimited(power, lowerLimit, upperLimit, false);
	}
	
	public LimitState setPowerLimited(double power, Integer lowerLimit, Integer upperLimit, boolean override) {
		if (lowerLimit == null) lowerLimit = this.lowerLimit;
		if (upperLimit == null) upperLimit = this.upperLimit;
		LimitState limitState = LimitState.NONE;
		if (power < 0) {
			if (getCurrentPositionAdjusted() < lowerLimit) {
				limitState = LimitState.LOWER;
			}
		} else if (power > 0) {
			if (getCurrentPositionAdjusted() > upperLimit) {
				limitState = LimitState.UPPER;
			}
		}
		motor.setPower(override || limitState == LimitState.NONE ? power : 0);
		lastLimitState = limitState;
		return limitState;
	}
	
	public LimitState getLastLimitState() {
		return lastLimitState;
	}
	
	private int getCurrentPositionAdjusted() {
		return motor.getCurrentPosition() * encoderMult;
	}
	
	public void pseudoSetTargetPosition(int position) {
		position = Range.clip(position, lowerLimit, upperLimit);
		targetPosition = position;
	}
	
	public enum LimitState {
		NONE,
		LOWER,
		UPPER
	}
}
