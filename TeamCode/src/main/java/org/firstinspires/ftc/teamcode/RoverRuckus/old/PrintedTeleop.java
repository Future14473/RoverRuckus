package org.firstinspires.ftc.teamcode.RoverRuckus.old;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.LimitedMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpModePrinted;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.Button.State.*;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.LimitedMotor.State.LOWER;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.LimitedMotor.State.UPPER;

@SuppressWarnings("Duplicates")
@TeleOp(group = "1real", name = "Old Teleop")
@Disabled
public class PrintedTeleop extends OurLinearOpModePrinted {
	//Encoder limits
	private static final int MOTOR_MIN = 100; //"at home" position for all
	// encoders
	private static final int ARM_MAX = 4450; //maximum arm extension (both
	// arms)
	private static final int HOOK_MAX = 26000; //maximum hook extension
	private static final int COLLECT_ARM_INITIAL_EXTENSION = 2000; //initial
	// extensions during auto extend
	private static final int SCORE_ARM_INITIAL_EXTENSION = ARM_MAX;
	private static final int HOOK_NULLIFY = 1500;
	//Servo positions
	private static final double COLLECT_DOOR_CLOSED = 0.71; //Collect door
	// positions
	private static final double COLLECT_DOOR_OPEN = 0.39;
	private static final double SCORE_DOOR_CLOSED = 0.9; //score door
	// positions;
	private static final double SCORE_DOOR_READY = 0.85;
	private static final double SCORE_DOOR_GOLD = 0.79;
	private static final double SCORE_DOOR_OPEN = 0.65;
	private static final double PARKER_POSITION_HOME = 0.6;
	//Mults
	private static final double SPEED_FAST_MULT = 100;
	private static final double SPEED_NORMAL_MULT = 1;
	private static final double SPEED_SLOW_MULT = 0.4;
	//Powers
	private static final double IDLE_IN_POWER = -0.6;
	private static final double IDLE_COLLECT_ARM_POWER = 0.05;
	private static final double IDLE_SCORE_ARM_POWER = 0.1;
	private static final double IDLE_SCOOPER_POWER = 0.6;
	//other constants
	private static final int TRANSFER_SLEEP_TIME = 200;
	//Variables for driving
	private boolean gyroDrive = false;
	private boolean reverseDrive = false;
	private double rotationOffSet = 0;
	//Motors and servos, for readability and functionality
	private LimitedMotor collectArm, scoreArm, hook;
	private DcMotor scooper;
	private Servo collectDoor;
	private Servo scoreDoor;
	private CRServo angler;
	//Buttons.
	
	private Button gp1y = new Button(() -> gamepad1.y); //toggle direction /
	// reset gyro
	
	private Button gp1b = new Button(() -> gamepad1.b); //toggle gyro drive
	
	private Button gp1dpadlr =
			new Button(() -> gamepad1.dpad_left || gamepad1.dpad_right);
	//reset HOOK encoder
	
	private Button gp2a = new Button(() -> gamepad2.a); //to next stage
	
	private Button gp2b = new Button(() -> gamepad2.b); //to prev stage.
	
	private Button gp2lbp = new Button(() -> gamepad2.left_bumper);//opening
	// scoreDoor
	
	private Button gp2rbp = new Button(() -> gamepad2.right_bumper);
	
	private Button gp2lsb = new Button(() -> gamepad2.left_stick_button);
	//reset COLLECT encoder
	
	private Button gp2rsb = new Button(() -> gamepad2.right_stick_button);
	//reset SCORE encoder
	//State
	private int scoreDoorState = 0;
	private ArmState armState = ArmState.COLLECT;
	//pseudo sleep
	private long sleepEndTime;
	
