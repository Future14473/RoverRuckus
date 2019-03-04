package org.firstinspires.ftc.teamcode.ruckus.real;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.config.NavigationConstants;
import org.firstinspires.ftc.teamcode.lib.navigation.*;
import org.firstinspires.ftc.teamcode.lib.opmode.Button;
import org.firstinspires.ftc.teamcode.lib.opmode.LimitedMotor;
import org.firstinspires.ftc.teamcode.lib.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.lib.robot.CurRobot;
import org.firstinspires.ftc.teamcode.lib.timer.DeadlineTimer;
import org.firstinspires.ftc.teamcode.lib.timer.Timer;
import org.firstinspires.ftc.teamcode.lib.timer.UnifiedTimers;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.firstinspires.ftc.teamcode.config.TeleopAndAutoConstants.*;
import static org.firstinspires.ftc.teamcode.lib.opmode.LimitedMotor.State.LOWER;
import static org.firstinspires.ftc.teamcode.lib.opmode.LimitedMotor.State.UPPER;

@TeleOp(group = "1real", name = "Teleop")
public class TheTeleop extends OurLinearOpMode {
	//Motors and servos, for readability and functionality
	private CurRobot             robot;
	private ManualMoveController manualMoveController;
	private AutoMoveController   autoMoveController;
	private LimitedMotor         collectArm, scoreArm, hook;
	private DcMotor       scooper;
	private Servo         collectDoor;
	private Servo         scoreDump;
	private CRServo       angler;
	//timers
	private UnifiedTimers timers              = new UnifiedTimers();
	private Timer         transferTimer       = timers.newTimer();
	private DeadlineTimer updateLocationTimer = timers.newDeadlineTimer();
	private DeadlineTimer cycleTime           = timers.newDeadlineTimer();
	
	//Variables for driving
	private boolean reverseDrive        = true;
	//Buttons.
	private Button  toggleReverse       = new Button(() -> gamepad1.y); //toggle reverse
	private Button  setTargPos          = new Button(() -> gamepad1.b); //to set target pos.
	private Button  toNextStage         = new Button(() -> gamepad2.a); //to next stage
	private Button  toPrevStage         = new Button(() -> gamepad2.b); //to prev stage.
	//opening/close scoreDump
	private Button  unDump              = new Button(() -> gamepad2.left_bumper);
	private Button  doDump              = new Button(() -> gamepad2.right_bumper);
	//reset COLLECT encoder
	private Button  resetCollectEncoder = new Button(() -> gamepad2.left_stick_button);
	//reset SCORE encoder
	private Button  resetScoreEncoder   = new Button(() -> gamepad2.right_stick_button);
	//reset HOOK encoder
	private Button  resetHookEncoder    =
			new Button(() -> gamepad1.dpad_left || gamepad1.dpad_right);
	
	//State
	private ArmState  armState   = ArmState.COLLECT;
	private boolean   dumpDown   = false;
	private boolean   targPosSet = false;
	private boolean   onInterval = false;
	private SpeedMode speedMode  = SpeedMode.NORMAL;
	
	@Override
	protected void initialize() throws InterruptedException {
		robot = new CurRobot(hardwareMap);
		robot.initIMU();
		robot.wheels.setMode(RUN_USING_ENCODER);
		robot.wheels.setZeroPowerBehavior(FLOAT);
		RampedMoveController rampedMoveController =
				new RampedMoveController(NavigationConstants.DEFAULT_MAX_ACCELERATIONS);
		Timer moveTimer = timers.newTimer();
		manualMoveController = new ManualMoveController(robot, rampedMoveController, moveTimer);
		autoMoveController =
				new AutoMoveController(robot, new PositionTracker(), rampedMoveController,
				                       moveTimer);
		scoreArm = new LimitedMotor(robot.scoreArm, MOTOR_MIN, SCORE_ARM_MAX, true);
		collectArm = new LimitedMotor(robot.collectArm, MOTOR_MIN, COLLECT_ARM_MAX, true);
		hook = new LimitedMotor(robot.hook, MOTOR_MIN, HOOK_MAX, true);
		scooper = robot.scooper;
		collectDoor = robot.collectDoor;
		scoreDump = robot.scoreDump;
		angler = robot.angler;
		waitUntil(robot::imuIsGyroCalibrated, 2, SECONDS);
	}
	
	@Override
	protected void run() {
		//cycle count
		int cycles = 0;
		timers.update();
		cycleTime.resetDeadline();
		//robot.parker.setPosition(PARKER_HOME);
		autoMoveController.setTargetPositionHere();
		while (opModeIsActive()) {
			timers.update();
			if (cycleTime.deadlineHit()) {
				cycleTime.addToDeadlineSeconds(INTERVAL_TIME);
				onInterval = true;
			} else onInterval = false;
			if (updateLocationTimer.deadlineHit()) {
				autoMoveController.updateLocation();
				updateLocationTimer.addToDeadlineSeconds(UPDATE_LOCATION_TIME);
			}
			doGamepad2();
			doGamepad1();
			if (onInterval) {
				telemetry.addData("Cycles per second:", cycles / INTERVAL_TIME);
				telemetry.update();
				cycles = 0;
			}
			cycles++;
		}
	}
	
