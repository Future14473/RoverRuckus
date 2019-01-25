package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

class MotorPowerSet {
	static final MotorPowerSet ZERO = new MotorPowerSet();
	static double MOVE_MULT = 4450; //change to tweak "move x meters" precisely. Degrees wheel turn per unit.
	static double TURN_MULT = 1205; //change to tweak "rotate x deg" precisely.   Degrees wheel turn per
	double[] power;
	
	MotorPowerSet(double fl, double fr, double bl, double br) {
		this.power = new double[]{fl, fr, bl, br};
	}
	
	MotorPowerSet() {
		this.power = new double[4];
	}
	
	MotorPowerSet scaled() {
		MotorPowerSet set = new MotorPowerSet();
		double max = 1;
		for (int i = 0; i < 4; i++) {
			max = Math.max(max, Math.abs(power[i]));
		}
		for (int i = 0; i < 4; i++) {
			set.power[i] = power[i] / max;
		}
		return set;
	}
	
	static MotorPowerSet calcPowerSet(double direction, double turnRate, double speed) {
		double robotAngle = direction + Math.PI / 4;
		double v1 = speed * Math.sin(robotAngle) + turnRate;
		double v2 = speed * Math.cos(robotAngle) - turnRate;
		double v3 = speed * Math.cos(robotAngle) + turnRate;
		double v4 = speed * Math.sin(robotAngle) - turnRate;
		return new MotorPowerSet(v1, v2, v3, v4);
	}
	
}
