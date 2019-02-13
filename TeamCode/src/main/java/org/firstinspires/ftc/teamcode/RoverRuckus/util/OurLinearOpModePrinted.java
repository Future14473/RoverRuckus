package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import android.support.annotation.CallSuper;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.PrintedRobot;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;

/**
 * A class with a bunch of helper functions for our OpModes.
 */
public abstract class OurLinearOpModePrinted extends LinearOpMode {
	private final SingleCondition condition = new SingleCondition();
	protected     PrintedRobot    robot;
	
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
			robot = new PrintedRobot(hardwareMap);
			initialize();
			if (Thread.interrupted()) throw new InterruptedException();
			telemetry.addLine("Initialization done");
			telemetry.addLine("You may now press start");
			telemetry.update();
			waitForStart();
			if (Thread.interrupted()) throw new InterruptedException();
			run();
		} finally {
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
	 * always loop after the OpMode stops: through normal exit, exception
	 * thrown, or OpMode stopped early. If a RuntimeException is thrown during
	 * cleanup, it is ignored. Inside a finally block.
	 */
	protected void cleanup() {}
	
	/**
	 * Pauses the current thread indefinitely until a given condition (checked
	 * with loop) occurs
	 *
	 * @throws InterruptedException if this thread is interrupted while
	 *                              waiting (op mode stopped).
	 */
	protected void waitUntil(BooleanSupplier condition)
			throws InterruptedException {
		RobotLog.d("WaitUntil started: " + condition.toString());
		this.condition.waitFor(condition);
		RobotLog.d("WaitUntil ended " + condition.toString());
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
			
			@Override
			public String toString() {
				return waitCondition.toString();
			}
		});
	}
	
	private static class SingleCondition {
		private final AtomicInteger   waiters = new AtomicInteger();
		private       BooleanSupplier condition;
		
		protected SingleCondition() {}
		
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
