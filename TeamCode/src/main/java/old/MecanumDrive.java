//package org.firstinspires.ftc.teamcode.RoverRuckus.old;
//
//import com.qualcomm.robotcore.util.ElapsedTime;
//import org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive.StraightMoveTask;
//import org.firstinspires.ftc.teamcode.lib.tasks.Task;
//import org.firstinspires.ftc.teamcode.lib.tasks.TaskAdapter;
//import org.firstinspires.ftc.teamcode.lib.tasks.TaskProgram;
//import org.firstinspires.ftc.teamcode.lib.navigation.AutoMoveCalculator;
//import org.firstinspires.ftc.teamcode.lib.navigation.WorldAxesDualPIDMoveCalc;
//import org.firstinspires.ftc.teamcode.lib.robot.IRobot;
//import org.firstinspires.ftc.teamcode.lib.robot.MotorSet;
//
//import java.util.Objects;
//
//import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
//import static org.firstinspires.ftc.teamcode.lib.robot.MotorSetPower.TURN;
//import static org.firstinspires.ftc.teamcode.lib.robot.MotorSetPower
// .fromPolarNonStandard;
//
///**
// * A task program that controls the autonomous motion of wheels.
// * Uses motor encoders to dynamically correct motion,
// * Optionally uses gyroscope (YOU SHOULD USE THIS) to increase
// * accuracy in turning and direction.
// * <p>
// * Orientation: <br>
// * <pre>
// *           <-(0)->
// *             +Y
// *              |
// * -90deg  -X --O-- +X  +90deg
// *              |
// *             -Y
// *          +/-180deg
// * </pre>
// */
//@SuppressWarnings("ALL")
//@Deprecated
//public class MecanumDrive extends TaskProgram {
//	private final IRobot      robot;
//	private final MotorSet    wheels;
//	private final ElapsedTime elapsedTime = new ElapsedTime();
//
//	private final AutoMoveCalculator autoMoveCalculator = new WorldAxesDualPIDMoveCalc();
//
//	private double targetAngle = 0;
//
//	public MecanumDrive(IRobot robot) {
//		super("MecanumDrive", true);
//		this.robot = Objects.requireNonNull(robot);
//		this.wheels = robot.getWheels();
//	}
//
//	@Override
//	public MecanumDrive then(Task task) {
//		add(task);
//		return this;
//	}
//
//	@Override
//	public void start() {
//		targetAngle = robot.getAngle();
//		wheels.setTargetPositionTolerance(25);
//		super.start();
//	}
//
//	/**
//	 * Adds a Task that represents moving the robot in a straight line the
//	 * specified direction and distance
//	 */
//	public void move(double direction, double distance, double speed) {
//		direction = Math.toRadians(direction);
//		add(new StraightMoveTask(robot, fromPolarNonStandard(direction, 1, 0),
//		                         distance * StraightMoveTask.MOVE_MULT, speed));
//	}
//
//	/**
//	 * Moves the robot to the specified relative location on the XY
//	 * plane
//	 */
//	public MecanumDrive moveXY(double x, double y, double speed) {
//		double direction = Math.toDegrees(Math.atan2(x, y));
//		double distance = Math.hypot(x, y);
//		move(direction, distance, speed);
//		return this;
//	}
//
//	/**
//	 * Rotates the robot a specific number of degrees.
//	 */
//	public MecanumDrive rotate(double degreesToTurn, double speed) {
//		degreesToTurn = Math.toRadians(degreesToTurn);
//		if (!false) {
//			add(new StraightMoveTask(robot, TURN.scale(Math.signum(degreesToTurn)),
//			                         Math.abs(degreesToTurn) * StraightMoveTask.TURN_MULT, speed));
//		} else {
//			add(new GyroRotateTask(degreesToTurn, speed));
//		}
//		return this;
//	}
//
//	private double getCurAngle() {
//		return robot.getAngle();
//	}
//
//	private class GyroRotateTask extends TaskAdapter {
//		private static final double ANGLE_TOLERANCE  = 0.02;
//		private static final int    CONSECUTIVE_HITS = 4;
//
//		private final double speed;
//		private final double degreesToTurn;
//		private       int    consecutive = 0;
//
//		protected GyroRotateTask(double degreesToTurn, double speed) {
//			if (speed <= 0 || speed > 1) throw new IllegalArgumentException("MaxSpeed not 0-1");
//			this.degreesToTurn = degreesToTurn;
//			this.speed = speed;
//		}
//
//		@Override
//		public void start() {
//			targetAngle += degreesToTurn;
//			wheels.setMode(RUN_USING_ENCODER);
//			elapsedTime.reset();
//		}
//
//		@Override
//		public void stop() {
//			wheels.stop();
//		}
//
//		@Override
//		public boolean loop() {
//			throw new UnsupportedOperationException();
////			//double curAngle = getCurAngle();
////			//RobotLog.dd("MecanumDrive", "curAngle: %.5f", curAngle);
////			MotorSetPower output =
////					autoMoveCalculator.getMovement(new XYR(XY.ZERO, targetAngle),
////					                                 new XYR(XY.ZERO, curAngle),
////					                                 new Magnitudes(speed),
////					                                 elapsedTime.getSeconds());
////			elapsedTime.reset();
////			//wheels.setPower(output);
////			boolean hit = Math.abs(curAngle - targetAngle) < ANGLE_TOLERANCE;
////			if (hit) {
////				consecutive++;
////			} else {
////				consecutive = 0;
////			}
////			return consecutive >= CONSECUTIVE_HITS;
//		}
//
//	}
//
//	@Override
//	public MecanumDrive thenSleep(long millis) {
//		super.thenSleep(millis);
//		return this;
//	}
//
//}
