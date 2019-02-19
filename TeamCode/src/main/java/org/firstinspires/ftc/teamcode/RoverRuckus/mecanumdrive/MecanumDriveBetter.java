package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import android.support.annotation.CallSuper;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RoverRuckus.Constants;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.Task;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskAdapter;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasks.TaskProgram;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSet;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;

/**
 * A task program that controls the autonomous motion of wheels.
 * Uses motor encoders to dynamically correct motion, and gyroscope to
 * correct orientation and direction.
 * Orientation is always relative to the current robot position, AFTER any simultaneous or
 * previous turns.
 */
@SuppressWarnings("unused")
public class MecanumDriveBetter extends TaskProgram {
	private static final AngleUnit    ourAngleUnit    = AngleUnit.RADIANS;
	private static final DistanceUnit ourDistanceUnit = DistanceUnit.INCH;
	
	private final Parameters         parameters;
	private final MotorSet           wheels;
	private final AutoMoveController moveController;
	private final MoveTask           adjustmentTask;
	
	public MecanumDriveBetter(IRobot robot, Parameters parameters) {
		super("Mecanum Drive", true);
		this.parameters = parameters.clone();
		this.wheels = robot.getWheels();
		moveController = new AutoMoveController(robot, parameters.encoderTicksPerUnit,
		                                        parameters.maxAngularAcceleration,
		                                        parameters.maxTranslationalAcceleration);
		adjustmentTask = new MoveTask(XY.ZERO, 2, this.parameters.translationalToleranceFine,
		                              0, 2, this.parameters.angularToleranceFine,
		                              this.parameters.consecutiveFine) {
			@Override
			public String toString() {
				return "Adjustment";
			}
		};
		
		if (this.parameters.adjustAtEnd) super.addOnDoneTask(adjustmentTask);
		super.addOnDoneTask(wheels::stop);
	}
	
	private XY xyFromUnit(double x, double y, DistanceUnit distanceUnit) {
		return new XY(ourDistanceUnit.fromUnit(distanceUnit, x),
		              ourDistanceUnit.fromUnit(distanceUnit, y));
	}
	
	private double angleFromUnit(double angle, AngleUnit angleUnit) {
		return ourAngleUnit.getUnnormalized().fromUnit(angleUnit.getUnnormalized(), angle);
	}
	
	public MecanumDriveBetter turnAndMove(
			double angle, double maxAngularSpeed, AngleUnit angleUnit,
			double x, double y, double maxTranslationSpeed, DistanceUnit distanceUnit) {
		
		add(new MoveTask(xyFromUnit(x, y, distanceUnit),
		                 maxTranslationSpeed,
		                 parameters.translationalToleranceCoarse,
		                 angleFromUnit(angle, angleUnit),
		                 maxAngularSpeed,
		                 parameters.angularToleranceCoarse,
		                 parameters.consecutiveCoarse));
		return this;
	}
	
	public MecanumDriveBetter turnAndMoveFine(
			double angle, double maxAngularSpeed, AngleUnit angleUnit,
			double x, double y, double maxTranslationSpeed, DistanceUnit distanceUnit) {
		
		add(new MoveTask(xyFromUnit(x, y, distanceUnit),
		                 maxTranslationSpeed,
		                 parameters.translationalToleranceFine,
		                 angleFromUnit(angle, angleUnit),
		                 maxAngularSpeed,
		                 parameters.angularToleranceFine,
		                 parameters.consecutiveFine));
		return this;
	}
	
	public MecanumDriveBetter move(
			double x, double y, double maxTranslationSpeed, DistanceUnit distanceUnit) {
		add(new MoveTask(xyFromUnit(x, y, distanceUnit),
		                 maxTranslationSpeed,
		                 parameters.translationalToleranceCoarse,
		                 0,
		                 maxTranslationSpeed * parameters.idleAngularSpeedMult,
		                 -1,
		                 parameters.consecutiveCoarse));
		return this;
	}
	
	public MecanumDriveBetter moveFine(
			double x, double y, double maxTranslationSpeed, DistanceUnit distanceUnit) {
		add(new MoveTask(xyFromUnit(x, y, distanceUnit),
		                 maxTranslationSpeed,
		                 parameters.translationalToleranceFine,
		                 0,
		                 maxTranslationSpeed * parameters.idleAngularSpeedMult,
		                 -1,
		                 parameters.consecutiveFine));
		return this;
	}
	
	public MecanumDriveBetter turn(
			double angle, double maxAngularSpeed, AngleUnit angleUnit) {
		add(new MoveTask(XY.ZERO,
		                 maxAngularSpeed * parameters.idleTranslationalSpeedMult,
		                 -1,
		                 angleFromUnit(angle, angleUnit),
		                 maxAngularSpeed,
		                 parameters.angularToleranceCoarse,
		                 parameters.consecutiveCoarse));
		return this;
	}
	
	public MecanumDriveBetter turnFine(
			double angle, double maxAngularSpeed, AngleUnit angleUnit) {
		add(new MoveTask(XY.ZERO,
		                 maxAngularSpeed * parameters.idleTranslationalSpeedMult,
		                 -1,
		                 angleFromUnit(angle, angleUnit),
		                 maxAngularSpeed,
		                 parameters.angularToleranceFine,
		                 parameters.consecutiveFine));
		return this;
	}
	
	public MecanumDriveBetter adjust() {
		add(adjustmentTask);
		return this;
		
	}
	
	public MecanumDriveBetter phantomTurnAndMove(
			double angle, AngleUnit angleUnit, double x, double y, DistanceUnit distanceUnit) {
		add(new ChangeTargetLocationTask(xyFromUnit(x, y, distanceUnit),
		                                 angleFromUnit(angle, angleUnit)));
		return this;
	}
	
