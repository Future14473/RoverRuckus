package org.firstinspires.ftc.teamcode.RoverRuckus.util.timer;

public class SingleSimpleTimer implements SimpleTimer {
	private long pastTime = System.nanoTime();
	
	@Override
	public long getNanos() {
		return System.nanoTime() - pastTime;
	}
	
	@Override
	public void reset() {
		pastTime = System.nanoTime();
	}
	
	@Override
	public double getSecondsAndReset() {
		long curTime = System.nanoTime();
		double o = (curTime - pastTime) / 1e9;
		pastTime = curTime;
		return o;
	}
}
