package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.*;

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
	private State lastState = State.NONE;
	
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
		motor.setMode(encoderReverse ? RUN_WITHOUT_ENCODER : RUN_USING_ENCODER);
	}
	
	public void resetEncoder() {
		motor.setMode(STOP_AND_RESET_ENCODER);
		motor.setMode(RUN_USING_ENCODER);
	}
	
	public State setPowerLimited(double power) {
		return setPowerLimited(power, null, null, false);
	}
	
	public State setPowerLimited(double power, boolean override) {
		return setPowerLimited(power, null, null, override);
	}
	
	public State setPowerLimited(double power, Integer lowerLimit, Integer upperLimit) {
		return setPowerLimited(power, lowerLimit, upperLimit, false);
	}
	
	public State setPowerLimited(double power, Integer lowerLimit, Integer upperLimit, boolean override) {
		if (lowerLimit == null) lowerLimit = this.lowerLimit;
		if (upperLimit == null) upperLimit = this.upperLimit;
		State state = State.NONE;
		if (power < 0) {
			if (getCurrentPositionAdjusted() < lowerLimit) {
				state = State.LOWER;
			}
		} else if (power > 0) {
			if (getCurrentPositionAdjusted() > upperLimit) {
				state = State.UPPER;
			}
		}
		motor.setPower(override || state == State.NONE ? power : 0);
		lastState = state;
		return state;
	}
	
	public State getLastState() {
		return lastState;
	}
	
	private int getCurrentPositionAdjusted() {
		return motor.getCurrentPosition() * encoderMult;
	}
	
	public void pseudoSetTargetPosition(int position) {
		position = Range.clip(position, lowerLimit, upperLimit);
		targetPosition = position;
	}
	
	public enum State {
		NONE,
		LOWER,
		UPPER
	}
}
