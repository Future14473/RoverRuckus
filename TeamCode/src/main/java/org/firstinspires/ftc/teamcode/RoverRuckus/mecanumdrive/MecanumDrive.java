package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.Task;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskAdapter;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskProgram;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.AxesDualPIDTargLocAlg;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.TargetLocationAlgorithm;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.XYR;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSet;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

import java.util.Objects;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower.TURN;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower.fromPolarNonStandard;

/**
 * A task program that controls the autonomous motion of wheels.
 * Uses motor encoders to dynamically correct motion,
 * Optionally uses gyroscope (YOU SHOULD USE THIS) to increase
 * accuracy in turning and direction.
 * <p>
 * Orientation: <br>
 * <pre>
 *           <-(0)->
 *             +Y
 *              |
 * -90deg  -X --O-- +X  +90deg
 *              |
 *             -Y
 *          +/-180deg
 * </pre>
 */
@SuppressWarnings("ALL")
@Deprecated
public class MecanumDrive extends TaskProgram {
	private final Parameters  parameters;
	private final IRobot      robot;
	private final MotorSet    wheels;
	private final ElapsedTime elapsedTime = new ElapsedTime();
	
	private final TargetLocationAlgorithm targetLocationAlgorithm =
			new AxesDualPIDTargLocAlg();
	
	private double targetAngle = 0;
	
	public MecanumDrive(IRobot robot, Parameters parameters) {
		super("MecanumDrive", true);
		this.robot = Objects.requireNonNull(robot);
		this.parameters = Objects.requireNonNull(parameters);
		this.wheels = robot.getWheels();
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
	
	/**
	 * Adds a Task that represents moving the robot in a straight line the
	 * specified direction and distance
	 */
	public void move(double direction, double distance, double speed) {
		direction = Math.toRadians(direction);
		add(new StraightMoveTask(robot,
		                         fromPolarNonStandard(direction, 1, 0),
		                         distance * StraightMoveTask.MOVE_MULT,
		                         speed));
	}
	
	/**
	 * Moves the robot to the specified relative location on the XY
	 * plane
	 */
	public MecanumDrive moveXY(double x, double y, double speed) {
		double direction = Math.toDegrees(Math.atan2(x, y));
		double distance = Math.hypot(x, y);
		move(direction, distance, speed);
		return this;
	}
	
	/**
	 * Rotates the robot a specific number of degrees.
	 */
	public MecanumDrive rotate(double degreesToTurn, double speed) {
		degreesToTurn = Math.toRadians(degreesToTurn);
		if (!parameters.useGyro) {
			add(new StraightMoveTask(robot,
			                         TURN.scale(Math.signum(degreesToTurn)),
			                         Math.abs(degreesToTurn) * StraightMoveTask.TURN_MULT,
			                         speed));
		} else {
			add(new GyroRotateTask(degreesToTurn, speed));
		}
		return this;
	}
	
	private double getCurAngle() {
		return robot.getAngle();
	}
	
	private class GyroRotateTask extends TaskAdapter {
		private static final double ANGLE_TOLERANCE  = 0.02;
		private static final int    CONSECUTIVE_HITS = 4;
		
		private final double speed;
		private final double degreesToTurn;
		private       int    consecutive = 0;
		
		protected GyroRotateTask(double degreesToTurn, double speed) {
			if (speed <= 0 || speed > 1) throw new IllegalArgumentException("MaxSpeed not 0-1");
			this.degreesToTurn = degreesToTurn;
			this.speed = speed;
		}
		
		@Override
		public void start() {
			targetAngle += degreesToTurn;
			wheels.setMode(RUN_USING_ENCODER);
			elapsedTime.reset();
		}
		
		@Override
		public void stop() {
			wheels.stop();
		}
		
		@Override
		public boolean loop() {
			double curAngle = getCurAngle();
			//RobotLog.dd("MecanumDrive", "curAngle: %.5f", curAngle);
			MotorSetPower output =
					targetLocationAlgorithm.getPower(new XYR(XY.ZERO, targetAngle),
					                                 new XYR(XY.ZERO, curAngle),
					                                 speed,
					                                 speed,
					                                 elapsedTime.seconds());
			elapsedTime.reset();
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
	
	@Override
	public MecanumDrive sleep(long millis) {
		super.sleep(millis);
		return this;
	}
	
	public static class Parameters {
		public boolean useGyro        = false;
		public int     wheelTolerance = 25;
	}
	
}
