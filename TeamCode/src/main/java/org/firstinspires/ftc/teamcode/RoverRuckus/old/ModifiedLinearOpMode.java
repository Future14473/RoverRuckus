package org.firstinspires.ftc.teamcode.RoverRuckus.old;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryInternal;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;

/**
 * Base class for our custom modified operation modes (similar to Linear op
 * mode).
 * Redundant code and weird things removed.
 */
public abstract class ModifiedLinearOpMode extends OpMode {
	private final SingleWaiter waiter = new SingleWaiter();
	private OpModeRunner runner = null;
	private ExecutorService executorService = null;
	private volatile boolean isStarted = false;
	private volatile boolean stopRequested = false;
	
	/**
	 * Override this method and place your code here.
	 *
	 * @throws InterruptedException to close OpMode early.
	 */
	protected abstract void runOpMode() throws InterruptedException;
	
	/**
	 * Override this method and place cleanup code here.
	 * This method will always loop after the OpMode stops: through
	 * normal exit, exception thrown, or OpMode stopped early.
	 * If a RuntimeException is thrown during cleanup, it is ignored.
	 */
	protected abstract void cleanup();
	
	/**
	 * From the normal OpMode; do not override
	 */
	@Override
	final public void init() {
		this.executorService = ThreadPool.newSingleThreadExecutor(
				"ModifiedLinearOpMode");
		this.runner = new OpModeRunner();
		this.executorService.execute(this.runner);
	}
	
	/**
	 * From the normal OpMode; do not override
	 */
	@Override
	final public void init_loop() {
		handleLoop();
	}
	
	/**
	 * From the normal OpMode; do not override
	 */
	@Override
	final synchronized public void start() {
		this.isStarted = true;
		//notify waitForStart();
		this.notifyAll();
	}
	
	/**
	 * From the normal OpMode; do not override
	 */
	@Override
	final public void loop() {
		handleLoop();
	}
	
