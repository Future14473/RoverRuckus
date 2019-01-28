package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MotorSetPower.ZERO;

/**
 * Represents a set of motors in the following order: <br>
 * [fl, fr, bl, br]
 * Immutable.
 *
 * @see MotorSetPower
 */
//immutable
public final class MotorSet implements Iterable<DcMotor> {
	private final List<DcMotor> motors;
	
	public MotorSet(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {
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
	 */
	public void setPower(MotorSetPower power) {
		power = power.scaled();
		for (int i = 0; i < 4; i++) {
			motors.get(i).setPower(power.power[i]);
		}
	}
	
	public MotorSetPosition getCurrentPosition() {
		MotorSetPosition o = new MotorSetPosition();
		for (int i = 0; i < 4; i++) {
			o.position[i] = motors.get(i).getCurrentPosition();
		}
		return o;
	}
	
	/**
	 * Sets all the motors' target position to the given {@link MotorSetPosition}
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
	 */
	public void setMode(DcMotor.RunMode mode) {
		for (DcMotor i : this) {
			i.setMode(mode);
		}
	}
	
	public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
		for (DcMotor t : this) {
			t.setZeroPowerBehavior(behavior);
		}
	}
	
	@Override
	public Iterator<DcMotor> iterator() {
		return motors.iterator();
	}
}
