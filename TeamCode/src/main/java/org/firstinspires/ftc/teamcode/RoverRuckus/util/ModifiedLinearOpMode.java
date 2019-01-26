package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * Base class for our custom modified operation modes (similar to Linear op mode).
 * Redundant code and weird things removed.
 */
public abstract class ModifiedLinearOpMode extends OpMode {
	private DrivingOpModeRunner runner = null;
	private ExecutorService executorService = null;
	private volatile boolean isStarted = false;
	private volatile boolean stopRequested = false;
	private BooleanSupplier waitCondition = null;
	private boolean theCondition = false;
	
	/**
	 * Override this method and place your code here.
	 *
	 * @throws InterruptedException to stop OpMode early.
	 */
	protected abstract void runOpMode() throws InterruptedException;
	
	/**
	 * Override this method and place your cleanup code here.
	 * This method will always run after the OpMode stops: through
	 * normal exit, exception thrown, or OpMode stopped early.
	 * If a RuntimeException is thrown, it is ignored.
	 */
	protected abstract void cleanup();
	
	/**
	 * From the normal OpMode; do not override
	 */
	@Override
	final public void init() {
		this.executorService = ThreadPool.newSingleThreadExecutor("ModifiedLinearOpMode");
		this.runner = new DrivingOpModeRunner();
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
			
			// interrupt the Driving opMode and shutdown it's service thread
			// interrupted status should also be a indicator of needing to stop.
			this.executorService.shutdownNow();
			/* Wait, forever, for the OpMode to stop. If this takes too long, then
			  {@link OpModeManagerImpl#callActiveOpModeStop()} will catch that and take action */
			try {
				ThreadPool.awaitTermination(this.executorService, 100, TimeUnit.DAYS, "User Driving op mode");
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	/**
	 * Does nothing -- not updating telemetry automatically.
	 * we already handle telemetry updates by ourselves -- sometimes we wish to "accumulate" data before we
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
		// if there is a runtime exception in user code; throw it so the normal error
		// reporting process can handle it
		if (this.runner.hasRuntimeException()) {
			throw this.runner.getRuntimeException();
		}
		// check for condition.
		if (waitCondition != null) {
			synchronized (this) {
				theCondition = waitCondition.getAsBoolean();
				if (theCondition) {
					waitCondition = null;
					this.notifyAll();
				}
			}
		}
		//no notifying necessary -- no one is waiting (no waitForHardwareCycle)
	}
	
	/**
	 * Answer as to whether this opMode is active and the robot should continue onwards. If the
	 * opMode is not active, the OpMode should terminate at its earliest convenience.
	 *
	 * @return whether the OpMode is currently active. If this returns false, you should
	 * break out of the loop in your {@link #runOpMode()} method and return to its caller.
	 * @apiNote No longer calls {@code idle()};
	 * @see #runOpMode()
	 * @see #isStarted()
	 */
	protected final boolean opModeIsActive() {
		return this.isStarted && !isStopRequested();
	}
	
	/**
	 * Checks if the opMode is active. If the OpMode is not active and should not
	 * be running, this will terminate the OpMode throwing an {@link InterruptedException}.
	 * Otherwise returns true.
	 */
	protected final boolean checkOpModeIsActive() throws InterruptedException {
		if (!opModeIsActive()) throw new InterruptedException();
		return true;
	}
	
	/**
	 * Has the the stopping of the opMode been requested?
	 *
	 * @return whether stopping opMode has been requested or not
	 * @see #opModeIsActive()
	 * @see #isStarted()
	 */
	public final boolean isStopRequested() {
		return this.stopRequested || Thread.currentThread().isInterrupted();
	}
	
	/**
	 * Pauses the current thread until a given condition (checked with loop) occurs.
	 *
	 * @throws InterruptedException if this thread is interrupted while sleeping (op mode stopped).
	 */
	protected void waitUntil(BooleanSupplier condition) throws InterruptedException {
		synchronized (this) {
			this.theCondition = false;
			this.waitCondition = condition;
			while (!theCondition) {
				this.wait();
			}
		}
	}
	
	/**
	 * Sleeps for the given amount of milliseconds, or until the thread is interrupted. This is
	 * simple shorthand for the operating-system-provided {@link Thread#sleep(long) sleep()} method.
	 *
	 * @param milliseconds amount of time to sleep, in milliseconds
	 * @throws InterruptedException if this thread is interrupted while sleeping (op mode stopped).
	 * @see Thread#sleep(long)
	 */
	public final void sleep(long milliseconds) throws InterruptedException {
		Thread.sleep(milliseconds);
	}
	
	/**
	 * Pauses the Driving Op Mode until start.
	 *
	 * @throws InterruptedException if this thread is interrupted while waiting (op mode stopped).
	 */
	public synchronized void waitForStart() throws InterruptedException {
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
	public final boolean isStarted() {
		return this.isStarted || Thread.currentThread().isInterrupted();
	}
	
	private class DrivingOpModeRunner implements Runnable {
		RuntimeException exception = null;
		
		boolean isShutdown = false;
		
		DrivingOpModeRunner() {
		}
		
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
					RobotLog.d("ModifiedLinearOpMode received an InterruptedException; shutting down this Driving op " + "mode");
				} catch (CancellationException ie) {
					RobotLog.d("ModifiedLinearOpMode received a CancellationException; shutting down this Driving op " + "mode");
				} catch (RuntimeException e) {
					this.exception = e;
				} finally {
					// since telemetry statements will very soon be replaced with the default op mode, it is not
					// worth it to try and update the telemetry.
					try {
						ModifiedLinearOpMode.this.cleanup();
					} catch (RuntimeException e) {
						RobotLog.d("ModifiedLinearOpMode received a RuntimeException during cleanup; ignoring.");
					} finally {
						this.isShutdown = true;
					}
				}
			});
		}
	}
}
