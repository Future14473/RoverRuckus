package org.firstinspires.ftc.teamcode.RoverRuckus;

import org.jetbrains.annotations.TestOnly;

@TestOnly
public class MockMover {
	private double  position;
	private double  speed;
	private double  noise;
	private boolean enabled = true;
	private double  lag;
	
	public void update(double mult) {
		if (!enabled) return;
		this.position += mult * (speed * (1 + (Math.random() - .5) * getNoise()));
	}
	
	public double getPosition() {
		return position;
	}
	
	public void setPosition(double position) {
		this.position = position;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		double diff = Math.sqrt(this.speed - speed) * lag;
		this.speed = constrain(speed, this.speed - diff, this.speed + diff);
	}
	
	private double constrain(double v, double min, double max) {
		return v < min ? min : v > max ? max : v;
	}
	
	public double getNoise() {
		return noise;
	}
	
	public void setNoise(double noise) {
		this.noise = noise;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public void setLag(double lag) {
		this.lag = lag;
	}
	
	public double getLag() {
		return lag;
	}
}
