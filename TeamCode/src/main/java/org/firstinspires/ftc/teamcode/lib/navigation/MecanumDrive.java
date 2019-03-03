package org.firstinspires.ftc.teamcode.lib.navigation;

import android.annotation.SuppressLint;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.lib.robot.IRobot;
import org.firstinspires.ftc.teamcode.lib.tasks.CompositeTask;
import org.firstinspires.ftc.teamcode.lib.tasks.Task;
import org.firstinspires.ftc.teamcode.lib.tasks.TaskAdapter;
import org.firstinspires.ftc.teamcode.lib.tasks.TaskProgram;
import org.firstinspires.ftc.teamcode.lib.timer.SingleTimer;
import org.jetbrains.annotations.NotNull;

import static org.firstinspires.ftc.teamcode.config.NavigationConstants.DEFAULT_MAX_ACCELERATIONS;
import static org.firstinspires.ftc.teamcode.config.NavigationConstants.ENCODER_TICKS_PER_INCH;

/**
 * A task program that controls the autonomous motion of wheels.
 * Uses motor encoders to dynamically correct motion, and gyroscope to
 * correct orientation and direction.
 * Orientation is always relative to the current robot position, AFTER any simultaneous or
 * previous turns.
 */
@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
public class MecanumDrive extends TaskProgram {
	public static final AngleUnit          ourAngleUnit    = AngleUnit.RADIANS;
	public static final DistanceUnit       ourDistanceUnit = DistanceUnit.INCH;
	private final       Parameters         parameters;
	private final       AutoMoveController autoMoveController;
	
	public MecanumDrive(IRobot robot, Parameters parameters) {
		super("Mecanum Drive", true);
		this.parameters = parameters.clone();
		autoMoveController = new AutoMoveController(
				robot,
				new PositionTracker(this.parameters.encoderTicksPerUnit),
				new RampedMoveController(this.parameters.maxAccelerations),
				new SingleTimer());
		
		if (this.parameters.adjustAtEnd) super.addOnDoneTask(
				new MoveTaskBuilder().considerMove(1)
				                     .considerTurn(1)
				                     .build(true));
		super.addOnDoneTask(autoMoveController::stopRobot);
	}
	
	private XY toOurUnit(double x, double y, DistanceUnit distanceUnit) {
		return new XY(ourDistanceUnit.fromUnit(distanceUnit, x),
		              ourDistanceUnit.fromUnit(distanceUnit, y));
	}
	
	private double toOurUnit(double angle, AngleUnit unit) {
		return ourAngleUnit.getUnnormalized().fromUnit(unit.getUnnormalized(), angle);
	}
	
	public MoveTaskBuilder move(double x, double y, DistanceUnit unit, double maxSpeed) {
		return new MoveTaskBuilder().move(x, y, unit, maxSpeed);
	}
	
	//things that get builders
	public MoveTaskBuilder newTask() {
		return new MoveTaskBuilder();
	}
	
	public MoveTaskBuilder turn(double angle, AngleUnit unit, double maxSpeed) {
		return new MoveTaskBuilder().turn(angle, unit, maxSpeed);
	}
	
	public MoveTaskBuilder turn(double angle, double maxSpeed) {
		return new MoveTaskBuilder().turn(angle, maxSpeed);
	}
	
	public MoveTaskBuilder move(double x, double y, double maxSpeed) {
		return new MoveTaskBuilder().move(x, y, maxSpeed);
	}
	
	public MecanumDrive thenMove(double x, double y, DistanceUnit unit, double maxSpeed,
	                             boolean fine) {
		return move(x, y, unit, maxSpeed).go(fine);
	}
	
	public MecanumDrive thenTurn(double toTurn, AngleUnit unit, double maxSpeed, boolean fine) {
		return turn(toTurn, unit, maxSpeed).go(fine);
	}
	
	public MecanumDrive thenTurn(double toTurn, double maxSpeed, boolean fine) {
		return turn(toTurn, maxSpeed).go(fine);
	}
	
	public MecanumDrive thenTurn(double toTurn, AngleUnit unit, double maxSpeed) {
		return turn(toTurn, unit, maxSpeed).go();
	}
	
	public MecanumDrive thenTurn(double toTurn, double maxSpeed) {
		return turn(toTurn, maxSpeed).go();
	}
	
