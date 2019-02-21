package org.firstinspires.ftc.teamcode.RoverRuckus;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.jetbrains.annotations.TestOnly;

@TestOnly
public class MockDcMotorEx implements DcMotorEx {
	private final MockMover         mockMover;
	private       ZeroPowerBehavior zeroPowerBehavior;
	private       Direction         direction;
	
	public MockDcMotorEx() {
		this.mockMover = new MockMover();
	}
	
	@Override
	public void setMotorEnable() {
		mockMover.setEnabled(true);
	}
	
	@Override
	public void setMotorDisable() {
		mockMover.setEnabled(false);
	}
	
	@Override
	public boolean isMotorEnabled() {
		return mockMover.getEnabled();
	}
	
	@Override
	public void setVelocity(double angularRate) {
		//radians/second;
		if (angularRate != 0 || this.zeroPowerBehavior == ZeroPowerBehavior.FLOAT)
			mockMover.setSpeed(angularRate);
	}
	
	@Override
	public void setVelocity(double angularRate, AngleUnit unit) {
		setVelocity(unit.toRadians(angularRate));
	}
	
	@Override
	public double getVelocity() {
		return mockMover.getSpeed();
	}
	
	@Override
	public double getVelocity(AngleUnit unit) {
		return unit.fromRadians(mockMover.getSpeed());
	}
	
	@Override
	public void setPIDCoefficients(RunMode mode, PIDCoefficients pidCoefficients) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setPIDFCoefficients(RunMode mode, PIDFCoefficients pidfCoefficients)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setPositionPIDFCoefficients(double p) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public PIDCoefficients getPIDCoefficients(RunMode mode) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public PIDFCoefficients getPIDFCoefficients(RunMode mode) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setTargetPositionTolerance(int tolerance) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getTargetPositionTolerance() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public MotorConfigurationType getMotorType() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setMotorType(MotorConfigurationType motorType) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public DcMotorController getController() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getPortNumber() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
		
		this.zeroPowerBehavior = zeroPowerBehavior;
	}
	
	@Override
	public ZeroPowerBehavior getZeroPowerBehavior() {
		return zeroPowerBehavior;
	}
	
	@Override
	public void setPowerFloat() {
		this.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);
		this.setVelocity(0);
	}
	
	@Override
	public boolean getPowerFloat() {
		return zeroPowerBehavior == ZeroPowerBehavior.FLOAT && mockMover.getSpeed() == 0;
	}
	
	@Override
	public void setTargetPosition(int position) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getTargetPosition() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isBusy() {
		return mockMover.getSpeed() != 0;
	}
	
	@Override
	public int getCurrentPosition() {
		return (int) mockMover.getPosition();
	}
	
	@Override
	public void setMode(RunMode mode) {
		if (mode == RunMode.STOP_AND_RESET_ENCODER) {
			mockMover.setPosition(0);
		}
	}
	
	@Override
	public RunMode getMode() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	@Override
	public Direction getDirection() {
		return direction;
	}
	
	@Override
	public void setPower(double power) {
		setVelocity(power);
	}
	
	@Override
	public double getPower() {
		return mockMover.getSpeed();
	}
	
	@Override
	public Manufacturer getManufacturer() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getDeviceName() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getConnectionInfo() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getVersion() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void resetDeviceConfigurationForOpMode() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void close() {
		throw new UnsupportedOperationException();
	}
	
	public void setNoise(double noise) {
		mockMover.setNoise(noise);
	}
	
	public void update(double mult) {
		mockMover.update(mult);
	}
	
	public void setLag(double lag) {
		mockMover.setLag(lag);
	}
}