	/**
	 * From the normal OpMode; do not override
	 */
	@Override
	final public void stop() {
		
		// make isStopRequested() return true and opModeIsActive() return false
		this.stopRequested = true;
		
		if (this.executorService != null) {
			
			// interrupt the Linear opMode and shutdown it's service thread
			// interrupted status should also be a indicator of needing to
			// close.
			this.executorService.shutdownNow();
			/* Wait, forever, for the OpMode to close. If this takes too long,
			 then
			  {@link OpModeManagerImpl#callActiveOpModeStop()} will catch that
			   and take action */
			try {
				ThreadPool.awaitTermination(this.executorService, 100,
						TimeUnit.DAYS, "User Linear op mode");
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	/**
	 * Does nothing -- not updating telemetry automatically.
	 * we already handle telemetry updates by ourselves -- sometimes we wish
	 * to "accumulate" data before we
	 * update, so we don't want automatic updates.
	 */
	@Override
	public void internalPostInitLoop() {}
	
	/**
	 * @see #internalPostInitLoop()
	 */
	@Override
	public void internalPostLoop() {}
	
	private void handleLoop() {
		// if there is a runtime exception in user code; throw it so the
		// normal error
		// reporting process can handle it
		if (runner.hasRuntimeException()) {
			throw runner.getRuntimeException();
		}
		
		
		//telemetry
		if (telemetry instanceof TelemetryInternal) {
			((TelemetryInternal) telemetry).tryUpdateIfDirty();
		}
		//slight efficiency
		Thread.yield();
		doInLoop();
	}
	
	/**
	 * Answer as to whether this opMode is active and the robot should
	 * continue onwards. If the
	 * opMode is not active, the OpMode should terminate at its earliest
	 * convenience.
	 *
	 * @return whether the OpMode is currently active. If this returns false,
	 * you should
	 * break out of the loop in your {@link #runOpMode()} method and return to
	 * its caller.
	 * @apiNote No longer calls {@code idle()};
	 * @see #runOpMode()
	 * @see #isStarted()
	 */
	protected final boolean opModeIsActive() {
		return this.isStarted && !isStopRequested();
	}
	
	/**
	 * Has the the stopping of the opMode been requested?
	 *
	 * @return whether stopping opMode has been requested or not
	 * @see #opModeIsActive()
	 * @see #isStarted()
	 */
	private boolean isStopRequested() {
		return this.stopRequested || Thread.currentThread().isInterrupted();
	}
	
	/**
	 * Pauses the current thread indefinitely until a given condition (checked
	 * with loop) occurs
	 *
	 * @throws InterruptedException if this thread is interrupted while
	 * waiting (op mode stopped).
	 */
	protected void waitUntil(BooleanSupplier condition) throws InterruptedException {
		RobotLog.d("WaitUntil started: " + condition.toString());
		this.waiter.waitFor(condition);
		RobotLog.d("WaitUntil ended " + condition.toString());
	}
	
	/**
	 * Pauses the current thread until a given condition occurs, or after
	 * timeout.
	 *
	 * @throws InterruptedException if this thread is interrupted while
	 * waiting (op mode stopped).
	 */
	protected void waitUntil(BooleanSupplier waitCondition, long timeout,
	                         TimeUnit unit) throws InterruptedException {
		long nanos = unit.toNanos(timeout);
		final long stopTime = System.nanoTime() + nanos;
		waitUntil(() -> waitCondition.getAsBoolean() || System.nanoTime() >= stopTime);
	}
	
	/**
	 * Sleeps for the given amount of milliseconds, or until the thread is
	 * interrupted. This is
	 * simple shorthand for the operating-system-provided
	 * {@link Thread#sleep(long) sleep()} method.
	 *
	 * @param milliseconds amount of time to sleep, in milliseconds
	 * @throws InterruptedException if this thread is interrupted while
	 * sleeping (op mode stopped).
	 * @see Thread#sleep(long)
	 */
	protected final void sleep(long milliseconds) throws InterruptedException {
		Thread.sleep(milliseconds);
	}
	
	/**
	 * Pauses the Linear Op Mode until start.
	 *
	 * @throws InterruptedException if this thread is interrupted while
	 * waiting (op mode stopped).
	 */
	protected synchronized void waitForStart() throws InterruptedException {
		while (!isStarted()) {
			this.wait();
		}
	}
	
	/**
	 * Has the opMode been started?
	 *
	 * @return whether this opMode has been started or not
	 * @see #opModeIsActive()
	 */
	private boolean isStarted() {
		return this.isStarted || Thread.currentThread().isInterrupted();
	}
	
	/**
	 * This will be run once during handleLoop().
	 */
	private void doInLoop() {
		//signal any waiters
		waiter.update();
	}
	
	private class OpModeRunner implements Runnable {
		private RuntimeException exception = null;
		
		private boolean isShutdown = false;
		
		RuntimeException getRuntimeException() {
			return this.exception;
		}
		
		boolean hasRuntimeException() {
			return this.exception != null;
		}
		
		public boolean isShutdown() {
			return this.isShutdown;
		}
		
		@Override
		public void run() {
			ThreadPool.logThreadLifeCycle("ModifiedLinearOpMode main", () -> {
				
				try {
					ModifiedLinearOpMode.this.runOpMode();
					requestOpModeStop();
				} catch (InterruptedException ie) {
					RobotLog.d("ModifiedLinearOpMode received an " +
							"InterruptedException; shutting down this Linear " + "op " + "mode");
				} catch (CancellationException ie) {
					RobotLog.d("ModifiedLinearOpMode received a " +
							"CancellationException; shutting down this Linear "
							+ "op " + "mode");
				} catch (RuntimeException e) {
					this.exception = e;
				} finally {
					// since telemetry statements will very soon be replaced
					// with the default op mode, we do not
					// need to try and
					// worth it to try and update the telemetry.
					try {
						ModifiedLinearOpMode.this.cleanup();
					} catch (RuntimeException e) {
						RobotLog.d("ModifiedLinearOpMode received a " +
								"RuntimeException during cleanup; ignoring.");
					} finally {
						this.isShutdown = true;
					}
				}
			});
		}
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
