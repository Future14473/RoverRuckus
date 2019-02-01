package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.LimitedMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.Button.State.*;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.LimitedMotor.State.LOWER;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.LimitedMotor.State.UPPER;

@TeleOp(group = "test")
public class TeleOpTesting extends OurLinearOpMode {
	//Encoder limits
	private static final int MOTOR_MIN = 100; //"at home" position for all encoders
	private static final int ARM_MAX = 4400; //maximum arm extension (both arms)
	private static final int HOOK_MAX = 26000; //maximum hook extension
	private static final int COLLECT_ARM_INITIAL_EXTENSION = 4000; //initial extensions during auto extend
	private static final int SCORE_ARM_INITIAL_EXTENSION = 4000;
	//Servo positions
	private static final double COLLECT_DOOR_CLOSED = 0; //TODO: FIND VALUES
	private static final double COLLECT_DOOR_OPEN = 1;
	private static final double SCORE_DOOR_CLOSED = 0.9; //score door positions;
	private static final double SCORE_DOOR_READY = 0.85;
	private static final double SCORE_DOOR_GOLD = 0.79;
	private static final double SCORE_DOOR_OPEN = 0.65;
	//Other constants
	private static final double SPEED_FAST_MULT = 100;
	private static final double SPEED_NORMAL_MULT = 1;
	private static final double SPEED_SLOW_MULT = 0.3;
	private static final double IDLE_POWER_IN = -0.5;
	private static final double IDLE_POWER_OUT = 0.2;
	//Variables for driving
	private boolean gyroDrive = true;
	private boolean reverseDrive = false;
	private double rotationOffSet = 0;
	//Motors and servos, for readability and functionality
	private LimitedMotor collectArm, scoreArm, hook;
	private DcMotor scooper;
	private Servo collectDoor;
	private Servo scoreDoor;
	private CRServo angler;
	//Buttons.
	private Button gp1y = new Button(() -> gamepad1.y); //toggle direction / reset gyro
	private Button gp1b = new Button(() -> gamepad1.b); //toggle gyro drive
	private Button gp2a = new Button(() -> gamepad2.a); //to next stage
	private Button gp2b = new Button(() -> gamepad2.b); //to prev stage.
	private Button gp2lbp = new Button(() -> gamepad2.left_bumper);//opening scoreDoor
	private Button gp2rbp = new Button(() -> gamepad2.right_bumper);
	private Button gp2lsb = new Button(() -> gamepad2.left_stick_button); //reset LEFT encoder
	private Button gp2rsb = new Button(() -> gamepad2.right_stick_button); //reset RIGHT encoder
	private Button gp2dpadLR = new Button(() -> gamepad2.dpad_left || gamepad2.dpad_right); //reset HOOK encoder
	//State
	private int scoreDoorState = 0;
	private ArmState armState = ArmState.COLLECT;
	
	@Override
	protected void initialize() throws InterruptedException {
		robot.initIMU();
		robot.wheels.setMode(RUN_USING_ENCODER);
		scoreArm = new LimitedMotor(robot.scoreArm, MOTOR_MIN, ARM_MAX, true);
		collectArm = new LimitedMotor(robot.collectArm, MOTOR_MIN, ARM_MAX, true);
		hook = new LimitedMotor(robot.hook, MOTOR_MIN, HOOK_MAX, true);
		scooper = robot.scooper;
		collectDoor = robot.collectDoor;
		scoreDoor = robot.scoreDoor;
		angler = robot.angler;
		waitUntil(robot.imu::isGyroCalibrated, 2500, MILLISECONDS);
	}
	
