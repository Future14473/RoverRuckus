package org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.*;

/**
 * A wrapper around a DcMotor that limits the motor's range of motion by reading
 * encoder values.
 */
public class LimitedMotor {
	
	private final DcMotorEx motor;
	private final Integer   lowerLimit;
	private final Integer   upperLimit;
	
	private State   lastState = State.NONE;
	private boolean encoderReverse;
	
	/**
	 * Creates a new LimitedMotor.
	 *
	 * @param motor          the motor
	 * @param lowerLimit     encoder lower limit;
	 * @param upperLimit     encoder upper limit;
	 * @param encoderReverse if the encoder values are negated (workaround
	 *                       hardware issues).
	 */
	public LimitedMotor(
			DcMotorEx motor, Integer lowerLimit, Integer upperLimit,
			boolean encoderReverse) {
		if (upperLimit < lowerLimit) throw new IllegalArgumentException();
		this.motor = motor;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.encoderReverse = encoderReverse;
		if (encoderReverse) {//note that this is a test and it might fail
			PIDFCoefficients coefficients = motor.getPIDFCoefficients(RUN_USING_ENCODER);
			coefficients.p *= -1;
			coefficients.i *= -1;
			coefficients.d *= -1;
			coefficients.f *= -1;
			motor.setPIDFCoefficients(RUN_USING_ENCODER, coefficients);
		}
		motor.setMode(RUN_USING_ENCODER);
	}
	
	public void resetEncoder() {
		motor.setMode(STOP_AND_RESET_ENCODER);
		motor.setMode(encoderReverse ? RUN_WITHOUT_ENCODER : RUN_USING_ENCODER);
	}
	
	public void setPowerLimited(double power) {
		setPowerLimited(power, null, null, false);
	}
	
	public void setPowerLimited(double power, boolean override) {
		setPowerLimited(power, null, null, override);
	}
	
	public void setPowerLimited(
			double power, Integer lowerLimit, Integer upperLimit) {
		setPowerLimited(power, lowerLimit, upperLimit, false);
	}
	
	private void setPowerLimited(
			double power, Integer lowerLimit,
			Integer upperLimit, boolean override) {
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
	}
	
	public State getLastState() {
		return lastState;
	}
	
	private int getCurrentPositionAdjusted() {
		return motor.getCurrentPosition() * (encoderReverse ? -1 : 1);
	}
	
	public enum State {
		NONE,
		LOWER,
		UPPER
	}
}
