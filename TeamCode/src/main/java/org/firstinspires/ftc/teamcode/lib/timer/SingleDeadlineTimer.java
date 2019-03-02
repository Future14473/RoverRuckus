package org.firstinspires.ftc.teamcode.lib.timer;

public class SingleDeadlineTimer implements DeadlineTimer {
	private long deadLine;
	
	@Override
	public long nanosToDeadline() {
		return deadLine - getCurTime();
	}
	
	private long getCurTime() {
		return System.nanoTime();
	}
	
	@Override
	public void addToDeadline(long nanos) {
		deadLine += nanos;
	}
	
	@Override
	public void resetDeadline() {
		deadLine = getCurTime();
	}
	
}
