package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import android.support.annotation.CallSuper;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;

/**
 * A class that facilitates making ModifiedLinearOpMode
 */
public abstract class OurLinearOpMode extends LinearOpMode {
	private final SingleWaiter waiter = new SingleWaiter();
	protected Robot robot;
	
	/**
	 * This method is loop once upon initialization.
	 * Put any additional initialization code here.
	 */
	protected abstract void initialize() throws InterruptedException;
	
	/**
	 * This method is loop after the start button is pressed. OpMode will stop when this
	 * method returns or throws InterruptedException
	 *
	 * @throws InterruptedException to stop the OpMode early.
	 */
	protected abstract void run() throws InterruptedException;
	
	@Override
	public final void runOpMode() throws InterruptedException {
		try {
			telemetry.addLine("Init started...");
			telemetry.addLine("Please wait before pressing start");
			telemetry.update();
			robot = new Robot(hardwareMap);
			initialize();
			telemetry.addLine("Init done");
			telemetry.addLine("You may now press start");
			telemetry.update();
			waitForStart();
			run();
		} finally {
			cleanup();
		}
	}
	
	/**
	 * Override this method and place cleanup code here.
	 * This method will always loop after the OpMode stops: through
	 * normal exit, exception thrown, or OpMode stopped early.
	 * If a RuntimeException is thrown during cleanup, it is ignored.
	 */
	protected void cleanup() {}
	
	@Override
	@CallSuper
	protected void handleLoop() {
		super.handleLoop();
		//signal any waiters
		waiter.update();
	}
	
	/**
	 * Pauses the current thread indefinitely until a given condition (checked with loop) occurs
	 *
	 * @throws InterruptedException if this thread is interrupted while waiting (op mode stopped).
	 */
	protected void waitUntil(BooleanSupplier condition) throws InterruptedException {
		RobotLog.d("WaitUntil started: " + condition.toString());
		this.waiter.waitFor(condition);
		RobotLog.d("WaitUntil ended " + condition.toString());
	}
	
	/**
	 * Pauses the current thread until a given condition occurs, or after timeout.
	 *
	 * @throws InterruptedException if this thread is interrupted while waiting (op mode stopped).
	 */
	protected void waitUntil(BooleanSupplier waitCondition, long timeout, TimeUnit unit) throws InterruptedException {
		long nanos = unit.toNanos(timeout);
		final long stopTime = System.nanoTime() + nanos;
		waitUntil(() -> waitCondition.getAsBoolean() || System.nanoTime() >= stopTime);
	}
	
	private static class SingleWaiter {
		private final AtomicInteger waiters = new AtomicInteger();
		private BooleanSupplier condition;
		
		protected SingleWaiter() {}
		
		protected synchronized void waitFor(BooleanSupplier condition) throws InterruptedException {
			this.notifyAll(); //screw all other waiters, if any
			this.condition = condition;
			if (condition.getAsBoolean()) return;
			waiters.incrementAndGet();
			wait();
			waiters.decrementAndGet();
		}
		
		protected void update() {
			if (waiters.get() == 0) return;
			synchronized (this) {
				if (condition.getAsBoolean()) {
					this.notifyAll();
				}
			}
		}
	}
}
