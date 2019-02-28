package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.*;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.LimitedMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.CurRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.timer.DeadlineTimer;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.timer.Timer;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.timer.UnifiedTimers;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.*;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.LimitedMotor.State.LOWER;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.LimitedMotor.State.UPPER;

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
	private UnifiedTimers timers        = new UnifiedTimers();
	private Timer         transferTimer = timers.newTimer();
	private DeadlineTimer cycleTime     = timers.newDeadlineTimer();
	//Variables for driving
	private boolean       reverseDrive  = false;
	//Buttons.
	private Button        gp1y          = new Button(() -> gamepad1.y); //toggle reverse
	private Button        gp1b          = new Button(() -> gamepad1.b); //to set target pos.
	private Button        gp2a          = new Button(() -> gamepad2.a); //to next stage
	private Button        gp2b          = new Button(() -> gamepad2.b); //to prev stage.
	//opening/close scoreDump
	private Button        gp2lbp        = new Button(() -> gamepad2.left_bumper);
	private Button        gp2rbp        = new Button(() -> gamepad2.right_bumper);
	//reset COLLECT encoder
	private Button        gp2lsb        = new Button(() -> gamepad2.left_stick_button);
	//reset SCORE encoder
	private Button        gp2rsb        = new Button(() -> gamepad2.right_stick_button);
	//reset HOOK encoder
	private Button        gp1dpadlr     =
			new Button(() -> gamepad1.dpad_left || gamepad1.dpad_right);
	//State
	private ArmState      armState      = ArmState.COLLECT;
	private boolean       dumpDown      = false;
	private boolean       targPosSet    = false;
	private boolean       onInterval    = false;
	
	@Override
	protected void initialize() throws InterruptedException {
		robot = new CurRobot(hardwareMap);
		robot.initIMU();
		robot.wheels.setMode(RUN_USING_ENCODER);
		robot.wheels.setZeroPowerBehavior(FLOAT);
		RampedMoveController rampedMoveController =
				new RampedMoveController(DEFAULT_MAX_ACCELERATIONS);
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
		robot.parker.setPosition(PARKER_HOME);
		autoMoveController.setTargetPositionHere();
		while (opModeIsActive()) {
			timers.update();
			if (cycleTime.deadlineHit()) {
				cycleTime.addToDeadline(500000000);
				onInterval = true;
			} else onInterval = false;
			autoMoveController.updateLocation();
			doGamepad1();
			doGamepad2();
			if (onInterval) {
				telemetry.addData("Cycles per second:", cycles * 2);
				cycles = 0;
			}
			cycles++;
		}
	}
	
	private void doGamepad1() {
		//now default slow?
		SpeedMode speedMode = gamepad1.left_bumper ? SpeedMode.SLOW : SpeedMode.NORMAL;
	/*-----------------*\
    |     GAMEPAD 1     |
	\* ----------------*/
		//auto move.
		if (gp1b.pressed()) {
			autoMoveController.setTargetPositionHere();
			targPosSet = true;
		}
		if (gamepad1.right_bumper) { //auto move
			autoMoveController.moveToTarget();
			if (onInterval) telemetry.addData("DRIVE MODE:", "AUTO");
		} else { //manual move
			if (gp1y.pressed()) reverseDrive = !reverseDrive;
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
		if (gp1dpadlr.pressed()) hook.resetEncoder();
	}
	
	private void doGamepad2() {
			/*----------------*\
		    |    GAMEPAD 2     |
			\*----------------*/
		boolean userAdvance = true;
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
			userAdvance = false;
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
		if (userAdvance && gp2a.pressed() || autoAdvance) { //advance to next stage
			armState = armState.next();
		} else if (gp2b.pressed()) { //go back.
			armState = armState.prev();
		}
		//angler always user controlled
		angler.setPower(-gamepad2.left_stick_y);
		//encoder reset
		if (gp2lsb.pressed()) collectArm.resetEncoder();
		if (gp2rsb.pressed()) scoreArm.resetEncoder();
		
		if (onInterval) {
			telemetry.addData("Prev state", armState.prev());
			telemetry.addData(" ARM STATE", armState);
			telemetry.addData("Next state", armState.next());
		}
	}
	
	private boolean onScore() {
		scooper.setPower(0); //idle
		//keep out of the way
		collectArm.setPowerLimited((double) 1, null, COLLECT_ARM_AWAY);
		collectDoor.setPosition(COLLECT_DOOR_CLOSED);
		scoreArm.setPowerLimited(-gamepad2.right_stick_y * 1, gamepad1.x);
		if (gp2lbp.down() || scoreArm.getLastPosition() < DUMP_ALLOW_POSITION) {
			scoreDump.setPosition(SCORE_DUMP_HOME);
			dumpDown = false;
		} else if (gp2rbp.down() ||
		           targPosSet &&
		           scoreArm.getLastPosition() > AUTO_DUMP_MIN_POSITION &&
		           autoMoveController.isOnTarget(AUTO_DUMP_TOLERANCE)) {
			scoreDump.setPosition(SCORE_DUMP_DOWN);
			dumpDown = true;
			transferTimer.reset();
		}
		return dumpDown && transferTimer.getMillis() > AUTO_DUMP_TRANSFER_TIME;
	}
	
	private boolean onToScore() {
		boolean autoAdvance;
		scooper.setPower(0); //idle
		//keep out of the way
		collectArm.setPowerLimited(1, null, COLLECT_ARM_AWAY);
		collectDoor.setPosition(COLLECT_DOOR_OPEN); //keep open
		scoreArm.setPowerLimited(1,
		                         null,
		                         SCORE_ARM_INITIAL_EXTENSION);
		scoreDump.setPosition(SCORE_DUMP_HOME);
		autoAdvance = scoreArm.getLastState() == UPPER;
		dumpDown = false;
		return autoAdvance;
	}
	
	private boolean onTransfer() {
		if (transferTimer.getMillis() < TRANSFER_SLEEP_TIME) return false;
		double triggerSum = gamepad2.right_bumper ? 1 :
		                    gamepad2.left_bumper ? -1 :
		                    gamepad2.right_trigger - gamepad2.left_trigger;
		scooper.setPower(1 + triggerSum); //PUSH THINGS UP!
		//keep in, allow wiggle
		collectArm.setPowerLimited(COLLECT_ARM_IN_POWER + -gamepad2.right_stick_x);
		collectDoor.setPosition(COLLECT_DOOR_OPEN); //OPEN DOOR
		//keep in, allow wiggle
		scoreArm.setPowerLimited(SCORE_ARM_IN_POWER + -gamepad2.right_stick_y);
		scoreDump.setPosition(SCORE_DUMP_HOME);
		return false;
	}
	
	private boolean onToTransfer() {
		boolean autoAdvance;
		scooper.setPower(SCOOPER_IDLE_POWER);
		collectArm.setPowerLimited(COLLECT_ARM_IN_POWER); //bring IN!!
		//keep door closed;
		collectDoor.setPosition(COLLECT_DOOR_CLOSED);
		//keep in, allow wiggle
		scoreArm.setPowerLimited(SCORE_ARM_IN_POWER + -gamepad2.right_stick_y);
		scoreDump.setPosition(SCORE_DUMP_HOME);
		collectDoor.setPosition(collectArm.getLastPosition() < COLLECT_ARM_INITIAL_EXTENSION ?
		                        COLLECT_DOOR_OPEN : COLLECT_DOOR_CLOSED);
		//CHANGED:
		// moved outside
		autoAdvance =
				collectArm.getLastState() == LOWER && scoreArm.getLastState() == LOWER;
		if (autoAdvance) transferTimer.reset();
		return autoAdvance;
	}
	
	private boolean onCollect() {
		double triggerSum = gamepad2.right_bumper ? 1 :
		                    gamepad2.left_bumper ? -1 :
		                    gamepad2.right_trigger - gamepad2.left_trigger;
		scooper.setPower(triggerSum);
		collectArm.setPowerLimited(-gamepad2.right_stick_y +
		                           (double) collectArm.getLastPosition() / COLLECT_ARM_MAX *
		                           COLLECT_ARM_MAX_IDLE_POWER,
		                           gamepad2.x);
		collectDoor.setPosition(COLLECT_DOOR_CLOSED);
		scoreArm.setPowerLimited(SCORE_ARM_IN_POWER);
		scoreDump.setPosition(SCORE_DUMP_HOME);
		return false;
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