	public MecanumDrive thenMove(double x, double y, double maxSpeed, boolean fine) {
		return move(x, y, maxSpeed).go(fine);
	}
	
	public MecanumDrive thenMove(double x, double y, DistanceUnit unit, double maxSpeed) {
		return move(x, y, unit, maxSpeed).go();
	}
	
	public MecanumDrive thenMove(double x, double y, double maxSpeed) {
		return move(x, y, maxSpeed).go();
	}
	
	public MecanumDrive thenAdjust() {
		new MoveTaskBuilder().considerMove(1).considerTurn(1).go(true);
		return thenStopRobot();
	}
	
	public MecanumDrive thenStopRobot() {
		return then(autoMoveController::stopRobot);
	}
	
	@Override
	public MecanumDrive then(Task task) {
		add(task);
		return this;
	}
	
	@Override
	public void start() {
		autoMoveController.updateLocation();
		autoMoveController.setTargetPositionHere();
		super.start();
	}
	
	@Override
	public MecanumDrive thenSleep(long millis) {
		super.thenSleep(millis);
		return this;
	}
	
	/**
	 * A task that simply updates the target position and angle.
	 * Moves are done relative to the current robot position1!1!!!!1!
	 */
	private class ChangeTargetPositionTask implements Task {
		private final XYR toMove;
		
		private ChangeTargetPositionTask(XYR toMove) {
			this.toMove = toMove;
		}
		
		@Override
		public void run() {
			autoMoveController.addToTargetPositionRelative(toMove);
		}
		
		@NotNull
		@Override
		public String toString() {
			return String.format("ChangeTargetLocation: %s", toMove);
		}
	}
	
	/**
	 * A task that actually moves the robot to the target location with
	 * specified tolerances and maximum speeds for both translation and
	 * rotation.
	 */
	private class PositionRobotTask extends TaskAdapter {
		private final Magnitudes maxVelocities;
		private final Magnitudes tolerances;
		private final int        consecutive;
		
		private double numConsecutive;
		
		/**
		 * Constructs a new MoveTask, that makes sure the robot goes to the right location
		 *
		 * @param maxTranslationalSpeed  The maximum translational speed this robot will move at.
		 * @param translationalTolerance The maximum distance the robot will tolerate being away
		 *                               from the target position before stop. A negative
		 *                               tolerance indicates infinite tolerance.
		 * @param maxAngularSpeed        The maximum angular speed to move at.
		 * @param angularTolerance       The maximum angle the robot will tolerate being away from
		 *                               the target angle before stop. A negative tolerance
		 *                               indicates infinite tolerance.
		 * @param consecutive            The number of consecutive cycles the the above two
		 */
		public PositionRobotTask(double maxTranslationalSpeed, double translationalTolerance,
		                         double maxAngularSpeed, double angularTolerance,
		                         int consecutive) {
			if (maxAngularSpeed < 0 || maxTranslationalSpeed < 0 || consecutive <= 0)
				throw new IllegalArgumentException();
			this.maxVelocities = new Magnitudes(maxTranslationalSpeed, maxAngularSpeed);
			this.tolerances = new Magnitudes(translationalTolerance, angularTolerance);
			this.consecutive = consecutive;
		}
		
		@Override
		public void start() {
			autoMoveController.resetInternalTimer();
			autoMoveController.setMaxVelocities(maxVelocities);
		}
		
		@Override
		public boolean loop() {
			autoMoveController.updateLocation();
			autoMoveController.moveToTarget();
			boolean hit = autoMoveController.isOnTarget(tolerances);
			if (hit) numConsecutive++;
			else numConsecutive = 0;
			return numConsecutive >= consecutive;
		}
		
		@NotNull
		@SuppressLint("DefaultLocale")
		@Override
		public String toString() {
			return String.format("MoveTask: %s", tolerances);
		}
		
	}
	
	/**
	 * Creates move tasks: a composite task of changing target position, then moving there.
	 * Will only consider translation/rotation tolerances if an actual movement/rotation occurred,
	 * respectively.
	 */
	//Google "OOP builder"
	public class MoveTaskBuilder {
		private XYR    toMove              = XYR.ZERO;
		private double maxTranslationSpeed = -1;
		private double maxAngularSpeed     = -1;
		
		private MoveTaskBuilder() { //only can build from within here.
		}
		
