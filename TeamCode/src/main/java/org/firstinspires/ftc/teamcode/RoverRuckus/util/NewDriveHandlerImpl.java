package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.concurrent.*;

/**
 * Separate utility class to handle autonomous omnidirectional motion on mecanum wheels.
 */
/*
	The new implementation of autonomous motion tasks utilizes a (wrapped_ singleThreadPoolExecutor, and the moveTasks
	are now Runnable's.
	Also some math tweaks.
*/
public class NewDriveHandlerImpl implements DriveHandlerIntf {
	
	private final InteractableSequentialExecutor executor = new InteractableSequentialExecutor();
	private final DcMotor[] motors;
	private LinearOpMode mode;
	private boolean running = true;
	
	NewDriveHandlerImpl(DcMotor leftFront, DcMotor rightFront, DcMotor backLeft, DcMotor backRight) {
		motors = new DcMotor[]{leftFront, rightFront, backLeft, backRight};
		for (int i = 0; i < 4; i++) {
			motors[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
		}
	}
	
	/**
	 * To track if OpMode is running.
	 */
	@Override
	public void addLinearOpMode(LinearOpMode mode) {
		this.mode = mode;
	}
	
	@Override
	public void cancelTasks() {
		executor.cancelTasks();
	}
	
	@Override
	public boolean isDone() {
		return executor.isDone();
	}
	
	/**
	 * adds a MoveTask to move in a straight line a specified direction and distance.
	 */
	@Override
	public void move(double direction, double distance, double speed) throws InterruptedException {
		direction = Math.toRadians(direction);
		addTask(new UniformMove(MotorPowerSet.calcPowerSet(direction, 0), distance * MOVE_MULT, speed));
	}
	
	/**
	 * Tells the robot to move in a specified direction and turnRate.
	 * May scale motor power levels so the values is less than 1.
	 */
	@Override
	public void moveAt(double direction, double turnRate, double speed) {
		setPower(MotorPowerSet.calcPowerSet(direction, turnRate, speed));
	}
	
	/**
	 * adds a MoveTasks that moves the robot in a straight line to a displacement specified by x and y.
	 */
	@Override
	public void moveXY(double x, double y, double speed) throws InterruptedException {
		double direction = Math.toDegrees(Math.atan2(x, y));
		double distance = Math.hypot(x, y);
		move(direction, distance, speed);
	}
	
	@Override
	public void stop() {
		running = false;
		executor.stop();
	}
	
	/**
	 * ads a move task to rotate in place a specified number of degrees, positive or negative.
	 */
	@Override
	public void turn(double degrees, double speed) throws InterruptedException {
		degrees = Math.toRadians(degrees);
		addTask(new UniformMove(MotorPowerSet.calcPowerSet(0, Math.signum(degrees)), Math.abs(degrees) * TURN_MULT,
				speed));
	}
	
	@Override
	public void waitForDone() throws InterruptedException {
		executor.waitForDone();
	}
	
	private void addTask(MoveTask task) throws InterruptedException {
		if (!okToRun()) throw new InterruptedException();
		executor.addTask(task);
	}
	
	private boolean okToRun() {
		return mode == null ? running : mode.opModeIsActive();
	}
	
	/**
	 * set motors to given powerSet.
	 */
	private void setPower(MotorPowerSet p) {
		MotorPowerSet s = p.scaled();
		for (int i = 0; i < 4; i++) { motors[i].setPower(s.power[i]); }
	}
	
	void setModeEncoder() {
		for (int i = 0; i < 4; i++) {
			motors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
	}
	
	private void stopRobot() {
		setPower(MotorPowerSet.ZERO);
	}
	
	private class InteractableSequentialExecutor {
		private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
		private Future<?> curFuture;
		//The following eventually gets garbage collected if nothing is running.
		// When tasks are being run.
		// "okToRun" every cycle, since we have no easy way of signaling stop directly.
		private final ExecutorService executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>()) {
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				tryPopQueue(); //add next task if exists.
			}
		};
		
		public synchronized void addTask(Runnable runnable) {
			queue.add(runnable);
			tryPopQueue();
		}
		
		private synchronized void tryPopQueue() {
			if (!okToRun()) {
				stop();
				return;
			}
			if (curFuture != null && !curFuture.isDone() && !curFuture.isCancelled()) return; //if running
			if (queue.isEmpty()) { //if no more to submit
				curFuture = null; //we are done, also notify if anyone waiting
				this.notifyAll();
				return;
			}
			curFuture = executor.submit(queue.element()); //add next task
		}
		
		public synchronized void stop() {
			executor.shutdownNow();
			curFuture = null;
			this.notifyAll();
		}
		
		public synchronized void cancelTasks() {
			queue.clear();
			if (curFuture != null) curFuture.cancel(true);
		}
		
		public boolean isDone() {
			return curFuture == null;
		}
		
		public synchronized void waitForDone() throws InterruptedException {
			while (curFuture != null) {
				this.wait(); //wait until curFuture == null; our done indicator
			}
		}
	}
	
