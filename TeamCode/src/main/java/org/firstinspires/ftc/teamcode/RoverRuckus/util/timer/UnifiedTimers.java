package org.firstinspires.ftc.teamcode.RoverRuckus.util.timer;

public class UnifiedTimers {
	private long curTime = System.nanoTime();
	
	public void update() {
		curTime = System.nanoTime();
	}
	
	public SimpleTimer newTimer() {
		return new Timer();
	}
	
	private class Timer implements SimpleTimer {
		private long pastTime = curTime;
		
		private Timer() {
		}
		
		@Override
		public long getNanos() {
			return curTime - pastTime;
		}
		
		@Override
		public void reset() {
			pastTime = curTime;
		}
	}
}
