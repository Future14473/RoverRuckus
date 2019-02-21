package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import android.annotation.SuppressLint;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.CompositeTask;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.Task;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskAdapter;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskProgram;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.CycleTime;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.*;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.DEFAULT_MAX_ACCELERATIONS;
import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.ENCODER_TICKS_PER_INCH;

/**
 * A task program that controls the autonomous motion of wheels.
 * Uses motor encoders to dynamically correct motion, and gyroscope to
 * correct orientation and direction.
 * Orientation is always relative to the current robot position, AFTER any simultaneous or
 * previous turns.
 */
//TODO: MAYBE RADIANS/SECOND^2 INSTEAD OF POWER LEVEL POWER PER SECOND!??!?!
@SuppressWarnings("unused")
public class MecanumDriveBetter extends TaskProgram {
	private static final AngleUnit    ourAngleUnit    = AngleUnit.RADIANS;
	private static final DistanceUnit ourDistanceUnit = DistanceUnit.INCH;
	
	private final Parameters         parameters;
	private final AutoMoveController moveController;
	
	private final CycleTime time = new CycleTime();
	
	public MecanumDriveBetter(IRobot robot, Parameters parameters) {
		super("Mecanum Drive", true);
		this.parameters = parameters.clone();
		moveController = new AutoMoveController(robot,
		                                        new PositionTracker(
				                                        this.parameters.encoderTicksPerUnit),
		                                        new RampedMoveController(
				                                        parameters.maxAccelerations));
		
		if (this.parameters.adjustAtEnd)
			super.addOnDoneTask(
					new MoveTaskBuilder().move(XY.ZERO, 0.6, DistanceUnit.INCH)
					                     .turn(0, 0.6, AngleUnit.RADIANS)
					                     .build(true));
		super.addOnDoneTask(robot.getWheels()::stop);
	}
	
	private double toOurUnit(double angle, AngleUnit unit) {
		return ourAngleUnit.getUnnormalized().fromUnit(unit.getUnnormalized(), angle);
	}
	
	private XY toOurUnit(XY XY, DistanceUnit distanceUnit) {
		return new XY(ourDistanceUnit.fromUnit(distanceUnit, XY.x),
		              ourDistanceUnit.fromUnit(distanceUnit, XY.y));
	}
	
	//things that get builders
	public MoveTaskBuilder newTask() {
		return new MoveTaskBuilder();
	}
	
	public MoveTaskBuilder turn(double angle, double maxSpeed, AngleUnit unit) {
		return new MoveTaskBuilder().turn(angle, maxSpeed, unit);
	}
	
	public MoveTaskBuilder turn(double angle, double maxSpeed) {
		return new MoveTaskBuilder().turn(angle, maxSpeed);
	}
	
	public MoveTaskBuilder move(XY toMove, double maxSpeed, DistanceUnit unit) {
		return new MoveTaskBuilder().move(toMove, maxSpeed, unit);
	}
	
	public MoveTaskBuilder move(XY toMove, double maxSpeed) {
		return new MoveTaskBuilder().move(toMove, maxSpeed);
	}
	
	//go turn
	public MecanumDriveBetter goTurn(double angle, double maxSpeed, AngleUnit unit, boolean fine) {
		return turn(angle, maxSpeed, unit).go(fine);
	}
	
	public MecanumDriveBetter goTurn(double angle, double maxSpeed, boolean fine) {
		return turn(angle, maxSpeed).go(fine);
	}
	
	public MecanumDriveBetter goTurn(double angle, double maxSpeed, AngleUnit unit) {
		return turn(angle, maxSpeed, unit).go();
	}
	
	public MecanumDriveBetter goTurn(double angle, double maxSpeed) {
		return turn(angle, maxSpeed).go();
	}
	
	//go move
	public MecanumDriveBetter goMove(XY toMove, double maxSpeed, DistanceUnit unit,
	                                 boolean fine) {
		return move(toMove, maxSpeed, unit).go(fine);
	}
	
	public MecanumDriveBetter goMove(XY toMove, double maxSpeed, boolean fine) {
		return move(toMove, maxSpeed).go(fine);
	}
	
	public MecanumDriveBetter goMove(XY toMove, double maxSpeed, DistanceUnit unit) {
		return move(toMove, maxSpeed, unit).go();
	}
	
	public MecanumDriveBetter goMove(XY toMove, double maxSpeed) {
		return move(toMove, maxSpeed).go();
	}
	
	@Override
	public MecanumDriveBetter then(Task task) {
		add(task);
		return this;
	}
	
	@Override
	public void start() {
		moveController.reset();
		super.start();
	}
	
	@Override
	public MecanumDriveBetter sleep(long millis) {
		super.sleep(millis);
		return this;
	}
	
	/**
	 * A task that simply updates the target position and angle.
	 * MovePositions are relative to target angle.
	 */
	private class ChangeTargetPositionTask implements Task {
		private final XYR toMove;
		
		private ChangeTargetPositionTask(XYR toMove) {
			this.toMove = toMove;
		}
		
		@Override
		public void run() {
			moveController.addToTargetPosition(toMove);
		}
		
		@Override
		public String toString() {
			return String.format("ChangeTargetLocation: %s",
			                     toMove);
		}
	}
	
