package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RoverRuckus.externalLib.MiniPID;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.Task;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskAdapter;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskProgram;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.GlobalVars;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSet;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPosition;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.Robot;

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
	private final Robot      robot;
	private final MotorSet   wheels;
	
	private final MiniPID turnPID        = new MiniPID(0.035, 0, 0.13);
	private       double  targetAngle    = 0;
	private       XYR     targetPosition = new XYR();
	private       XYR     curPosition    = new XYR();
	
	public MecanumDrive(Robot robot, Parameters parameters) {
		super(true);
		this.robot = Objects.requireNonNull(robot);
		this.parameters = Objects.requireNonNull(parameters);
		this.wheels = robot.getWheels();
		init();
	}
	
	private void init() {
		turnPID.setSetpoint(0);
		turnPID.setOutputLimits(1);
		turnPID.setOutputRampRate(0.06);
		turnPID.setOutputFilter(0.05);
	}
	
	@Override
	public MecanumDrive then(Task task) {
		add(task);
		return this;
	}
	
	@Override
	public void start() {
		targetAngle = robot.getAngle();
		wheels.setTargetPositionTolerance(parameters.wheelTolerance);
		super.start();
	}
	
	private void moveExact(
			double directionRadians, double distance, double speed) {
		
	}
	
	private void moveCoarse(
			double directionRadians, double distance, double speed) {
		
	}
	
	/**
	 * Adds a Task that represents moving the robot in a straight line the
	 * specified direction and distance
	 */
	public void move(double direction, double distance, double speed) {
		direction = Math.toRadians(direction);
		add(new OldStraightMoveTask(robot, calcPower(direction, 1, 0),
		                            distance * OldStraightMoveTask.MOVE_MULT,
		                            speed));
	}
	
	/**
	 * Moves the robot to the specified relative location on the coordinate
	 * plane
	 */
	public MecanumDrive moveXY(double x, double y, double speed) {
		double direction = Math.toDegrees(Math.atan2(x, y));
		double distance = Math.hypot(x, y);
		move(direction, distance, speed);
		return this;
	}
	
	public void moveXY(double x, double y, DistanceUnit unit, double speed) {
		//TODO!!!
		//NOTE: DETERMINE UNITS
	}
	
	/**
	 * Rotates the robot a specific number of degrees.
	 */
	public MecanumDrive rotate(double degreesToTurn, double speed) {
		if (!parameters.useGyro) {
			degreesToTurn = Math.toRadians(degreesToTurn);
			add(new OldStraightMoveTask(robot, calcPower(0, 0, Math.signum(
					degreesToTurn)), Math.abs(degreesToTurn) *
			                         OldStraightMoveTask.TURN_MULT, speed));
		} else {
			add(new GyroRotateTask(degreesToTurn, speed));
		}
		return this;
	}
	
	private double getCurAngle() {
		return robot.getAngle();
	}
	
	private abstract class GyroAwareMoveTask extends TaskAdapter {
		private MotorSetPosition prevPosition;
		
		protected abstract boolean doneCondition();
		
		@Override
		public abstract void start();
		
		@Override
		public boolean loop() {
			double curAngle = getCurAngle();
			MotorSetPosition currentPosition = wheels.getCurrentPosition();
			MotorSetPosition positionDelta =
					currentPosition.delta(prevPosition);
			
			prevPosition = currentPosition;
			return false;
		}
		
		@SuppressWarnings("Duplicates")
		private XYR motorDeltaToXYR(MotorSetPosition delta) {
			//best approximation of inverse calcMotorSetPosition.
			double speed = 0, dir = 0, turnRate = 0;
			double robAngle = dir + Math.PI / 4;
			double v1 = speed * Math.sin(robAngle) + turnRate;
			double v2 = speed * Math.cos(robAngle) - turnRate;
			double v3 = speed * Math.cos(robAngle) + turnRate;
			double v4 = speed * Math.sin(robAngle) - turnRate;
			
		}
	}
	
	private class GyroRotateTask extends TaskAdapter {
		private static final double ANGLE_TOLERANCE  = 1.5;
		private static final int    CONSECUTIVE_HITS = 5;
		
		private final double speed;
		private final double degreesToTurn;
		private       int    consecutive = 0;
		
		public GyroRotateTask(double degreesToTurn, double speed) {
			if (speed <= 0 || speed > 1)
				throw new IllegalArgumentException("MaxSpeed not 0-1");
			this.degreesToTurn = degreesToTurn;
			this.speed = speed;
		}
		
		@Override
		public void start() {
			targetAngle += degreesToTurn;
			wheels.setMode(RUN_USING_ENCODER);
		}
		
		@Override
		public void stop() {
			wheels.stop();
		}
		
		@Override
		public boolean loop() {
			double curAngle = getCurAngle();
			double output = turnPID.getOutput(curAngle - targetAngle);
			wheels.setPower(TURN.scaleTo(output).scaleDownTo(speed));
			boolean hit = Math.abs(curAngle - targetAngle) < ANGLE_TOLERANCE;
			if (hit) {
				consecutive++;
			} else {
				consecutive = 0;
			}
			return GlobalVars.shouldComplete && consecutive >= CONSECUTIVE_HITS;
		}
		
	}
	
	/**
	 * XY position and rotation.
	 */
	private static class XYR {
		public double x, y, d;
		public final DistanceUnit unit = DistanceUnit.CM;
	}
	
	public static class Parameters {
		public boolean useGyro        = false;
		public int     wheelTolerance = 25;
	}
	
}
