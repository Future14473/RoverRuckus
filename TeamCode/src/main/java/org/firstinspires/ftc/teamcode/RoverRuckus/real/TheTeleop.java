package org.firstinspires.ftc.teamcode.RoverRuckus.real;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.RampedMoveController;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation.XY;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.LimitedMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.OurLinearOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.BaseRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.CurRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.*;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.Button.State.*;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.LimitedMotor.State.LOWER;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode.LimitedMotor.State.UPPER;

@TeleOp(group = "1real", name = "Teleop")
public class TheTeleop extends OurLinearOpMode {
	//Motors and servos, for readability and functionality
	private CurRobot             robot;
	private RampedMoveController rampedMoveController =
			new RampedMoveController(BaseRobot.RAMP_RATE);
	private LimitedMotor         collectArm, scoreArm, hook;
	private DcMotor scooper;
	private Servo   collectDoor;
	private Servo   scoreDump;
	private CRServo angler;
	
	//Variables for driving
	private boolean  gyroDrive      = false;
	private boolean  reverseDrive   = false;
	private double   rotationOffSet = 0;
	//Buttons.
	//toggle direction / reset gyro
	private Button   gp1y           = new Button(() -> gamepad1.y);
	//toggle gyro drive
	private Button   gp1b           = new Button(() -> gamepad1.b);
	private Button   gp2a           = new Button(() -> gamepad2.a); //to next stage
	private Button   gp2b           = new Button(() -> gamepad2.b); //to prev stage.
	//opening/close scoreDump
	private Button   gp2lbp         = new Button(() -> gamepad2.left_bumper);
	private Button   gp2rbp         = new Button(() -> gamepad2.right_bumper);
	//reset COLLECT encoder
	private Button   gp2lsb         = new Button(() -> gamepad2.left_stick_button);
	//reset SCORE encoder
	private Button   gp2rsb         = new Button(() -> gamepad2.right_stick_button);
	//reset HOOK encoder
	private Button   gp1dpadlr      = new Button(() -> gamepad1.dpad_left || gamepad1.dpad_right);
	//State
	private ArmState armState       = ArmState.COLLECT;
	//pseudo sleep
	private long     sleepEndTime;
	
	@Override
	protected void initialize() {
		robot = new CurRobot(hardwareMap);
		robot.initIMU();
		robot.wheels.setMode(RUN_USING_ENCODER);
		robot.wheels.setZeroPowerBehavior(FLOAT);
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
		while (opModeIsActive()) {
			//FOR GAMEPAD1, CHANGED BY GAMEPAD2 2 is fast, 1 is normal, 0 is
			// slow.
			SpeedMode speedMode = gamepad1.right_bumper ?
			                      SpeedMode.FAST : gamepad1.left_bumper ?
			                                       SpeedMode.SLOW : SpeedMode.NORMAL;
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
				collectArm.setPowerLimited(1, null, INITIAL_EXTENSION_COLLECT_ARM);
				//extend to initial
				collectDoor.setPosition(COLLECT_DOOR_CLOSED); //close door
				scoreArm.setPowerLimited(IDLE_POWER_IN); //keep in
				scoreDump.setPosition(SCORE_DUMP_HOME); //close door.
				autoAdvance = collectArm.getLastState() == UPPER;
				break;
			case COLLECT:
				scooper.setPower(triggerSum);
				collectArm.setPowerLimited(-gamepad2.right_stick_y + IDLE_POWER_COLLECT_ARM,
				                           gamepad2.x);
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
				collectArm.setPowerLimited(IDLE_POWER_IN); //keep in
				collectDoor.setPosition(COLLECT_DOOR_OPEN); //keep open
				scoreArm.setPowerLimited(1, null, INITIAL_EXTENSION_SCORE_ARM_);
				scoreDump.setPosition(SCORE_DUMP_HOME);
				speedMode = SpeedMode.SLOW; //GO SLOW
				autoAdvance = scoreArm.getLastState() == UPPER;
				break;
			case SCORE:
				scooper.setPower(0); //idle
				collectArm.setPowerLimited(IDLE_POWER_IN); //keep in
				collectDoor.setPosition(COLLECT_DOOR_CLOSED);
				scoreArm.setPowerLimited(-gamepad2.right_stick_y + IDLE_POWER_SCORE_ARM,
				                         gamepad1.x);
				if (gp2rbp.pressed()) {
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
			double direction = Math.atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y);
			
			if (gp1b.pressed()) gyroDrive = !gyroDrive;
			
			Button.State gp1yState = gp1y.getState();
			if (gyroDrive) {
				if (gp1yState == HELD) {
					speedMode = SpeedMode.STOP;
				} else if (gp1yState == RELEASED) {
					rotationOffSet = robot.getAngle() + direction;
				}
				direction += robot.getAngle() - rotationOffSet;
				telemetry.addData("DIRECTION", "GYRO");
			} else {
				if (gp1yState == PRESSED) reverseDrive = !reverseDrive;
				if (reverseDrive) direction += Math.PI;
				telemetry.addData("DIRECTION", reverseDrive ? "HOOK FRONT" : "ARM FRONT");
			}
			
			double moveSpeed = Math.pow(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y),
			                            1.7);
			MotorSetPower power = rampedMoveController.getPower(
					XY.fromPolar(moveSpeed, direction).scale(speedMode.mult),
					-gamepad1.right_stick_x * speedMode.mult, 1, 1);
			robot.wheels.setPower(power);
			//hook
			hook.setPowerLimited(gamepad1.x ? 1 : gamepad1.a ? -1 : 0, gamepad1.dpad_down);
			if (gp1dpadlr.pressed()) {
				hook.resetEncoder();
			}
			
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
