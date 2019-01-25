package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MotorPowerSet.ZERO;

//immutable
public final class MotorSet {
	private final DcMotor[] motorList;
	
	public MotorSet(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {
		motorList = new DcMotor[]{fl, fr, bl, br};
	}
	
	public void stop() {
		setPowerTo(ZERO);
	}
	
	void setPowerTo(MotorPowerSet powerSet) {
		powerSet = powerSet.scaled();
		for (int i = 0; i < 4; i++) {
			this.get(i).setPower(powerSet.power[i]);
		}
	}
	
	public DcMotor get(int i) {
		return motorList[i];
	}
}