	public MecanumDriveBetter phantomMove(double x, double y, DistanceUnit distanceUnit) {
		add(new ChangeTargetLocationTask(xyFromUnit(x, y, distanceUnit)));
		return this;
	}
	
	public MecanumDriveBetter phantomTurn(double angle, AngleUnit angleUnit) {
		add(new ChangeTargetLocationTask(angleFromUnit(angle, angleUnit)));
		return this;
	}
	
	@Override
	public MecanumDriveBetter then(Task task) {
		add(task);
		return this;
	}
	
	@Override
	public void start() {
		updateLocation();
		super.start();
	}
	
	@Override
	public MecanumDriveBetter sleep(long millis) {
		super.sleep(millis);
		return this;
	}
	
	private void updateLocation() {
		moveController.updateLocation();
	}
	
	private class ChangeTargetLocationTask implements Task {
		private final XY     toTranslate;
		private final double toTurn;
		
		private ChangeTargetLocationTask(double toTurn) {
			this(XY.ZERO, toTurn);
		}
		
		private ChangeTargetLocationTask(XY toTranslate) {
			this(toTranslate, 0);
		}
		
		private ChangeTargetLocationTask(XY toTranslate, double toTurn) {
			this.toTranslate = toTranslate;
			this.toTurn = toTurn;
		}
		
		@Override
		public void run() {
			moveController.addToTargetAngle(toTurn);
			moveController.addToTargetLocation(toTranslate);
		}
		
		@Override
		public String toString() {
			return String.format("ChangeTargetLocationTask{toTranslate=%s, toTurn=%s}",
			                     toTranslate,
			                     toTurn);
		}
	}
	
	private class MoveTask extends TaskAdapter {
		private final double maxAngularSpeed;
		private final double maxTranslationalSpeed;
		private final double translationalTolerance;
		private final double angularTolerance;
		private final int    consecutive;
		private final double toTurn;
		private final XY     toTranslate;
		
		private double numConsecutive;
		
		/**
		 * Constructs a new MoveTask, that instructs the robot to move to a certain
		 * location and/or turn a certain amount. Has tolerance settings.
		 *
		 * @param toTranslate            The relative position to move, referenced AFTER a turn
		 *                               occurs.
		 * @param maxTranslationalSpeed  The maximum translational speed this robot will move at.
		 * @param translationalTolerance The maximum distance the robot will tolerate being away
		 *                               from the target position before stop. A negative
		 *                               tolerance indicates infinite tolerance.
		 * @param toTurn                 The number of radians to turn
		 * @param maxAngularSpeed        The maximum angular speed to move at.
		 * @param angularTolerance       The maximum angle the robot will tolerate being away from
		 *                               the target angle before stop. A negative tolerance
		 *                               indicates infinite tolerance.
		 * @param consecutive            The number of consecutive cycles the the above two
		 */
		public MoveTask(
				XY toTranslate, double maxTranslationalSpeed, double translationalTolerance,
				double toTurn, double maxAngularSpeed, double angularTolerance,
				int consecutive) {
			if (maxAngularSpeed <= 0 || maxTranslationalSpeed <= 0 || consecutive <= 1)
				throw new IllegalArgumentException();
			this.toTranslate = toTranslate;
			this.maxTranslationalSpeed = maxTranslationalSpeed;
			this.translationalTolerance = translationalTolerance;
			this.toTurn = toTurn;
			this.maxAngularSpeed = maxAngularSpeed;
			this.angularTolerance = angularTolerance;
			this.consecutive = consecutive;
		}
		
		@Override
		@CallSuper
		public void start() {
			//reset lastAngle and motorPos
			//update targetLocation and Angle
			moveController.addToTargetAngle(toTurn);
			moveController.addToTargetLocation(toTranslate);
			wheels.setMode(RUN_USING_ENCODER);
		}
		
		@Override
		public boolean loop() {
			moveController.updateAndMove(maxAngularSpeed, maxTranslationalSpeed);
			boolean hit = (translationalTolerance <= 0 ||
			               moveController.getLocationError().magnitude() <
			               translationalTolerance) &&
			              (angularTolerance <= 0 ||
			               Math.abs(moveController.getAngularError()) < angularTolerance);
			if (hit) numConsecutive++;
			else numConsecutive = 0;
			return numConsecutive >= consecutive;
		}
		
		@Override
		public String toString() {
			return String.format(
					"MoveTask{toTurn=%s, toTranslate=%s}",
					toTurn,
					toTranslate);
		}
	}
	
	public static class Parameters implements Cloneable {
		public static final DistanceUnit distanceUnit = ourDistanceUnit;
		public static final AngleUnit    angleUnit    = ourAngleUnit;
		
		public double  encoderTicksPerUnit          = Constants.ENCODER_TICKS_PER_INCH;
		public double  maxAngularAcceleration       = Constants.DEFAULT_MAX_ACCELERATION;
		public double  maxTranslationalAcceleration = Constants.DEFAULT_MAX_ACCELERATION;
		public double  angularToleranceCoarse       = 7;
		public double  angularToleranceFine         = 1;
		public double  translationalToleranceCoarse = 0.1;
		public double  translationalToleranceFine   = 0.03;
		public double  idleAngularSpeedMult         = 0.3;
		public double  idleTranslationalSpeedMult   = 0.3;
		public int     consecutiveCoarse            = 2;
		public int     consecutiveFine              = 5;
		public boolean adjustAtEnd                  = false;
		
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
