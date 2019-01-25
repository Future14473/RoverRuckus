package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

import static org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MotorPowerSet.*;

/**
 * The publicly available class to interact with {@link MoveTaskExecutor}
 * (One day we might add a interface abstraction)
 */
public class MecanumDrive {
	private final MoveTaskExecutor taskExecutor;
	private final MotorSet motors;
	
	/**
	 * Construct, given a set of motors to run on.
	 */
	public MecanumDrive(MotorSet motors) {
		taskExecutor = new MoveTaskExecutor(motors);
		this.motors = motors;
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
	 * Utility: set the motors right now to move in the specified direction, turnRate, and speed.
	 */
	public void moveAt(double direction, double turnRate, double speed) {
		motors.setPowerTo(calcPowerSet(direction, turnRate, speed));
	}
	
	/**
	 * Adds a MoveTask that represents moving the robot in a straight line the specified direction and distance
	 */
	public void move(double direction, double distance, double speed) {
		direction = Math.toRadians(direction);
		taskExecutor.add(new StraightMoveTask(calcPowerSet(direction, 0, 1), distance * MOVE_MULT, speed));
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
	public void turn(double angle, double speed) {
		angle = Math.toRadians(angle);
		taskExecutor.add(new StraightMoveTask(calcPowerSet(0, Math.signum(angle), 0), Math.abs(angle) * TURN_MULT,
				speed));
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
	 * Stops the internal thread and any tasks.
	 */
	public void stop() {
		taskExecutor.stop();
	}
}
