package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.ManualMoveController;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.GamepadButton;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.LimitedMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.CurRobot;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.*;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.LimitedMotor.State.LOWER;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.LimitedMotor.State.UPPER;

@TeleOp(group = "1real", name = "Teleop")
public class TheTeleop extends OurLinearOpMode {
	//Motors and servos, for readability and functionality
	private CurRobot             robot;
	private ManualMoveController manualMoveController;
	private LimitedMotor         collectArm, scoreArm, hook;
	private DcMotor scooper;
	private Servo   collectDoor;
	private Servo   scoreDump;
	private CRServo angler;
	
	//Variables for driving
	private boolean       reverseDrive = false;
	//Buttons.
	private GamepadButton gp1y         = new GamepadButton(() -> gamepad1.y); //toggle reverse
	private GamepadButton gp2a         = new GamepadButton(() -> gamepad2.a); //to next stage
	private GamepadButton gp2b         = new GamepadButton(() -> gamepad2.b); //to prev stage.
	//opening/close scoreDump
	private GamepadButton gp2lbp       = new GamepadButton(() -> gamepad2.left_bumper);
	private GamepadButton gp2rbp       = new GamepadButton(() -> gamepad2.right_bumper);
	//reset COLLECT encoder
	private GamepadButton gp2lsb       = new GamepadButton(() -> gamepad2.left_stick_button);
	//reset SCORE encoder
	private GamepadButton gp2rsb       = new GamepadButton(() -> gamepad2.right_stick_button);
	//reset HOOK encoder
	private GamepadButton gp1dpadlr    =
			new GamepadButton(() -> gamepad1.dpad_left || gamepad1.dpad_right);
	//State
	private ArmState      armState     = ArmState.COLLECT;
	//pseudo sleep
	private long          sleepEndTime;
	
	@Override
	protected void initialize() {
		robot = new CurRobot(hardwareMap);
		robot.wheels.setMode(RUN_USING_ENCODER);
		robot.wheels.setZeroPowerBehavior(FLOAT);
		manualMoveController = new ManualMoveController(robot);
		scoreArm = new LimitedMotor(robot.scoreArm, MOTOR_MIN, SCORE_ARM_MAX, true);
		collectArm = new LimitedMotor(robot.collectArm, MOTOR_MIN, COLLECT_ARM_MAX, true);
		hook = new LimitedMotor(robot.hook, MOTOR_MIN, HOOK_MAX, true);
		scooper = robot.scooper;
		collectDoor = robot.collectDoor;
		scoreDump = robot.scoreDump;
		angler = robot.angler;
		sleepEndTime = System.nanoTime();
	}
	
