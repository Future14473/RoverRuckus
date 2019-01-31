package org.firstinspires.ftc.teamcode.RoverRuckus.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Button;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.LimitedMotor;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.OurLinearOpMode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.firstinspires.ftc.teamcode.RoverRuckus.util.Button.State.*;

@TeleOp(group = "test")
public class TeleOpTesting extends OurLinearOpMode {
	//Encoder constants.
	private static final int MOTOR_MIN = 100; //"at home" position for all encoders
	private static final int ARM_MAX = 4400;
	private static final int HOOK_MAX = 26000;
	private static final int COLLECT_ARM_INITIAL_EXTEND = 4000;
	private static final int SCORE_ARM_INITIAL_EXTEND = 4000;
	private static final double COLLECT_DOOR_CLOSED = 0; //TODO: FIND VALUES
	private static final double COLLECT_DOOR_OPEN = 1;
	private static final double SCORE_DOOR_CLOSED = 0.83;
	private static final double SCORE_DOOR_GOLD = 0.79;
	private static final double SCORE_DOOR_OPEN = 0.65;
	//driving
	private boolean gyroDrive = true;
	private boolean reverseDrive = false;
	private double rotationOffSet = 0;
	//Limited Motors;
	private LimitedMotor collectArm, scoreArm, hook;
	//buttons
	//   GP1
	private Button gp1y = new Button(() -> gamepad1.y); //toggle direction/ reset gyro
	private Button gp1b = new Button(() -> gamepad1.b); //toggle gyro drive
	//   GP2
	private Button gp2a = new Button(() -> gamepad2.a); //to next stage
	private Button gp2b = new Button(() -> gamepad2.b); //to prev stage.
	private Button gp2lsb = new Button(() -> gamepad2.left_stick_button); //reset LEFT encoder
	private Button gp2rsb = new Button(() -> gamepad2.right_stick_button); //reset RIGHT encoder
	private Button gp2dpadLR = new Button(() -> gamepad2.dpad_left || gamepad2.dpad_right); //reset HOOK encoder
	private Button gp2lb = new Button(() -> gamepad2.left_bumper);
	private Button gp2rb = new Button(() -> gamepad2.right_bumper);
	
	// other
	private int doorPos = 0;
	private ArmState armState = ArmState.COLLECT;
	
	@Override
	protected void initialize() throws InterruptedException {
		robot.initIMU();
		robot.wheels.setMode(RUN_USING_ENCODER);
		
		scoreArm = new LimitedMotor(robot.scoreArm, MOTOR_MIN, ARM_MAX, true);
		collectArm = new LimitedMotor(robot.collectArm, MOTOR_MIN, ARM_MAX, true);
		hook = new LimitedMotor(robot.hook, MOTOR_MIN, HOOK_MAX, true);
		
		
		waitUntil(robot.imu::isGyroCalibrated, 2500, MILLISECONDS);
	}
	
