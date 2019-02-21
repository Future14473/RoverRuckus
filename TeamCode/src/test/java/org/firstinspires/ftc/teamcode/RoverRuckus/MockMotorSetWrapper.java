package org.firstinspires.ftc.teamcode.RoverRuckus;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSet;
import org.jetbrains.annotations.TestOnly;

@TestOnly
public class MockMotorSetWrapper {
	private final MotorSet        motorSet;
	private final MockDcMotorEx[] mockDcMotorExes;
	
	public MockMotorSetWrapper() {
		mockDcMotorExes = new MockDcMotorEx[4];
		for (int i = 0; i < 4; i++) {
			mockDcMotorExes[i] = new MockDcMotorEx();
		}
		motorSet = new MotorSet(mockDcMotorExes);
	}
	
	public void setNoise(double noise) {
		for (MockDcMotorEx ex : mockDcMotorExes) {
			ex.setNoise(noise);
		}
	}
	
	public MotorSet getMockMotorSet() {
		return motorSet;
	}
	
	public MockDcMotorEx[] getMockDcMotorExes() {
		return mockDcMotorExes;
	}
	
	public void update(double mult) {
		for (MockDcMotorEx ex : mockDcMotorExes) {
			ex.update(mult);
		}
	}
	
	public void setLag(double lag) {
		for (MockDcMotorEx ex : mockDcMotorExes) {
			ex.setLag(lag);
		}
	}
}
