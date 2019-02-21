package org.firstinspires.ftc.teamcode.RoverRuckus;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSet;
import org.jetbrains.annotations.TestOnly;

@TestOnly
public class MockRobot implements IRobot {
	public MockMotorSetWrapper mockSet = new MockMotorSetWrapper();
	
	@Override
	public MotorSet getWheels() {
		return mockSet.getMockMotorSet();
	}
	
	@Override
	public double getAngle() {
		return 0;//TODO: STUB
	}
}
