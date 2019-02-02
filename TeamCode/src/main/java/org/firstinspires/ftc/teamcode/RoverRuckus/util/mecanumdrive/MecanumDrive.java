package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

import java.util.function.DoubleSupplier;

import static org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MotorSetPower.calcPower;

/**
 * The publicly available class to interact with {@link MoveTaskExecutor}
 * <p>
 * Orientation: <br>
 * <pre>
 *       -deg<-(0)->+deg
 *             +Y
 *              |
 * -90deg  -X --O-- +X  +90deg
 *              |
 *             -Y
 *          +/-180deg
 * </pre>
 * Factory, Wrapper, and Decorator (too much?)
 */
public class MecanumDrive {
	private final MoveTaskExecutor taskExecutor;
	private final DoubleSupplier direction;
	
	/**
	 * Construct, given a set of motors to run on.
	 *
	 * @param motors
	 */
	public MecanumDrive(MotorSet motors) {
		taskExecutor = new MoveTaskExecutor(motors);
		this.direction = null;
	}
	
	/**
	 * Construct, given a set of motors to run on.
	 */
	public MecanumDrive(MotorSet motors, DoubleSupplier direction) {
		taskExecutor = new MoveTaskExecutor(motors);
		this.direction = new CumulativeDirection(direction);
	}
	
	
	/**
	 * Cancels all enqueued MoveTasks and stops
	 * any moveTask currently running
	 */
	public void cancel() {
		taskExecutor.cancel();
	}
	
	/**
	 * @return true if no tasks are running, false if running
	 */
	public boolean isDone() {
		return taskExecutor.isDone();
	}
	
	/**
	 * Adds a MoveTask that represents moving the robot in a straight line the specified direction and distance
	 */
	public void move(double direction, double distance, double speed) {
		direction = Math.toRadians(direction);
		taskExecutor.add(new StraightMoveTask(calcPower(direction, 1, 0), distance * MotorSetPosition.MOVE_MULT,
				speed));
	}
	
	/**
	 * Adds a MoveTask that moving the robot to a specified relative location on the coordinate plane
	 */
	public void moveXY(double x, double y, double speed) {
		double direction = Math.toDegrees(Math.atan2(x, y));
		double distance = Math.hypot(x, y);
		move(direction, distance, speed);
	}
	
	/**
	 * Adds a task the represents turning the robot a specific number of degrees.
	 */
	public void turn(double degreesToTurn, double speed) {
		if (direction == null) {
			degreesToTurn = Math.toRadians(degreesToTurn);
			taskExecutor.add(new StraightMoveTask(calcPower(0, 0, Math.signum(degreesToTurn)),
					Math.abs(degreesToTurn) * MotorSetPosition.TURN_MULT, speed));
		} else {
			taskExecutor.add(new TurnMoveTask(degreesToTurn, speed, direction));
		}
	}
	
	/**
	 * waits (blocks the current thread) until all tasks are finished.
	 *
	 * @throws InterruptedException if the thread is interrupted while waiting
	 */
	public void waitUntilDone() throws InterruptedException {
		taskExecutor.waitUntilDone();
	}
	
	/**
	 * Waits until all tasks are finished, then sleeps a specified number of milliseconds
	 */
	public void sleep(int millis) throws InterruptedException {
		waitUntilDone();
		Thread.sleep(millis);
	}
	
	/**
	 * Stops the internal thread and any tasks.
	 */
	public void stop() {
		taskExecutor.stop();
	}
}