	@Override
	protected void initialize() {
		robot.initIMU();
		robot.wheels.setMode(RUN_USING_ENCODER);
		robot.wheels.setZeroPowerBehavior(FLOAT);
		scoreArm = new LimitedMotor(robot.scoreArm, MOTOR_MIN, ARM_MAX, true);
		collectArm = new LimitedMotor(robot.collectArm, MOTOR_MIN, ARM_MAX,
				true);
		hook = new LimitedMotor(robot.hook, MOTOR_MIN, HOOK_MAX, true);
		scooper = robot.scooper;
		collectDoor = robot.collectDoor;
		scoreDoor = robot.scoreDoor;
		angler = robot.angler;
		sleepEndTime = System.nanoTime();
	}
	
	@Override
	protected void run() {
		robot.parker.setPosition(PARKER_POSITION_HOME);
		while (opModeIsActive()) {
			//FOR GAMEPAD1, CHANGED BY GAMEPAD2 2 is fast, 1 is normal, 0 is
			// slow.
			int speedMode = gamepad1.right_bumper ? 2 : (gamepad1.left_bumper
					? 0 : 1);
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
				collectArm.setPowerLimited(IDLE_IN_POWER); //extend to initial
				collectDoor.setPosition(COLLECT_DOOR_CLOSED); //close door
				scoreArm.setPowerLimited(IDLE_IN_POWER); //keep in
				scoreDoor.setPosition(SCORE_DOOR_CLOSED); //close door.
			} else switch (armState) {
			case TO_COLLECT:
				scooper.setPower(0); //idle
				collectArm.setPowerLimited(1, null,
						COLLECT_ARM_INITIAL_EXTENSION); //extend to initial
				collectDoor.setPosition(COLLECT_DOOR_CLOSED); //close door
				scoreArm.setPowerLimited(IDLE_IN_POWER); //keep in
				scoreDoor.setPosition(SCORE_DOOR_CLOSED); //close door.
				autoAdvance = collectArm.getLastState() == UPPER;
				break;
			case COLLECT:
				scooper.setPower(triggerSum);
				collectArm.setPowerLimited(-gamepad2.right_stick_y + IDLE_COLLECT_ARM_POWER, gamepad2.x);
				collectDoor.setPosition(COLLECT_DOOR_CLOSED);
				scoreArm.setPowerLimited(IDLE_IN_POWER);
				scoreDoor.setPosition(SCORE_DOOR_CLOSED);
				break;
			case TO_TRANSFER:
				scooper.setPower(IDLE_SCOOPER_POWER); //keep balls from falling
				collectArm.setPowerLimited(-1); //bring IN!!
				collectDoor.setPosition(COLLECT_DOOR_CLOSED); //keep door
				// closed; no fa;; pit
				scoreArm.setPowerLimited(IDLE_IN_POWER); //keep in
				scoreDoor.setPosition(SCORE_DOOR_READY);
				autoAdvance =
						collectArm.getLastState() == LOWER && scoreArm.getLastState() == LOWER;
				if (autoAdvance) {
					collectDoor.setPosition(COLLECT_DOOR_OPEN); //OPEN DOOR
					// NOW...
					sleepEndTime =
							System.nanoTime() + MILLISECONDS.toNanos(TRANSFER_SLEEP_TIME); //pseudo sleep.
				}
				userAdvance = false;
				break;
			case TRANSFER:
				if (System.nanoTime() - sleepEndTime < 0) break;
				scooper.setPower(1 + triggerSum); //PUSH THINGS UP!
				collectArm.setPowerLimited(IDLE_IN_POWER); //keep in
				collectDoor.setPosition(COLLECT_DOOR_OPEN); //OPEN DOOR
				scoreArm.setPowerLimited(IDLE_IN_POWER); //keep in
				scoreDoor.setPosition(SCORE_DOOR_READY);
				break;
			case TO_SCORE:
				scooper.setPower(0); //idle
				collectArm.setPowerLimited(IDLE_IN_POWER); //keep in
				collectDoor.setPosition(COLLECT_DOOR_OPEN); //keep open
				scoreArm.setPowerLimited(1, null, SCORE_ARM_INITIAL_EXTENSION);
				scoreDoor.setPosition(SCORE_DOOR_READY);
				scoreDoorState = 0; //quick reset
				speedMode = 0; //GO SLOW
				autoAdvance = scoreArm.getLastState() == UPPER;
				break;
			case SCORE:
				double scoreDoorPos = scoreDoorState == 0 ? SCORE_DOOR_READY :
						scoreDoorState == 1 ? SCORE_DOOR_GOLD :
								SCORE_DOOR_OPEN;
				scooper.setPower(0); //idle
				collectArm.setPowerLimited(IDLE_IN_POWER); //keep in
				collectDoor.setPosition(COLLECT_DOOR_CLOSED);
				scoreArm.setPowerLimited(-gamepad2.right_stick_y + IDLE_SCORE_ARM_POWER, gamepad1.x);
				scoreDoor.setPosition(scoreDoorPos);
				speedMode = 0; // GO SLOW
				if (gp2rbp.pressed()) {
					scoreDoorState++;
					if (scoreDoorState >= 3) scoreDoorState = 2;
				}
				if (gp2lbp.pressed()) {
					scoreDoorState = 0;
				}
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
			telemetry.addData("Prev state: (press B)", armState.prev());
			telemetry.addData("            ARM STATE", armState);
			telemetry.addData("Next state  (press A)", armState.next());
			if (gp2lsb.pressed()) {
				collectArm.resetEncoder();
			}
			if (gp2rsb.pressed()) {
				scoreArm.resetEncoder();
			}
			/*-----------------*\
		    |     GAMEPAD 1     |
			\* ----------------*/
			double speedMult = (speedMode == 2) ? SPEED_FAST_MULT :
					(speedMode == 1 ? SPEED_NORMAL_MULT : SPEED_SLOW_MULT);
			double direction = Math.atan2(gamepad1.left_stick_x,
					-gamepad1.left_stick_y);
			
			if (gp1b.pressed()) gyroDrive = !gyroDrive;
			Button.State gp1yState = gp1y.getState();
			if (gyroDrive) {
				if (gp1yState == HELD) {
					speedMult = 0;
				} else if (gp1yState == RELEASED) {
					rotationOffSet = robot.getAngle() + direction;
				}
				direction += robot.getAngle() - rotationOffSet;
				telemetry.addData("DIRECTION", "GYRO");
			} else {
				if (gp1yState == PRESSED) reverseDrive = !reverseDrive;
				if (reverseDrive) direction += Math.PI;
				telemetry.addData("DIRECTION", reverseDrive ? "HOOK FRONT" :
						"ARM FRONT");
			}
			
			double turnRate = gamepad1.right_stick_x * speedMult;
			double speed = Math.pow(Math.hypot(gamepad1.left_stick_x,
					gamepad1.left_stick_y), 1.7) * speedMult;
			robot.smoothMoveAt(direction, speed, turnRate);
			//hook
			hook.setPowerLimited(gamepad1.x ? 1 : gamepad1.a ? -1 : 0,
					gamepad1.dpad_down);
			if (gp1dpadlr.pressed()) {
				hook.resetEncoder();
			}
			
			telemetry.update();
		}
	}
	
	@Override
	protected void cleanup() {
		collectDoor.setPosition(COLLECT_DOOR_CLOSED);
		scoreDoor.setPosition(SCORE_DOOR_CLOSED);
	}
	
	private enum ArmState {
		TO_COLLECT,
		COLLECT,
		TO_TRANSFER,
		TRANSFER,
		TO_SCORE,
		SCORE;
		private static final ArmState[] values = ArmState.values();
		
		ArmState next() {
			return values[(this.ordinal() + 1) % values.length];
		}
		
		ArmState prev() {
			int ord =
					(this.ordinal() / 2 * 2 - 2 + values.length) % values.length;
			return values[ord];
		}
	}
}
