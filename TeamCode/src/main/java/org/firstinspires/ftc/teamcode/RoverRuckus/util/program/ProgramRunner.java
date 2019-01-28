package org.firstinspires.ftc.teamcode.RoverRuckus.util.program;

public class ProgramRunner {
	private Thread thread;
	
	public void runProgram(Program program) {
		thread = new Thread(program);
	}
	
	public void stopProgram() {
		thread.interrupt();
	}
	
	public boolean isDone() {
		return thread.isAlive();
	}
}