	/**
	 * A task that actually moves the robot to the target location with
	 * specified tolerances and maximum speeds for both translation and
	 * rotation.
	 */
	class PositionRobotTask extends TaskAdapter {
		private final double maxAngularSpeed;
		private final double angularTolerance;
		private final double maxTranslationalSpeed;
		private final double translationalTolerance;
		private final int    consecutive;
		
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
		public PositionRobotTask(
				double maxTranslationalSpeed, double translationalTolerance,
				double maxAngularSpeed, double angularTolerance,
				int consecutive) {
			if (maxAngularSpeed <= 0 || maxTranslationalSpeed <= 0 || consecutive <= 1)
				throw new IllegalArgumentException();
			this.maxTranslationalSpeed = maxTranslationalSpeed;
			this.translationalTolerance = translationalTolerance;
			this.maxAngularSpeed = maxAngularSpeed;
			this.angularTolerance = angularTolerance;
			this.consecutive = consecutive;
		}
		
		@Override
		public void start() {
			time.reset();
		}
		
		@Override
		public boolean loop() {
			moveController.updateAndMove(maxAngularSpeed, maxTranslationalSpeed,
			                             time.getSecondsAndReset());
			boolean hit = moveController.isOnTarget(translationalTolerance, angularTolerance);
			if (hit) numConsecutive++;
			else numConsecutive = 0;
			return numConsecutive >= consecutive;
		}
		
		@SuppressLint("DefaultLocale")
		@Override
		public String toString() {
			return String.format("MoveTask{angTol: %.2f, posTol:%.2f}",
			                     angularTolerance,
			                     translationalTolerance);
		}
		
	}
	
	/**
	 * Creates move tasks: a composite task of changing target position, then moving there.
	 * Will only consider translation/rotation tolerances if an actual movement/rotation occured,
	 * respectively.
	 */
	//Google "OOP builder"
	public class MoveTaskBuilder {
		private XYR    toMove              = XYR.ZERO;
		private double maxTranslationSpeed = -1;
		private double maxAngularSpeed     = -1;
		
		private MoveTaskBuilder() { //only can build from self.
		}
		
		/**
		 * Adds turning this moveTask.
		 */
		public MoveTaskBuilder turn(double toTurn, double maxSpeed, AngleUnit unit) {
			if (maxAngularSpeed < 0) throw new IllegalArgumentException();
			toMove = toMove.addToAngle(toOurUnit(toTurn, unit));
			this.maxAngularSpeed = maxSpeed;
			return this;
		}
		
		/**
		 * Adds turning to this moveTask.
		 */
		public MoveTaskBuilder turn(double angle, double maxSpeed) {
			return turn(angle, maxSpeed, ourAngleUnit);
		}
		
		/**
		 * Adds movement to this move task.
		 */
		public MoveTaskBuilder move(XY location, double maxSpeed, DistanceUnit unit) {
			if (maxTranslationSpeed < 0) throw new IllegalArgumentException();
			//to account for previous rotations, rotate.
			toMove = toMove.addToXY(toOurUnit(location, unit).rotate(this.toMove.angle));
			this.maxTranslationSpeed = maxSpeed;
			return this;
		}
		
		/**
		 * adds movement to this move task.
		 */
		public MoveTaskBuilder move(XY toMove, double maxSpeed) {
			return move(toMove, maxSpeed, ourDistanceUnit);
		}
		
		/**
		 * Adds the being built move task to the {@link TaskProgram}
		 */
		public MecanumDriveBetter go() {
			return go(false);
		}
		
		/**
		 * Adds the being built move task to the {@link TaskProgram}
		 *
		 * @param fine if true, less tolerant on target position
		 */
		public MecanumDriveBetter go(boolean fine) {
			return then(build(fine));
		}
		
		private Task build(boolean fine) {
			if (maxTranslationSpeed == -1 && maxAngularSpeed == -1)
				throw new IllegalArgumentException();
			Magnitudes tolerance = fine ? parameters.toleranceFine : parameters.toleranceCoarse;
			double translationalTolerance = maxTranslationSpeed != -1 ?
			                                tolerance.translational : -1;
			double angularTolerance = maxAngularSpeed != -1 ?
			                          tolerance.angular : -1;
			if (maxTranslationSpeed == -1) maxTranslationSpeed =
					maxAngularSpeed * parameters.idleSpeedMult.translational;
			if (maxAngularSpeed == -1) maxAngularSpeed =
					maxTranslationSpeed * parameters.idleSpeedMult.angular;
			int consecutive = fine ?
			                  parameters.consecutiveFine : parameters.consecutiveCoarse;
			return new CompositeTask(
					new ChangeTargetPositionTask(toMove),
					new PositionRobotTask(maxTranslationSpeed, translationalTolerance,
					                      maxAngularSpeed, angularTolerance,
					                      consecutive));
		}
	}
	
	public static class Parameters implements Cloneable {
		public static final DistanceUnit distanceUnit = ourDistanceUnit;
		public static final AngleUnit    angleUnit    = ourAngleUnit;
		
		public  double     encoderTicksPerUnit        = ENCODER_TICKS_PER_INCH;
		public  Magnitudes maxAccelerations           = DEFAULT_MAX_ACCELERATIONS;
		public  Magnitudes toleranceCoarse            = new Magnitudes(6, Math.toRadians(15));
		public  Magnitudes toleranceFine              = new Magnitudes(1.5, Math.toRadians(1));
		public  Magnitudes idleSpeedMult              = new Magnitudes(0.3);
		public  int        consecutiveCoarse          = 2;
		public  int        consecutiveFine            = 5;
		public  boolean    adjustAtEnd                = false;
		private double     idleAngularSpeedMult       = 0.3;
		private double     idleTranslationalSpeedMult = 0.3;
		
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
