package org.firstinspires.ftc.teamcode.RoverRuckus.robottasks;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.MotorSetPower.ZERO;

/**
 * Represents a set of motors in the following order: <br>
 * [fl, fr, bl, br]
 * Immutable.
 *
 * @see MotorSetPower
 */
//immutable
public final class MotorSet implements Iterable<DcMotorEx> {
	private final List<DcMotorEx> motors;
	
	public MotorSet(DcMotorEx fl, DcMotorEx fr, DcMotorEx bl, DcMotorEx br) {
		motors = Arrays.asList(fl, fr, bl, br);
	}
	
	/**
	 * Sets all the motors' power level to 0.
	 */
	public void stop() {
		setPower(ZERO);
	}
	
	/**
	 * Sets all the motors' power to the given {@link MotorSetPower}
	 *
	 * @param power the power to set the motors
	 */
	public void setPower(MotorSetPower power) {
		power = power.scaled();
		for (int i = 0; i < 4; i++) {
			motors.get(i).setPower(power.power[i]);
		}
	}
	
	/**
	 * returns a MotorSetPosition representing all the motors's current position.
	 *
	 * @return a MotorSetPosition representing all the motors's current position.
	 */
	public MotorSetPosition getCurrentPosition() {
		MotorSetPosition o = new MotorSetPosition();
		for (int i = 0; i < 4; i++) {
			o.position[i] = motors.get(i).getCurrentPosition();
		}
		return o;
	}
	
	/**
	 * Sets all the motors' target position to the given {@link MotorSetPosition}
	 *
	 * @param position the target position
	 */
	public void setTargetPosition(MotorSetPosition position) {
		for (int i = 0; i < 4; i++) {
			motors.get(i).setTargetPosition(position.position[i]);
		}
	}
	
	/**
	 * Gets a motor.
	 *
	 * @return the motor.
	 */
	public DcMotor get(int i) {
		return motors.get(i);
	}
	
	/**
	 * Sets all the motors' mode
	 *
	 * @param mode the mode to set
	 */
	public void setMode(DcMotor.RunMode mode) {
		for (DcMotor m : this) {
			m.setMode(mode);
		}
	}
	
	/**
	 * Sets all motors' zero power behavior.
	 *
	 * @param behavior the zero power behavior
	 */
	public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
		for (DcMotor m : this) {
			m.setZeroPowerBehavior(behavior);
		}
	}
	
	/**
	 * Returns an iterator to the motors in this MotorSet
	 *
	 * @return an iterator to the motors
	 */
	@Override
	public Iterator<DcMotorEx> iterator() {
		return motors.iterator();
	}
	
	/**
	 * Returns the target position tolerance of the first motor, in encoder ticks, usually indicating what all the
	 * motors's target position is.
	 *
	 * @return the target position tolerance of the first motor, in encoder ticks.
	 */
	public int getTargetPositionTolerance() {
		return motors.get(0).getTargetPositionTolerance();
	}
	
	/**
	 * Sets all the motors's target position tolerance to the given tolerance
	 *
	 * @param tolerance the desired tolerance, in encoder ticks.
	 */
	public void setTargetPositionTolerance(int tolerance) {
		for (DcMotorEx m : this) {
			m.setTargetPositionTolerance(tolerance);
		}
	}
}
