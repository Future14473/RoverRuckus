package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MotorPowerSet.ZERO;

/**
 * Represents a set of motors in the following order: <br>
 * [fl, fr, bl, br]
 * Immutable.
 *
 * @see MotorPowerSet
 */
//immutable
public final class MotorSet {
	private final DcMotor[] motors;
	
	public MotorSet(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {
		motors = new DcMotor[]{fl, fr, bl, br};
	}
	
	/**
	 * Sets all the motors' power level to 0.
	 */
	public void stop() {
		setPowerTo(ZERO);
	}
	
	/**
	 * Sets all the motors' power to the given {@link MotorPowerSet}
	 */
	public void setPowerTo(MotorPowerSet powerSet) {
		powerSet = powerSet.scaled();
		for (int i = 0; i < 4; i++) {
			motors[i].setPower(powerSet.power[i]);
		}
	}
	
	/**
	 * Gets a motor.
	 *
	 * @return the motor.
	 */
	DcMotor get(int i) {
		return motors[i];
	}
	
	/**
	 * Sets all the motors' mode
	 */
	public void setModeTo(DcMotor.RunMode mode) {
		for (int i = 0; i < 4; i++) {
			motors[i].setMode(mode);
		}
	}
}
