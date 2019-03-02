package org.firstinspires.ftc.teamcode.lib.timer;

public class SingleTimer implements Timer {
	private long pastTime = getCurTime();
	
	@Override
	public long getNanos() {
		return getCurTime() - pastTime;
	}
	
	private long getCurTime() {
		return System.nanoTime();
	}
	
	@Override
	public void reset() {
		pastTime = getCurTime();
	}
	
	@Override
	public double getSecondsAndReset() {
		long curTime = getCurTime();
		double o = (curTime - pastTime) / 1e9;
		pastTime = curTime;
		return o;
	}
}
