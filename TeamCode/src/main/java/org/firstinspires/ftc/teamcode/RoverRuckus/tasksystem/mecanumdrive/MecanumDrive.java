package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.mecanumdrive;

import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.AbstractTaskProgram;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.TaskExecutorImpl;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.IRobot;

import static org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.MotorSetPower.calcPower;

/**
 * The publicly available class to interact with {@link TaskExecutorImpl}
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
public class MecanumDrive extends AbstractTaskProgram {
	private final Parameters parameters;
	private final IRobot robot;
	
	public MecanumDrive(IRobot robot, Parameters parameters) {
		super();
		this.robot = robot;
		this.parameters = parameters;
	}
	
	@Override
	public void start() {
		super.start();
		robot.getWheelMotors().setTargetPositionTolerance(parameters.wheelTolerance);
	}
	
	/**
	 * Adds a Task that represents moving the robot in a straight line the specified direction and distance
	 */
	public void move(double direction, double distance, double speed) {
		direction = Math.toRadians(direction);
		add(new StraightMoveTask(robot, calcPower(direction, 1, 0), distance * UniformMoveTask.MOVE_MULT, speed));
	}
	
	/**
	 * Adds a Task that moving the robot to a specified relative location on the coordinate plane
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
			add(new StraightMoveTask(robot, calcPower(0, 0, Math.signum(degreesToTurn)),
					Math.abs(degreesToTurn) * UniformMoveTask.TURN_MULT, speed));
		} else {
			add(new TurnMoveTask(robot, degreesToTurn, speed));
		}
	}
	
	public static class Parameters {
		private static final Parameters defaultParams = new Parameters();
		public boolean useGyro = false;
		public int wheelTolerance = 25;
		
	}
}