		public MoveTaskBuilder phantomTurn(double toTurn, AngleUnit unit) {
			toMove = toMove.addToAngle(toOurUnit(toTurn, unit));
			return this;
		}
		
		public MoveTaskBuilder phantomMove(double x, double y, DistanceUnit unit) {
			this.toMove = this.toMove.addToXY(toOurUnit(x, y, unit).rotate(this.toMove.angle));
			return this;
		}
		
		/**
		 * Adds turning this moveTask.
		 */
		public MoveTaskBuilder turn(double toTurn, AngleUnit unit, double maxSpeed) {
			if (maxSpeed < 0) throw new IllegalArgumentException();
			toMove = toMove.addToAngle(toOurUnit(toTurn, unit));
			this.maxAngularSpeed = maxSpeed;
			return this;
		}
		
		/**
		 * Adds turning to this moveTask.
		 */
		public MoveTaskBuilder turn(double angle, double maxSpeed) {
			return turn(angle, ourAngleUnit, maxSpeed);
		}
		
		/**
		 * Adds movement to this move task.
		 */
		public MoveTaskBuilder move(double x, double y, DistanceUnit unit, double maxSpeed) {
			if (maxSpeed < 0) throw new IllegalArgumentException();
			//to account for previous rotations, rotate.
			this.toMove = this.toMove.addToXY(toOurUnit(x, y, unit).rotate(this.toMove.angle));
			this.maxTranslationSpeed = maxSpeed;
			return this;
		}
		
		/**
		 * Adds movement to this move task.
		 */
		public MoveTaskBuilder move(double x, double y, double maxSpeed) {
			return move(x, y, ourDistanceUnit, maxSpeed);
		}
		
		/**
		 * Requires position to be correct enough to continue.
		 */
		public MoveTaskBuilder considerMove(double maxTranslationalSpeed) {
			this.maxTranslationSpeed = maxTranslationalSpeed;
			return this;
		}
		
		/**
		 * Requires angle to be correct enough to continue
		 */
		public MoveTaskBuilder considerTurn(double maxAngularSpeed) {
			this.maxAngularSpeed = maxAngularSpeed;
			return this;
		}
		
		/**
		 * Adds the being built move task to the {@link TaskProgram}
		 */
		public MecanumDrive go() {
			return go(false);
		}
		
		/**
		 * Adds the being built move task to the {@link TaskProgram}
		 *
		 * @param fine if true, less tolerant on target position
		 */
		public MecanumDrive go(boolean fine) {
			return then(build(fine));
		}
		
		private Task build(boolean fine) {
			if (maxTranslationSpeed == -1 && maxAngularSpeed == -1)
				throw new IllegalArgumentException();
			Magnitudes tolerance = fine ? parameters.toleranceFine : parameters.toleranceCoarse;
			double translationalTolerance =
					maxTranslationSpeed != -1 ? tolerance.translational : -1;
			double angularTolerance = maxAngularSpeed != -1 ? tolerance.angular : -1;
			if (maxTranslationSpeed == -1)
				maxTranslationSpeed = maxAngularSpeed * parameters.idleSpeedMult.translational;
			if (maxAngularSpeed == -1)
				maxAngularSpeed = maxTranslationSpeed * parameters.idleSpeedMult.angular;
			int consecutive = fine ? parameters.consecutiveFine : parameters.consecutiveCoarse;
			return new CompositeTask(new ChangeTargetPositionTask(toMove),
			                         new PositionRobotTask(maxTranslationSpeed,
			                                               translationalTolerance, maxAngularSpeed,
			                                               angularTolerance, consecutive));
		}
	}
	
	public static class Parameters implements Cloneable {
		
		public double     encoderTicksPerUnit = ENCODER_TICKS_PER_INCH;
		public Magnitudes maxAccelerations    = DEFAULT_MAX_ACCELERATIONS;
		public Magnitudes toleranceCoarse     = new Magnitudes(10, Math.toRadians(10));
		public Magnitudes toleranceFine       = new Magnitudes(2, Math.toRadians(3));
		public Magnitudes idleSpeedMult       = new Magnitudes(0.3);
		public int        consecutiveCoarse   = 1;
		public int        consecutiveFine     = 3;
		public boolean    adjustAtEnd         = false;
		
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