	private void doGamepad1() {
	/*-----------------*\
    |     GAMEPAD 1     |
	\* ----------------*/
		//auto move.
		if (setTargPos.pressed()) {
			autoMoveController.setTargetPositionHere();
			targPosSet = true;
		}
		if (gamepad1.right_bumper) { //auto move
			autoMoveController.moveToTarget();
			if (onInterval) telemetry.addData("DRIVE MODE:", "AUTO");
		} else { //manual move
			if (toggleReverse.pressed()) reverseDrive = !reverseDrive;
			double direction = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x);
			if (reverseDrive) direction += Math.PI;
			double moveSpeed =
					Math.pow(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1.7);
			manualMoveController.driveAt(
					XY.fromPolar(moveSpeed, direction).scale(speedMode.mult),
					-gamepad1.right_stick_x * speedMode.mult);
			if (onInterval)
				telemetry.addData("DRIVE MODE:", reverseDrive ? "HOOK FRONT" : "ARM FRONT");
		}
		//hook
		hook.setPowerLimited(gamepad1.x ? 1 : gamepad1.a ? -1 : 0, gamepad1.dpad_down);
		if (resetHookEncoder.pressed()) hook.resetEncoder();
	}
	
	private void doGamepad2() {
		speedMode = gamepad1.left_bumper ? SpeedMode.SLOW : SpeedMode.NORMAL;
			/*----------------*\
		    |    GAMEPAD 2     |
			\*----------------*/
		boolean autoAdvance = false;
		if (Math.abs(robot.hook.getCurrentPosition()) > HOOK_NULLIFY) {
			onHooking();
		} else switch (armState) {
		case TO_COLLECT:
			autoAdvance = onToCollect();
			break;
		case COLLECT:
			autoAdvance = onCollect();
			break;
		case TO_TRANSFER:
			autoAdvance = onToTransfer();
			break;
		case TRANSFER:
			autoAdvance = onTransfer();
			break;
		case TO_SCORE:
			autoAdvance = onToScore();
			break;
		case SCORE:
			autoAdvance = onScore();
			break;
		}
		if (toNextStage.pressed() || autoAdvance) { //advance to next stage
			armState = armState.next();
		} else if (toPrevStage.pressed()) { //go back.
			armState = armState.prev();
		}
		//angler always user controlled
		angler.setPower(-gamepad2.left_stick_y);
		//encoder reset
		if (resetCollectEncoder.pressed()) collectArm.resetEncoder();
		if (resetScoreEncoder.pressed()) scoreArm.resetEncoder();
		
		if (onInterval) {
			telemetry.addData("Prev state", armState.prev());
			telemetry.addData(" ARM STATE", armState);
			telemetry.addData("Next state", armState.next());
		}
	}
	
	private boolean onScore() {
		scooper.setPower(0); //idle
		//keep out of the way
		collectArm.setPowerLimited(1, null, COLLECT_ARM_AWAY);
		collectDoor.setPosition(COLLECT_DOOR_CLOSED);
		scoreArm.setPowerLimited(-gamepad2.right_stick_y * 1, gamepad1.x);
		boolean scoreDumpUp = scoreArm.getLastPosition() > DUMP_ALLOW_POSITION;
		if (unDump.down() || !scoreDumpUp) {
			scoreDump.setPosition(SCORE_DUMP_HOME);
			dumpDown = false;
		} else if (!dumpDown &&
		           (doDump.down() ||
		            targPosSet &&
		            scoreArm.getLastPosition() > AUTO_DUMP_MIN_POSITION &&
		            autoMoveController.isOnTarget(AUTO_DUMP_TOLERANCE))) {
			dumpDown = true;
			scoreDump.setPosition(SCORE_DUMP_DOWN);
			transferTimer.reset();
		}
		if (scoreDumpUp) speedMode = SpeedMode.SLOW;
		return dumpDown && transferTimer.getSeconds() > AUTO_DUMP_TRANSFER_TIME;
	}
	
	private boolean onToScore() {
		scooper.setPower(0); //idle
		//keep out of the way
		collectArm.setPowerLimited(1, null, COLLECT_ARM_AWAY);
		collectDoor.setPosition(COLLECT_DOOR_OPEN); //keep open
		scoreArm.setPowerLimited(1, null, SCORE_ARM_INITIAL_EXTENSION);
		scoreDump.setPosition(SCORE_DUMP_HOME);
		if (scoreArm.getLastPosition() > DUMP_ALLOW_POSITION) speedMode = SpeedMode.SLOW;
		dumpDown = false;
		return scoreArm.getLastState() == UPPER;
	}
	
	private boolean onTransfer() {
		if (transferTimer.getMillis() < TRANSFER_SLEEP_TIME) return false;
		double triggerSum = getTriggerSum();
		scooper.setPower(1 + 2 * triggerSum); //PUSH THINGS UP!
		//keep in, allow wiggle
		collectArm.setPowerLimited(
				COLLECT_ARM_IN_POWER - 1.5 * gamepad2.right_stick_x);
		collectDoor.setPosition(COLLECT_DOOR_OPEN); //OPEN DOOR
		//keep in, allow wiggle
		scoreArm.setPowerLimited(SCORE_ARM_IN_POWER * (1 + 2 * gamepad2.right_stick_y));
		scoreDump.setPosition(SCORE_DUMP_HOME + (unDump.down() ? 0 :
		                                         (Math.random() - 0.5) * SCORE_DUMP_WIGGLE));
		return false;
	}
	
	private boolean onToTransfer() {
		scooper.setPower(unDump.down() ? -SCOOPER_IDLE_POWER : SCOOPER_IDLE_POWER);
		collectArm.setPowerLimited(
				COLLECT_ARM_IN_POWER - 1.5 * gamepad2.right_stick_x); //bring IN!!
		//keep door closed;
		collectDoor.setPosition(COLLECT_DOOR_CLOSED);
		//keep in, allow wiggle
		scoreArm.setPowerLimited(SCORE_ARM_IN_POWER * (1 + 2 * gamepad2.right_stick_y));
		scoreDump.setPosition(SCORE_DUMP_HOME);
		collectDoor.setPosition(COLLECT_DOOR_CLOSED);
		//CHANGED:
		// moved outside
		boolean autoAdvance =
				collectArm.getLastState() == LOWER && scoreArm.getLastState() == LOWER;
		if (autoAdvance) transferTimer.reset();
		return autoAdvance;
	}
	
	private boolean onCollect() {
		double triggerSum = getTriggerSum();
		scooper.setPower(triggerSum);
		collectArm.setPowerLimited(-gamepad2.right_stick_y /*+
		                           (double) collectArm.getLastPosition() / COLLECT_ARM_MAX *
		                           COLLECT_ARM_MAX_IDLE_POWER*/,
		                           gamepad2.x);
		collectDoor.setPosition(COLLECT_DOOR_CLOSED);
		scoreArm.setPowerLimited(SCORE_ARM_IN_POWER);
		scoreDump.setPosition(SCORE_DUMP_HOME);
		return false;
	}
	
	private double getTriggerSum() {
		return gamepad2.right_bumper ? BUMPER_POWER :
		       gamepad2.left_bumper ? -BUMPER_POWER :
		       gamepad2.right_trigger - gamepad2.left_trigger;
	}
	
	private boolean onToCollect() {
		scooper.setPower(0); //idle
		collectArm.setPowerLimited(1, null, COLLECT_ARM_INITIAL_EXTENSION);
		//extend to initial
		collectDoor.setPosition(COLLECT_DOOR_CLOSED); //close door
		scoreArm.setPowerLimited(SCORE_ARM_IN_POWER); //keep in
		scoreDump.setPosition(SCORE_DUMP_HOME); //close door.
		return collectArm.getLastState() == UPPER;
	}
	
	private void onHooking() {
		//bring everything in.
		if (onInterval) telemetry.addLine("HOOKING");
		scooper.setPower(0); //idle
		collectArm.setPowerLimited(COLLECT_ARM_IN_POWER);
		collectDoor.setPosition(COLLECT_DOOR_CLOSED); //close door
		scoreArm.setPowerLimited(SCORE_ARM_IN_POWER); //keep in
		scoreDump.setPosition(SCORE_DUMP_HOME); //close door.
	}
	
	private enum ArmState {
		TO_COLLECT {
			@Override
			public ArmState next() {
				return COLLECT;
			}
			
			@Override
			public ArmState prev() {
				return TO_SCORE;
			}
		},
		COLLECT {
			@Override
			public ArmState next() {
				return TO_TRANSFER;
			}
			
			@Override
			public ArmState prev() {
				return TO_SCORE;
			}
		},
		TO_TRANSFER {
			@Override
			public ArmState next() {
				return TRANSFER;
			}
			
			@Override
			public ArmState prev() {
				return TO_COLLECT;
			}
		},
		TRANSFER {
			@Override
			public ArmState next() {
				return TO_SCORE;
			}
			
			@Override
			public ArmState prev() {
				return TO_COLLECT;
			}
		},
		TO_SCORE {
			@Override
			public ArmState next() {
				return SCORE;
			}
			
			@Override
			public ArmState prev() {
				return TO_TRANSFER;
			}
		},
		SCORE {
			@Override
			public ArmState next() {
				return TO_COLLECT;
			}
			
			@Override
			public ArmState prev() {
				return TO_TRANSFER;
			}
		};
		
		public abstract ArmState next();
		
		public abstract ArmState prev();
	}
	
	private enum SpeedMode {
		NORMAL(SPEED_MULT_NORM),
		SLOW(SPEED_MULT_SLOW);
		public final double mult;
		
		SpeedMode(double mult) {
			this.mult = mult;
		}
	}
}
