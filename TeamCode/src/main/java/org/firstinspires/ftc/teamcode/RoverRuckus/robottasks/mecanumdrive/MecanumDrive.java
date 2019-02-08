package org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.mecanumdrive;

import org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.AbstractRobotTaskExecutor;
import org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.IRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.RobotTaskExecutorImpl;
import org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.SleepTask;

import static org.firstinspires.ftc.teamcode.RoverRuckus.robottasks.MotorSetPower.calcPower;

/**
 * The publicly available class to interact with {@link RobotTaskExecutorImpl}
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
public class MecanumDrive extends AbstractRobotTaskExecutor {
	private final Parameters parameters;
	
	public MecanumDrive(IRobot robot, Parameters parameters) {
		super(robot);
		this.parameters = parameters;
	}
	
	@Override
	public void start() {
		super.start();
		robot.getWheelMotors().setTargetPositionTolerance(parameters.wheelTolerance);
	}
	
	/**
	 * Adds a RobotTask that represents moving the robot in a straight line the specified direction and distance
	 */
	public void move(double direction, double distance, double speed) {
		direction = Math.toRadians(direction);
		robotTaskExecutor.add(new StraightMoveRobotTask(calcPower(direction, 1, 0),
				distance * UniformMoveRobotTask.MOVE_MULT, speed));
	}
	
	/**
	 * Adds a RobotTask that moving the robot to a specified relative location on the coordinate plane
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
		if (!parameters.useGyro) {
			degreesToTurn = Math.toRadians(degreesToTurn);
			robotTaskExecutor.add(new StraightMoveRobotTask(calcPower(0, 0, Math.signum(degreesToTurn)),
					Math.abs(degreesToTurn) * UniformMoveRobotTask.TURN_MULT, speed));
		} else {
			robotTaskExecutor.add(new TurnMoveRobotTask(degreesToTurn, speed));
		}
	}
	
	/**
	 * Waits until all tasks are finished, then sleeps a specified number of milliseconds
	 */
	public void sleep(int millis) {
		add(new SleepTask(millis));
	}
	
	/**
	 * Stops the internal thread and any tasks.
	 */
	@Override
	public void stop() {
		robotTaskExecutor.stop();
	}
	
	public static class Parameters {
		protected boolean useGyro = false;
		protected int wheelTolerance = 25;
	}
}
