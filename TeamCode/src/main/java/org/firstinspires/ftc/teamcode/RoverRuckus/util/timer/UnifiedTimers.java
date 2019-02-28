package org.firstinspires.ftc.teamcode.RoverRuckus.util.timer;

public class UnifiedTimers {
	private long curTime = System.nanoTime();
	
	public void update() {
		curTime = System.nanoTime();
	}
	
	public Timer newTimer() {
		return new TiedTimer();
	}
	
	public DeadlineTimer newDeadlineTimer() {
		return new TiedDeadlineTimer();
	}
	
	private class TiedTimer implements Timer {
		private long pastTime = curTime;
		
		private TiedTimer() {
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
	
	private class TiedDeadlineTimer implements DeadlineTimer {
		private long deadLine = curTime;
		
		private TiedDeadlineTimer() {
		}
		
		@Override
		public long nanosToDeadline() {
			return deadLine - curTime;
		}
		
		@Override
		public void addToDeadline(long nanos) {
			deadLine += nanos;
		}
		
		@Override
		public void resetDeadline() {
			deadLine = curTime;
		}
	}
}