	public abstract class MoveTask implements Runnable {
		
		@Override
		public final void run() {
			try {
				start();
				boolean o;
				do o = process(); while (!o && !Thread.interrupted() && okToRun());
			} finally { //paranoia
				stopRobot();
			}
		}
		
		protected abstract void start();
		
		protected abstract boolean process();
	}
	
	private class UniformMove extends MoveTask {
		private MotorPowerSet targetPower, actualPower;
		private double multiplier;
		
		private double speed;
		
		private double[] progress = new double[4];
		
		UniformMove(MotorPowerSet targetPower, double multiplier, double speed) {
			this.targetPower = targetPower;
			this.multiplier = multiplier;
			this.speed = speed;
		}
		
		@Override
		protected void start() {
			for (int i = 0; i < 4; i++) {
				motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				motors[i].setMode(DcMotor.RunMode.RUN_TO_POSITION);
				motors[i].setTargetPosition((int) (multiplier * targetPower.power[i]));
			}
			actualPower = new MotorPowerSet();
		}
		
		//read motor positions and adjust them as necessary if they go off track.
		//supposed to make all the motors turn in unison.
		@Override
		protected boolean process() {
			double avgProgress = 0;
			int maxOff = 0;
			for (int i = 0; i < 4; i++) {
				progress[i] = (double) motors[i].getCurrentPosition() / motors[i].getTargetPosition();
				maxOff = Math.max(maxOff, Math.abs(motors[i].getCurrentPosition() - motors[i].getTargetPosition()));
				if (Double.isNaN(progress[i])) progress[i] = 1;
				avgProgress += progress[i];
			}
			avgProgress /= 4;
			//adjust power as necessary..
			for (int i = 0; i < 4; i++) {
				actualPower.power[i] = targetPower.power[i] * (1 - 3 * (progress[i] - avgProgress)) * speed;
			}
			setPower(actualPower);
			return maxOff < 100;
		}
		
	}
	
	//TODO
	private class TurnAndStraight extends MoveTask {
		private final double theta, m, phi, s;
		
		private TurnAndStraight(double direction, double distance, double degreesTurn, double speed) {
			phi = direction;
			theta = degreesTurn;
			m = distance;
			s = speed;
		}
		
		@Override
		protected void start() {
		
		}
		
		@Override
		protected boolean process() {
			return false;
		}
	}
	
	/**
	 * a set of power levels for all 4 motors
	 * just a container around double[4]
	 */
	private static class MotorPowerSet {
		private static final MotorPowerSet ZERO = new MotorPowerSet(0, 0, 0, 0);
		double[] power;
		
		public MotorPowerSet(double leftFront, double rightFront, double backLeft, double backRight) {
			this.power = new double[]{leftFront, rightFront, backLeft, backRight};
		}
		
		public MotorPowerSet() {
			this.power = new double[4];
		}
		
		public MotorPowerSet scaled() {
			MotorPowerSet set = new MotorPowerSet();
			double scalar = 0.95;
			for (int i = 0; i < 4; i++) {
				scalar = Math.max(scalar, Math.abs(power[i]));
			}
			scalar = 0.95 / scalar;
			for (int i = 0; i < 4; i++) {
				set.power[i] = power[i] * scalar;
			}
			return set;
		}
		
		/**
		 * Generates a MotorPowerSet that corresponds to moving the robot in the specified direction, turnRate, and
		 * speed.
		 */
		public static MotorPowerSet calcPowerSet(double direction, double turnRate) {
			return calcPowerSet(direction, turnRate, 1);
		}
		
		/**
		 * Generates a MotorPowerSet that corresponds to moving the robot in the specified direction, turnRate, and
		 * speed.
		 */
		public static MotorPowerSet calcPowerSet(double direction, double turnRate, double speed) {
			double robotAngle = direction + Math.PI / 4;
			double v1 = speed * Math.sin(robotAngle) + turnRate;
			double v2 = speed * Math.cos(robotAngle) - turnRate;
			double v3 = speed * Math.cos(robotAngle) + turnRate;
			double v4 = speed * Math.sin(robotAngle) - turnRate;
			return new MotorPowerSet(v1, v2, v3, v4);
		}
	}
}