package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem;

import android.support.annotation.NonNull;

import java.util.concurrent.Callable;

/**
 * A java.util.concurrent.FutureTask\<V\>that fits
 * our Task system.
 */
public class FutureTask<V> extends java.util.concurrent.FutureTask<V> implements Task {
	
	public FutureTask(@NonNull Callable<V> callable) {
		super(callable);
	}
	
	public FutureTask(@NonNull Runnable runnable, V result) {
		super(runnable, result);
	}
}