	@Override
	protected void run() {
		while (opModeIsActive()) {//FOR GAMEPAD1, CHANGED BY GAMEPAD2 2 is fast, 1 is normal, 0 is slow.
			int speedMode = gamepad1.right_bumper ? 2 : (gamepad1.left_bumper ? 0 : 1);
			/*----------------*\
		    |    GAMEPAD 2     |
			\*----------------*/
			boolean userAdvance = true;
			boolean autoAdvance = false;
			double triggerSum = gamepad2.right_trigger - gamepad2.left_trigger;
			if (gamepad2.right_bumper) triggerSum = 1;
			else if (gamepad2.left_bumper) triggerSum = -1;
			switch (armState) {
			case TO_COLLECT: scooper.setPower(0); //idle
				collectArm.setPowerLimited(1, null, COLLECT_ARM_INITIAL_EXTENSION); //extend to initial
				collectDoor.setPosition(COLLECT_DOOR_CLOSED); //close door
				scoreArm.setPowerLimited(IDLE_POWER_IN); //keep in
				scoreDoor.setPosition(SCORE_DOOR_CLOSED); //close door.
				autoAdvance = collectArm.getLastState() == UPPER;
				break;
			case COLLECT: scooper.setPower(triggerSum);
				collectArm.setPowerLimited(-gamepad2.right_stick_y + IDLE_POWER_OUT, gamepad2.x);
				collectDoor.setPosition(COLLECT_DOOR_CLOSED);
				scoreArm.setPowerLimited(IDLE_POWER_IN);
				scoreDoor.setPosition(SCORE_DOOR_CLOSED);
				break;
			case TO_TRANSFER: scooper.setPower(0.5); //keep balls from falling
				collectArm.setPowerLimited(-1); //bring IN!!
				collectDoor.setPosition(COLLECT_DOOR_CLOSED); //keep door closed; no fa;; pit
				scoreArm.setPowerLimited(IDLE_POWER_IN); //keep in
				scoreDoor.setPosition(SCORE_DOOR_READY);
				autoAdvance = collectArm.getLastState() == LOWER && scoreArm.getLastState() == LOWER;
				userAdvance = false;
				break;
			case TRANSFER: scooper.setPower(1 + triggerSum); //PUSH THINGS UP!
				collectArm.setPowerLimited(IDLE_POWER_IN); //keep in
				collectDoor.setPosition(COLLECT_DOOR_OPEN); //OPEN DOOR
				scoreArm.setPowerLimited(IDLE_POWER_IN); //keep in
				scoreDoor.setPosition(SCORE_DOOR_READY);
				break;
			case TO_SCORE: //TODO
				scooper.setPower(0); //idle
				collectArm.setPowerLimited(IDLE_POWER_IN); //keep in
				collectDoor.setPosition(COLLECT_DOOR_OPEN); //keep open
				scoreArm.setPowerLimited(1, null, SCORE_ARM_INITIAL_EXTENSION);
				scoreDoor.setPosition(SCORE_DOOR_READY);
				scoreDoorState = 0; //quick reset
				speedMode = 0; //GO SLOW
				autoAdvance = scoreArm.getLastState() == UPPER;
				break;
			case SCORE:
				double scoreDoorPos = scoreDoorState == 0 ? SCORE_DOOR_READY : scoreDoorState == 1 ? SCORE_DOOR_GOLD :
						SCORE_DOOR_OPEN;
				scooper.setPower(0); //idle
				collectArm.setPowerLimited(IDLE_POWER_IN); //keep in
				collectDoor.setPosition(COLLECT_DOOR_CLOSED);
				scoreArm.setPowerLimited(-gamepad2.right_stick_y + IDLE_POWER_OUT, gamepad1.x);
				scoreDoor.setPosition(scoreDoorPos);
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
			//hook
			hook.setPowerLimited(gamepad2.dpad_up ? 1 : gamepad2.dpad_down ? -1 : 0, gamepad2.x);
			if (gp2dpadLR.pressed()) {
				hook.resetEncoder();
			}
			/*-----------------*\
		    |     GAMEPAD 1     |
			\* ----------------*/
			double speedMult = (speedMode == 2) ? SPEED_FAST_MULT : (speedMode == 1 ? SPEED_NORMAL_MULT :
					SPEED_SLOW_MULT);
			double direction = Math.atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y);
			
			if (gp1b.pressed()) gyroDrive = !gyroDrive;
			Button.State gp1yState = gp1y.getState();
			if (gyroDrive) {
				if (gp1yState == DOWN) {
					speedMult = 0;
				} else if (gp1yState == RELEASED) {
					rotationOffSet = robot.getDirection() + direction;
				}
				direction += robot.getDirection() - rotationOffSet;
				telemetry.addData("DIRECTION", "GYRO");
			} else {
				if (gp1yState == PRESSED) reverseDrive = !reverseDrive;
				if (reverseDrive) direction += Math.PI;
				telemetry.addData("DIRECTION", reverseDrive ? "HOOK FRONT" : "ARM FRONT");
			}
			
			double turnRate = gamepad1.right_stick_x * speedMult;
			double speed = Math.pow(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1.7) * speedMult;
			robot.moveAt(direction, speed, turnRate);
			
			telemetry.update();
		}
	}
	
	@Override
	protected void cleanup() {
		DcMotorEx a;
		a.
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
			int ord = (this.ordinal() / 2 * 2 - 2 + values.length) % values.length;
			return values[ord];
		}
	}
}