	@Override
	protected void run() {
		robot.parker.setPosition(PARKER_HOME);
		//cycle count
		long countEndTime = System.nanoTime() + 500000000;
		int cycles = 0;
		double cyclesPerSec = 0;
		while (opModeIsActive()) {
			//FOR GAMEPAD1, CHANGED BY GAMEPAD2 2 is fast, 1 is normal, 0 is
			// slow.
			SpeedMode speedMode = gamepad1.right_bumper ? SpeedMode.FAST :
			                      gamepad1.left_bumper ? SpeedMode.SLOW : SpeedMode.NORMAL;
			/*----------------*\
		    |    GAMEPAD 2     |
			\*----------------*/
			boolean userAdvance = true;
			boolean autoAdvance = false;
			double triggerSum = gamepad2.right_trigger - gamepad2.left_trigger;
			if (gamepad2.right_bumper) triggerSum = 1;
			else if (gamepad2.left_bumper) triggerSum = -1;
			if (Math.abs(robot.hook.getCurrentPosition()) > HOOK_NULLIFY) {
				//close everything.
				telemetry.addLine("HOOKING!!!!");
				scooper.setPower(0); //idle
				collectArm.setPowerLimited(IDLE_POWER_IN); //extend to initial
				collectDoor.setPosition(COLLECT_DOOR_CLOSED); //close door
				scoreArm.setPowerLimited(IDLE_POWER_IN); //keep in
				scoreDump.setPosition(SCORE_DUMP_HOME); //close door.
			} else switch (armState) {
			case TO_COLLECT:
				scooper.setPower(0); //idle
				collectArm.setPowerLimited(1, null, COLLECT_ARM_INITIAL_EXTENSION);
				//extend to initial
				collectDoor.setPosition(COLLECT_DOOR_CLOSED); //close door
				scoreArm.setPowerLimited(IDLE_POWER_IN); //keep in
				scoreDump.setPosition(SCORE_DUMP_HOME); //close door.
				autoAdvance = collectArm.getLastState() == UPPER;
				break;
			case COLLECT:
				scooper.setPower(triggerSum);
				collectArm.setPowerLimited(-gamepad2.right_stick_y, gamepad2.x);
				collectDoor.setPosition(COLLECT_DOOR_CLOSED);
				scoreArm.setPowerLimited(IDLE_POWER_IN);
				scoreDump.setPosition(SCORE_DUMP_HOME);
				break;
			case TO_TRANSFER:
				scooper.setPower(IDLE_POWER_SCOOPER); //keep balls from falling
				collectArm.setPowerLimited(-1); //bring IN!!
				//keep door closed;
				collectDoor.setPosition(COLLECT_DOOR_CLOSED);
				scoreArm.setPowerLimited(IDLE_POWER_IN); //keep in
				scoreDump.setPosition(SCORE_DUMP_HOME);
				autoAdvance =
						collectArm.getLastState() == LOWER && scoreArm.getLastState() == LOWER;
				if (autoAdvance) {
					collectDoor.setPosition(COLLECT_DOOR_OPEN);
					// NOW...
					sleepEndTime = System.nanoTime() + MILLISECONDS.toNanos(TRANSFER_SLEEP_TIME);
					//pseudo sleep.
				}
				userAdvance = false;
				break;
			case TRANSFER:
				if (System.nanoTime() - sleepEndTime < 0) break;
				scooper.setPower(1 + triggerSum); //PUSH THINGS UP!
				collectArm.setPowerLimited(IDLE_POWER_IN); //keep in
				collectDoor.setPosition(COLLECT_DOOR_OPEN); //OPEN DOOR
				scoreArm.setPowerLimited(IDLE_POWER_IN); //keep in
				scoreDump.setPosition(SCORE_DUMP_HOME);
				break;
			case TO_SCORE:
				scooper.setPower(0); //idle
				//keep out of the way
				collectArm.setPowerLimited(1, null, COLLECT_ARM_AWAY);
				collectDoor.setPosition(COLLECT_DOOR_OPEN); //keep open
				//TODO: MAKE BETTER
				scoreArm.setPowerLimited(collectArm.getLastState() == UPPER ? 1 : 0.1, null,
				                         SCORE_ARM_INITIAL_EXTENSION);
				scoreDump.setPosition(SCORE_DUMP_HOME);
				speedMode = SpeedMode.SLOW; //GO SLOW
				autoAdvance = scoreArm.getLastState() == UPPER;
				break;
			case SCORE:
				scooper.setPower(0); //idle
				//keep out of the way
				collectArm.setPowerLimited(IDLE_POWER_OUT, null, COLLECT_ARM_AWAY);
				collectDoor.setPosition(COLLECT_DOOR_CLOSED);
				scoreArm.setPowerLimited(-gamepad2.right_stick_y, gamepad1.x);
				if (gp2rbp.down()) {
					scoreDump.setPosition(SCORE_DUMP_DOWN);
				} else if (gp2lbp.pressed()) {
					scoreDump.setPosition(SCORE_DUMP_HOME);
				}
				speedMode = SpeedMode.SLOW; // GO SLOW
				userAdvance = true;
				break;
			}
			if (userAdvance && gp2a.pressed() || autoAdvance) {
				armState = armState.next();
			} else if (gp2b.pressed()) {
				armState = armState.prev();
			}
			//angler always userControlled.
			angler.setPower(-gamepad2.left_stick_y);
			//encoder reset
			if (gp2lsb.pressed()) {
				collectArm.resetEncoder();
			}
			if (gp2rsb.pressed()) {
				scoreArm.resetEncoder();
			}
			telemetry.addData("Prev state: (press B)", armState.prev());
			telemetry.addData("            ARM STATE", armState);
			telemetry.addData("Next state  (press A)", armState.next());
			/*-----------------*\
		    |     GAMEPAD 1     |
			\* ----------------*/
			if (gp1y.pressed()) reverseDrive = !reverseDrive;
			telemetry.addData("DIRECTION", reverseDrive ? "HOOK FRONT" : "ARM FRONT");
			
			double direction = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x);
			if (reverseDrive) direction += Math.PI;
			double moveSpeed =
					Math.pow(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1.7);
			manualMoveController.driveAt(XY.fromPolar(moveSpeed, direction).scale(speedMode.mult),
			                             -gamepad1.right_stick_x * speedMode.mult);
			//hook
			hook.setPowerLimited(gamepad1.x ? 1 : gamepad1.a ? -1 : 0, gamepad1.dpad_down);
			if (gp1dpadlr.pressed()) {
				hook.resetEncoder();
			}
			if (System.nanoTime() > countEndTime) {
				cyclesPerSec = (cyclesPerSec + cycles * 2) / 2;
				countEndTime += 5e8;
				cycles = 0;
			}
			cycles++;
			telemetry.addData("Cycles per second:", cyclesPerSec);
			telemetry.update();
		}
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
		SLOW(SPEED_MULT_SLOW),
		STOP(0);
		public final double mult;
		
		SpeedMode(double mult) {
			this.mult = mult;
		}
	}
}