	@Override
	protected void run() {
		while (opModeIsActive()) {
			/*-----------------*\
		    |    GAMEPAD 1    |
			\* ----------------*/
			double speedMult = gamepad1.right_bumper ? 10 : (gamepad1.left_bumper ? 1.0 / 3 : 1);
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
			/*-----------------*\
			 |    GAMEPAD 2    |
			\* ----------------*/
			boolean userAdvance = false;
			boolean autoAdvance = false;
			switch (armState) {
				case TO_COLLECT:
					robot.scooper.setPower(0); //idle
					collectArm.setPowerLimited(1, null, COLLECT_ARM_INITIAL_EXTEND); //extend to initial
					robot.collectDoor.setPosition(COLLECT_DOOR_CLOSED); //close door
					scoreArm.setPowerLimited(-0.1); //keep in
					robot.scoreDoor.setPosition(SCORE_DOOR_CLOSED); //close door.
					userAdvance = true;
					autoAdvance = collectArm.getLastLimitState() == LimitedMotor.LimitState.UPPER;
					break;
				case COLLECT:
					robot.scooper.setPower(gamepad2.right_bumper ? 1 : gamepad2.left_bumper ? -0.5 : 0);
					collectArm.setPowerLimited(-gamepad2.right_stick_y, gamepad2.x);
					robot.collectDoor.setPosition(COLLECT_DOOR_CLOSED);
					scoreArm.setPowerLimited(-0.1);
					robot.scoreDoor.setPosition(SCORE_DOOR_CLOSED);
					userAdvance = true;
					break;
				case TO_TRANSFER:
					robot.scooper.setPower(0.5); //keep balls from falling
					collectArm.setPowerLimited(-1); //bring IN!!
					robot.collectDoor.setPosition(COLLECT_DOOR_CLOSED); //keep door closed
					scoreArm.setPowerLimited(-0.1); //keep in
					robot.scoreDoor.setPosition(SCORE_DOOR_CLOSED);
					autoAdvance =
							collectArm.getLastLimitState() == LimitedMotor.LimitState.LOWER && scoreArm.getLastLimitState() == LimitedMotor.LimitState.LOWER;
					userAdvance = false;
					break;
				case TRANSFER:
					robot.scooper.setPower(1); //PUSH THINGS UP!
					collectArm.setPowerLimited(-0.1); //keep in
					robot.collectDoor.setPosition(COLLECT_DOOR_OPEN); //OPEN DOOR
					scoreArm.setPowerLimited(-0.1); //keep in
					robot.scoreDoor.setPosition(SCORE_DOOR_CLOSED);
					userAdvance = true;
					break;
				case TO_SCORE:
					robot.scooper.setPower(0); //idle
					collectArm.setPowerLimited(-0.1); //keep in
					robot.collectDoor.setPosition(COLLECT_DOOR_OPEN); //keep open
					scoreArm.setPowerLimited(1, null, SCORE_ARM_INITIAL_EXTEND);
					robot.scoreDoor.setPosition(SCORE_DOOR_CLOSED);
					doorPos = 0;
					userAdvance = true;
					autoAdvance = scoreArm.getLastLimitState() == LimitedMotor.LimitState.UPPER;
					break;
				case SCORE:
					double scoreDoorPos = SCORE_DOOR_CLOSED;
					switch (doorPos) {
						case 0:
							scoreDoorPos = SCORE_DOOR_CLOSED;
							break;
						case 1:
							scoreDoorPos = SCORE_DOOR_GOLD;
							break;
						case 2:
							scoreDoorPos = SCORE_DOOR_OPEN;
							break;
					}
					robot.scooper.setPower(0); //idle
					collectArm.setPowerLimited(-0.1); //keep in
					robot.collectDoor.setPosition(COLLECT_DOOR_CLOSED);
					scoreArm.setPowerLimited(-gamepad2.right_stick_y, gamepad1.x);
					robot.scoreDoor.setPosition(scoreDoorPos);
					if (gp2rb.pressed()) {
						doorPos++;
						if (doorPos >= 3) doorPos = 2;
					}
					if (gp2lb.pressed()) {
						doorPos = 0;
					}
					userAdvance = true;
					break;
			}
			if (userAdvance && gp2a.pressed() || autoAdvance) {
				armState = armState.next();
			} else if (gp2b.pressed()) {
				armState = armState.prev();
			}
			//angler always active.
			robot.angler.setPower(-gamepad2.left_stick_y); //idle
			telemetry.addData("Prev state: (press B)", armState.prev());
			telemetry.addData("ARM STATE", armState.toString());
			telemetry.addData("Next state (press A)", armState.next());
			//collectArm : negate stick x value.
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
			telemetry.update();
		}
	}
	
	@Override
	protected void cleanup() {
	
	}
	
	private enum ArmState {
		TO_COLLECT,
		COLLECT,
		TO_TRANSFER,
		TRANSFER,
		TO_SCORE,
		SCORE;
		static final ArmState[] values = ArmState.values();
		
		ArmState next() {
			return values[(this.ordinal() + 1) % values.length];
		}
		
		ArmState prev() {
			int ord = (this.ordinal() / 2 * 2 - 2 + values.length) % values.length;
			return values[ord];
		}
	}
}
