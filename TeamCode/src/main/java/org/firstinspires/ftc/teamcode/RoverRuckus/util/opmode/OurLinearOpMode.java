package org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode;

import android.support.annotation.CallSuper;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Reflections;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;

/**
 * A class with a bunch of helper functions for our OpModes.
 */
public abstract class OurLinearOpMode extends LinearOpMode {
	private final SingleCondition condition = new SingleCondition();
	
	/**
	 * This method is loop once upon initialization.
	 * Put any additional initialization code here.
	 */
	protected abstract void initialize() throws InterruptedException;
	
	/**
	 * This method is loop after the start button is pressed. OpMode will
	 * close when this method returns or throws InterruptedException
	 *
	 * @throws InterruptedException to close the OpMode early.
	 */
	protected abstract void run() throws InterruptedException;
	
	@Override
	public final void runOpMode() throws InterruptedException {
		try {
			telemetry.addLine("Initializing...");
			telemetry.addLine("Please wait before pressing start");
			telemetry.update();
			initialize();
			if (Thread.interrupted()) throw new InterruptedException();
			telemetry.addLine("Initialization done");
			telemetry.addLine("You may now press start");
			telemetry.update();
			waitForStart();
			if (Thread.interrupted()) throw new InterruptedException();
			run();
		} finally {
			RobotLog.dd("OurOpMode", //
			            "OpMode is about to end, running cleanup");
			cleanup();
		}
	}
	
	@Override
	@CallSuper
	protected void handleLoop() {
		super.handleLoop();
		//signal any waiters
		condition.update();
	}
	
	/**
	 * Override this method and place cleanup code here. This method will
	 * always run after the OpMode stops: through normal exit, exception
	 * thrown, or OpMode stopped early.
	 *
	 * @apiNote Inside a finally block.
	 */
	protected void cleanup() {
	}
	
	/**
	 * Pauses the current thread indefinitely until a given condition (checked
	 * with loop) occurs
	 *
	 * @throws InterruptedException if this thread is interrupted while
	 *                              waiting (op mode stopped).
	 */
	protected void waitUntil(BooleanSupplier condition)
			throws InterruptedException {
		RobotLog.d("WaitUntil started: " + Reflections.betterName(condition));
		this.condition.waitFor(condition);
		RobotLog.d("WaitUntil ended " + Reflections.betterName(condition));
	}
	
	/**
	 * Pauses the current thread until a given condition occurs, or after
	 * timeout.
	 *
	 * @throws InterruptedException if this thread is interrupted while
	 *                              waiting (op mode stopped).
	 */
	protected void waitUntil(
			final BooleanSupplier waitCondition, long timeout, TimeUnit unit)
			throws InterruptedException {
		long nanos = unit.toNanos(timeout);
		final long stopTime = System.nanoTime() + nanos;
		waitUntil(new BooleanSupplier() {
			@Override
			public boolean getAsBoolean() {
				return waitCondition.getAsBoolean() ||
				       System.nanoTime() >= stopTime;
			}
			
			@NotNull
			@Override
			public String toString() {
				return waitCondition.toString();
			}
		});
	}
	
	protected static boolean sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			return true;
		}
		return false;
	}
	
	private static class SingleCondition {
		private final AtomicInteger   waiters = new AtomicInteger();
		private       BooleanSupplier condition;
		
		protected SingleCondition() {
		}
		
		synchronized void waitFor(BooleanSupplier condition)
				throws InterruptedException {
			this.notifyAll(); //screw all other waiters, if any
			this.condition = condition;
			if (condition.getAsBoolean()) return;
			waiters.incrementAndGet();
			this.wait();
			waiters.decrementAndGet();
		}
		
		void update() {
			if (waiters.get() == 0) return;
			synchronized (this) {
				if (condition.getAsBoolean()) {
					this.notifyAll();
				}
			}
		}
	}
}
