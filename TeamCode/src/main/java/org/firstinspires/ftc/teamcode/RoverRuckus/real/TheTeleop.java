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
import org.firstinspires.ftc.teamcode.RoverRuckus.util.timer.SimpleTimer;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.timer.UnifiedTimers;
import org.jetbrains.annotations.NotNull;

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
	private UnifiedTimers timers       = new UnifiedTimers();
	private SimpleTimer   sleepEndTime = timers.newTimer(), cycleTime = timers.newTimer();
	//Variables for driving
	private boolean  reverseDrive = false;
	//Buttons.
	private Button   gp1y         = new Button(() -> gamepad1.y); //toggle reverse
	private Button   gp1b         = new Button(() -> gamepad1.b); //to set target pos.
	private Button   gp2a         = new Button(() -> gamepad2.a); //to next stage
	private Button   gp2b         = new Button(() -> gamepad2.b); //to prev stage.
	//opening/close scoreDump
	private Button   gp2lbp       = new Button(() -> gamepad2.left_bumper);
	private Button   gp2rbp       = new Button(() -> gamepad2.right_bumper);
	//reset COLLECT encoder
	private Button   gp2lsb       = new Button(() -> gamepad2.left_stick_button);
	//reset SCORE encoder
	private Button   gp2rsb       = new Button(() -> gamepad2.right_stick_button);
	//reset HOOK encoder
	private Button   gp1dpadlr    =
			new Button(() -> gamepad1.dpad_left || gamepad1.dpad_right);
	//State
	private ArmState armState     = ArmState.COLLECT;
	
	@Override
	protected void initialize() throws InterruptedException {
		robot = new CurRobot(hardwareMap);
		robot.initIMU();
		robot.wheels.setMode(RUN_USING_ENCODER);
		robot.wheels.setZeroPowerBehavior(FLOAT);
		RampedMoveController rampedMoveController =
				new RampedMoveController(DEFAULT_MAX_ACCELERATIONS);
		SimpleTimer moveTimer = timers.newTimer();
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
		robot.parker.setPosition(PARKER_HOME);
		//cycle count
		int cycles = 0;
		cycleTime.reset();
		autoMoveController.setTargetPositionHere();
		while (opModeIsActive()) {
			timers.update();
			autoMoveController.updateLocation();
			doGamepad1();
			doGamepad2();
			//record time
			if (cycleTime.getSeconds() > 0.5) {
				cycleTime.reset();
				cycles = 0;
				
				telemetry.addData("Cycles per second:", cycles * 2);
				telemetry.update(); //here is when we update
			}
			cycles++;
		}
	}
	
	private void doGamepad1() {
		//now default slow?
		SpeedMode speedMode = gamepad1.left_bumper ? SpeedMode.NORMAL : SpeedMode.SLOW;
	/*-----------------*\
    |     GAMEPAD 1     |
	\* ----------------*/
		//auto move.
		if (gp1b.pressed()) autoMoveController.setTargetPositionHere();
		if (gamepad1.right_bumper) { //auto move
			autoMoveController.moveToTarget();
			telemetry.addData("DRIVE MODE:", "AUTO");
		} else { //manual move
			if (gp1y.pressed()) reverseDrive = !reverseDrive;
			double direction = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x);
			if (reverseDrive) direction += Math.PI;
			double moveSpeed =
					Math.pow(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1.7);
			manualMoveController.driveAt(
					XY.fromPolar(moveSpeed, direction).scale(speedMode.mult),
					-gamepad1.right_stick_x * speedMode.mult);
			telemetry.addData("DRIVE MODE:", reverseDrive ? "HOOK FRONT" : "ARM FRONT");
		}
		//hook
		hook.setPowerLimited(gamepad1.x ? 1 : gamepad1.a ? -1 : 0, gamepad1.dpad_down);
		if (gp1dpadlr.pressed()) hook.resetEncoder();
	}
	
	@NotNull
	private void doGamepad2() {
			/*----------------*\
		    |    GAMEPAD 2     |
			\*----------------*/
		boolean userAdvance = true;
		boolean autoAdvance = false;
		double triggerSum = gamepad2.right_bumper ? 1 :
		                    gamepad2.left_bumper ? -1 :
		                    gamepad2.right_trigger - gamepad2.left_trigger;
		if (Math.abs(robot.hook.getCurrentPosition()) > HOOK_NULLIFY) {
			//bring everything in.
			telemetry.addLine("HOOKING");
			scooper.setPower(0); //idle
			collectArm.setPowerLimited(COLLECT_ARM_IN_POWER);
			collectDoor.setPosition(COLLECT_DOOR_CLOSED); //close door
			scoreArm.setPowerLimited(SCORE_ARM_IN_POWER); //keep in
			scoreDump.setPosition(SCORE_DUMP_HOME); //close door.
		} else switch (armState) {
		case TO_COLLECT:
			scooper.setPower(0); //idle
			collectArm.setPowerLimited(1, null, COLLECT_ARM_INITIAL_EXTENSION);
			//extend to initial
			collectDoor.setPosition(COLLECT_DOOR_CLOSED); //close door
			scoreArm.setPowerLimited(SCORE_ARM_IN_POWER); //keep in
			scoreDump.setPosition(SCORE_DUMP_HOME); //close door.
			autoAdvance = collectArm.getLastState() == UPPER;
			break;
		case COLLECT:
			scooper.setPower(triggerSum);
			collectArm.setPowerLimited(-gamepad2.right_stick_y +
			                           (double) collectArm.getLastPosition() / COLLECT_ARM_MAX *
			                           COLLECT_ARM_MAX_IDLE_POWER,
			                           gamepad2.x);
			collectDoor.setPosition(COLLECT_DOOR_CLOSED);
			scoreArm.setPowerLimited(SCORE_ARM_IN_POWER);
			scoreDump.setPosition(SCORE_DUMP_HOME);
			break;
		case TO_TRANSFER:
			scooper.setPower(SCOOPER_IDLE_POWER);
			collectArm.setPowerLimited(COLLECT_ARM_IN_POWER); //bring IN!!
			//keep door closed;
			collectDoor.setPosition(COLLECT_DOOR_CLOSED);
			scoreArm.setPowerLimited(SCORE_ARM_IN_POWER); //keep in
			scoreDump.setPosition(SCORE_DUMP_HOME);
			collectDoor.setPosition(COLLECT_DOOR_OPEN);//CHANGED: moved outside
			autoAdvance =
					collectArm.getLastState() == LOWER && scoreArm.getLastState() == LOWER;
			if (autoAdvance) {
				sleepEndTime.reset();
				//sloppy yet working pseudo sleep.
			}
			userAdvance = false;
			break;
		case TRANSFER:
			if (sleepEndTime.getMillis() < TRANSFER_SLEEP_TIME) break;
			scooper.setPower(1 + triggerSum); //PUSH THINGS UP!
			collectArm.setPowerLimited(COLLECT_ARM_IN_POWER); //keep in
			collectDoor.setPosition(COLLECT_DOOR_OPEN); //OPEN DOOR
			scoreArm.setPowerLimited(SCORE_ARM_IN_POWER); //keep in
			scoreDump.setPosition(SCORE_DUMP_HOME);
			break;
		case TO_SCORE:
			scooper.setPower(0); //idle
			//keep out of the way
			collectArm.setPowerLimited(1, null, COLLECT_ARM_AWAY);
			collectDoor.setPosition(COLLECT_DOOR_OPEN); //keep open
			scoreArm.setPowerLimited(collectArm.getLastState() == UPPER ? 1 : 0.1,
			                         null,
			                         SCORE_ARM_INITIAL_EXTENSION);
			scoreDump.setPosition(SCORE_DUMP_HOME);
			autoAdvance = scoreArm.getLastState() == UPPER;
			break;
		case SCORE:
			scooper.setPower(0); //idle
			//keep out of the way
			collectArm.setPowerLimited((double) 1, null, COLLECT_ARM_AWAY);
			collectDoor.setPosition(COLLECT_DOOR_CLOSED);
			scoreArm.setPowerLimited(-gamepad2.right_stick_y * 1, gamepad1.x);
			if (gp2rbp.down()) { //or also is in position
				scoreDump.setPosition(SCORE_DUMP_DOWN);
			} else if (gp2lbp.pressed()) {
				scoreDump.setPosition(SCORE_DUMP_HOME);
			}
			userAdvance = true;
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
		
		telemetry.addData("Prev state", armState.prev());
		telemetry.addData(" ARM STATE", armState);
		telemetry.addData("Next state", armState.next());
	}
	
	private enum ArmState {
		TO_COLLECT,
		COLLECT,
		TO_TRANSFER,
		TRANSFER,
		TO_SCORE,
		SCORE;
		private static final ArmState[] values = ArmState.values();
		
		public ArmState next() {
			return values[(this.ordinal() + 1) % values.length];
		}
		
		public ArmState prev() {
			int ord = (this.ordinal() / 2 * 2 - 2 + values.length) % values.length;
			return values[ord];
		}
	}
	
	private enum SpeedMode {
		FAST(SPEED_MULT_FAST),
		NORMAL(SPEED_MULT_NORM),
		SLOW(SPEED_MULT_SLOW);
		public final double mult;
		
		SpeedMode(double mult) {
			this.mult = mult;
		}
	}
}
