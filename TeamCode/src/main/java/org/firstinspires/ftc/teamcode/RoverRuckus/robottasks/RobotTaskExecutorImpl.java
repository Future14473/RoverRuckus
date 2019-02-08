package org.firstinspires.ftc.teamcode.RoverRuckus.robottasks;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * Actually handles the running of {@link RobotTaskAdapter}s, given a
 * {@link IRobot} to loop them on.<br>
 * Tasks are put into a queue and loop one at a time. This class
 * contains several other methods to interact with the
 * queue and the running of the MoveTasks. Running of {@link RobotTaskAdapter}s
 * are done in a separate thread. <br>
 * Although Object#finalize() is overriden as a backup and the thread
 * will stop itself after 2 minutes of inactivity: <br>
 * IT IS THE RESPONSIBILITY OF THE CLIENT TO CALL {@link #stop()} ON
 * THIS METHOD TO END THE INTERNAL THREAD GRACEFULLY. ELSE IT MAY NEVER
 * STOP WHEN ITS SUPPOSED TO.
 * TODO: use hardwareMap and OpModeManagerListener to close when needed.
 *
 * @see RobotTaskAdapter
 */
public class RobotTaskExecutorImpl implements RobotTaskExecutor {
	private final BlockingQueue<RobotTask> queue = new LinkedBlockingQueue<>();
	private final IRobot iRobot;
	//executors are too complicated for us to need em. Simple is faster and simpler.
	private final Thread theThread;
	private final AtomicBoolean isRunning = new AtomicBoolean(true);
	private final AtomicBoolean paused = new AtomicBoolean(false);
	private final Object doneLock = new Object();
	private boolean done = true;
	
	/**
	 * Construct via IRobot
	 *
	 * @param iRobot the IRobot
	 */
	public RobotTaskExecutorImpl(IRobot iRobot) {
		this.iRobot = iRobot;
		theThread = new Thread(this::run);
		theThread.setName("Move RobotTask Executor");
		theThread.setDaemon(true);
		theThread.start();
	}
	
	@Override
	public void add(RobotTask robotTask) {
		queue.add(robotTask);
	}
	
	@Override
	public void cancel() {
		queue.clear();
		theThread.interrupt();
	}
	
	@Override
	public boolean isDone() {
		synchronized (doneLock) {
			return done;
		}
	}
	
	@Override
	public void waitUntilDone() throws InterruptedException {
		synchronized (doneLock) {
			while (!done) doneLock.wait();
		}
	}
	
	@Override
	public void start() {
		theThread.start();
	}
	
	@Override
	public void pause() {
		paused.set(true);
	}
	
	@Override
	public void resume() {
		paused.set(false);
		synchronized (paused) {
			paused.notifyAll();
		}
	}
	
	@Override
	public boolean isPaused() {
		return paused.get();
	}
	
	@Override
	public void stop() {
		isRunning.set(false);
		theThread.interrupt();
		synchronized (doneLock) {
			done = true;
			doneLock.notifyAll();
		}
	}
	
	@Override
	protected void finalize() {
		if (isRunning.get()) stop();
	}
	
	private void run() {
		while (isRunning.get()) try {
			RobotTask curRobotTask;
			synchronized (doneLock) { //we can't say done while polling queue.
				if (queue.isEmpty()) { //if we're done, set and notify
					done = true;
					doneLock.notifyAll();
				}
				curRobotTask = queue.poll(2, MINUTES);
				if (curRobotTask == null) {
					stop();
					break;
				}
				done = false;
			}
			waitIfPaused();
			if (!Thread.interrupted()) {
				curRobotTask.start(iRobot);
				while (!(Thread.interrupted() || curRobotTask.loop())) {
					waitIfPaused();
				}
			}
		} catch (InterruptedException ignored) {}
		synchronized (doneLock) {
			done = true;
			doneLock.notifyAll();
		}
	}
	
	private void waitIfPaused() throws InterruptedException {
		if (paused.get()) {
			synchronized (paused) {
				while (paused.get()) {
					paused.wait();
				}
			}
		}
	}
}
	

