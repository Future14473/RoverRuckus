package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RoverRuckus.externalLib.MiniPID;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.TaskAdapter;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.TaskProgram;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GlobalVars;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;

import java.util.Objects;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower.TURN;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower.calcPower;

/**
 * A task program that controls the autonomous motion of wheels.
 * Uses motor encoders to dynamically correct motion,
 * Optionally uses gyroscope (YOU SHOULD USE THIS) to increase
 * accuracy in turning and direction.
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
 */
public class MecanumDrive extends TaskProgram {
	private final Parameters parameters;
	private final IRobot robot;
	private final MiniPID turnPID = new MiniPID(0.035, 0, 0.13);
	private double targetAngle = 0;
	
	public MecanumDrive(IRobot robot, Parameters parameters) {
		super(true);
		this.robot = Objects.requireNonNull(robot);
		this.parameters = Objects.requireNonNull(parameters);
	}
	
	@Override
	public void start() {
		targetAngle = robot.getAngle();
		robot.getWheels().setTargetPositionTolerance(parameters.wheelTolerance);
		super.start();
	}
	
	/**
	 * Adds a Task that represents moving the robot in a straight line the
	 * specified direction and distance
	 */
	public void move(double direction, double distance, double speed) {
		direction = Math.toRadians(direction);
		add(new OldStraightMoveTask(robot, calcPower(direction, 1, 0),
				distance * OldStraightMoveTask.MOVE_MULT, speed));
	}
	
	/**
	 * Moves the robot to the specified relative location on the coordinate
	 * plane
	 */
	public void moveXY(double x, double y, double speed) {
		double direction = Math.toDegrees(Math.atan2(x, y));
		double distance = Math.hypot(x, y);
		move(direction, distance, speed);
	}
	
	public void moveXY(double x, double y, DistanceUnit unit, double speed) {
		//TODO!!!
		//NOTE: DETERMINE UNITS
	}
	
	/**
	 * Rotates the robot a specific number of degrees.
	 */
	public void rotate(double degreesToTurn, double maxSpeed) {
		if (!parameters.useGyro) {
			degreesToTurn = Math.toRadians(degreesToTurn);
			add(new OldStraightMoveTask(robot, calcPower(0, 0,
					Math.signum(degreesToTurn)),
					Math.abs(degreesToTurn) * OldStraightMoveTask.TURN_MULT,
					maxSpeed));
		} else {
			add(new GyroRotateTask(degreesToTurn, maxSpeed));
		}
	}
	
	private double getCurAngle() {
		return robot.getAngle();
	}
	
	public class GyroRotateTask extends TaskAdapter {
		private static final double ANGLE_TOLERANCE = 1.5;
		private static final int CONSECUTIVE_HITS = 5;
		
		private final double maxSpeed;
		private final double degreesToTurn;
		private int consecutive = 0;
		
		public GyroRotateTask(double degreesToTurn, double maxSpeed) {
			if (maxSpeed <= 0 || maxSpeed > 1)
				throw new IllegalArgumentException("MaxSpeed not 0-1");
			this.degreesToTurn = degreesToTurn;
			this.maxSpeed = maxSpeed;
			turnPID.setSetpoint(0);
			turnPID.setOutputLimits(1);
			turnPID.setOutputRampRate(0.06);
			turnPID.setOutputFilter(0.05);
		}
		
		@Override
		public void start() {
			targetAngle += degreesToTurn;
			robot.getWheels().setMode(RUN_USING_ENCODER);
		}
		
		@Override
		public void stop() {
			robot.getWheels().stop();
		}
		
		@Override
		public boolean loop() {
			double curAngle = getCurAngle();
			double output = turnPID.getOutput(curAngle - targetAngle);
			robot.getWheels().setPower(TURN.scaleTo(output).scaleDownTo(maxSpeed));
			boolean hit = Math.abs(curAngle - targetAngle) < ANGLE_TOLERANCE;
			if (hit) {
				consecutive++;
			} else {
				consecutive = 0;
			}
			return GlobalVars.shouldComplete && consecutive >= CONSECUTIVE_HITS;
		}
		
	}
	
	public static class Parameters {
		public boolean useGyro = false;
		public int wheelTolerance = 25;
	}
	
}
