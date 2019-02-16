package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.Task;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskAdapter;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskProgram;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSet;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPosition;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

import java.util.Objects;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower.calcPolarNonstandard;

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
public class MecanumDriveBetter extends TaskProgram {
	private final Parameters parameters;
	private final IRobot     robot;
	private final MotorSet   wheels;
	
	private final LocationMoveController moveController;
	
	private XY     targetLocation = new XY();
	private XY     curLocation    = new XY();
	private double targetAngle    = 0;
	
	//current angle is method.
	public MecanumDriveBetter(IRobot robot, Parameters parameters) {
		super(true);
		this.robot = Objects.requireNonNull(robot);
		this.parameters = parameters.clone();
		this.wheels = robot.getWheels();
		moveController = new LocationMoveController(parameters.rampRate);
	}
	
	@Override
	public MecanumDriveBetter then(Task task) {
		add(task);
		return this;
	}
	
	@Override
	public void start() {
		targetAngle = robot.getAngle();
		wheels.setTargetPositionTolerance(parameters.wheelTolerance);
		super.start();
	}
	
	/*private void moveExact(
			double directionRadians, double distance, double speed) {
		
	}
	
	private void moveCoarse(
			double directionRadians, double distance, double speed) {
		
	}*/
	
	/**
	 * Adds a Task that represents moving the robot in a straight line the
	 * specified direction and distance
	 */
	public void move(double direction, double distance, double speed) {
		direction = Math.toRadians(direction);
		add(new OldStraightMoveTask(robot,
		                            calcPolarNonstandard(direction, 1, 0),
		                            distance * OldStraightMoveTask.MOVE_MULT,
		                            speed));
	}
	
	/**
	 * Moves the robot to the specified relative location on the coordinate
	 * plane
	 */
	public MecanumDriveBetter moveXY(double x, double y, double speed) {
		double direction = Math.toDegrees(Math.atan2(x, y));
		double distance = Math.hypot(x, y);
		move(direction, distance, speed);
		return this;
	}
	
/*	public void moveXY(double x, double y, DistanceUnit unit, double speed) {
		//TODO!!!
		//NOTE: DETERMINE UNITS
	}*/
	
	/**
	 * Rotates the robot a specific number of degrees.
	 */
	public MecanumDriveBetter rotate(double degreesToTurn, double speed) {
		if (!parameters.useGyro) {
			degreesToTurn = Math.toRadians(degreesToTurn);
			add(new OldStraightMoveTask(robot,
			                            calcPolarNonstandard(0, 0, Math.signum(degreesToTurn)),
			                            Math.abs(degreesToTurn) * OldStraightMoveTask.TURN_MULT,
			                            speed));
		} else {
			add(new GyroRotateTask(degreesToTurn, speed));
		}
		return this;
	}
	
	private double getCurAngle() {
		return robot.getAngle();
	}
	
	private abstract class GyroAwareMoveTask extends TaskAdapter {
		protected double           curAngle;
		private   MotorSetPosition lastMotorPos;
		
		protected abstract boolean inLoop();
		
		@Override
		public boolean loop() {
			updateLocation();
			return inLoop();
		}
		
		private void updateLocation() {
			curAngle = getCurAngle();
			MotorSetPosition curMotorPos = wheels.getCurrentPosition();
			MotorSetPosition deltaMotorPos = curMotorPos.subtract(lastMotorPos);
			lastMotorPos = curMotorPos;
			XY deltaLocation = deltaMotorToDeltaLoc(deltaMotorPos).rotate(curAngle);
			curLocation = curLocation.add(deltaLocation);
		}
		
		private XY deltaMotorToDeltaLoc(MotorSetPosition delta) {
			double d1 = delta.get(0), d2 = delta.get(1), d3 = delta.get(2), d4 = delta.get(3);
			double direction = Math.atan2(d2 + d3, d1 + d4);
			double distance = Math.hypot(d2 + d3, d1 + d4) / parameters.moveMult;
			direction += Math.PI / 4;
			return XY.fromPolar(direction, distance);
		}
	}
	
	private class GyroRotateTask extends TaskAdapter {
		private static final double ANGLE_TOLERANCE  = 1.5;
		private static final int    CONSECUTIVE_HITS = 4;
		
		private final double speed;
		private final double degreesToTurn;
		private       int    consecutive = 0;
		
		public GyroRotateTask(double degreesToTurn, double speed) {
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
			MotorSetPower output =
				moveController.getPower(targetLocation, curLocation, targetAngle, curAngle, speed);
			wheels.setPower(output);
			boolean hit = Math.abs(curAngle - targetAngle) < ANGLE_TOLERANCE;
			if (hit) {
				consecutive++;
			} else {
				consecutive = 0;
			}
			return consecutive >= CONSECUTIVE_HITS;
		}
		
	}
	
	public static class Parameters implements Cloneable {
		public final boolean useGyro        = true;
		public       int     wheelTolerance = 25;
		public       int     rampRate       = 4;
		public       int     moveMult       = 4450;
		
		@Override
		public Parameters clone() {
			try {
				return (Parameters) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new Error();
			}
		}
	}
	
}
