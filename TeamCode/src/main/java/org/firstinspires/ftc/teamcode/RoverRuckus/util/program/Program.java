package org.firstinspires.ftc.teamcode.RoverRuckus.util.program;

import java.util.ArrayList;
import java.util.Iterator;

public class Program implements Runnable {
	private final ArrayList<Runnable> runnables = new ArrayList<>();
	
	public void add(Runnable runnable) {
		runnables.add(runnable);
	}
	
	@Override
	public void run() {
		for (Iterator<Runnable> iterator = runnables.iterator(); iterator.hasNext() && !Thread.interrupted(); ) {
			Runnable runnable = iterator.next();
			runnable.run();
		}
	}
	
	public static Runnable sleep(final int millis) {
		return () -> {
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
	}
}
