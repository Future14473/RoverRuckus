package org.firstinspires.ftc.teamcode.RoverRuckus.mecanumdrive;

import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.TaskAdapter;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.IRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSet;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPosition;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.robot.MotorSetPower;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

/**
 * A {@link org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem.Task} that
 * moves the robot's wheels uniformly.
 * For moving or turning.
 *
 * @deprecated the new implementation(s) use gyroscope to align the robot
 * better.
 */
@Deprecated
class OldStraightMoveTask extends TaskAdapter {
	public final static double TURN_MULT = 1205; //change to tweak "rotate x
	// deg" precisely.   Degrees wheel turn per radian robot turn
	public final static double MOVE_MULT = 4450; //change to tweak "move x
	// meters" precisely. Degrees wheel turn per yard robot move
	protected final IRobot robot;
	// per
	// unit.
	//TODO: Make above protected
	private final double speed;
	private final double[] progress = new double[4];
	private MotorSet motors;
	private MotorSetPower targPower;
	private MotorSetPosition targPos;
	private MotorSetPosition curPos;
	
	OldStraightMoveTask(IRobot robot, MotorSetPower targPower, double mult,
	                    double speed) {
		this.targPower = targPower;
		this.speed = speed;
		this.robot = robot;
		this.targPos = new MotorSetPosition(targPower, mult);
	}
	
	private int getMaxOff() {
		int maxOff = 0;
		for (int i = 0; i < 4; i++) {
			maxOff = Math.max(maxOff,
					Math.abs(curPos.get(i) - targPos.get(i)));
		}
		return maxOff;
	}
	
	private double getAvgProgress() {
		double avgProgress = 0;
		for (int i = 0; i < 4; i++) {
			progress[i] = ((double) curPos.get(i)) / targPos.get(i);
			if (Double.isNaN(progress[i])) progress[i] = 1;
			avgProgress += progress[i];
		}
		return Range.clip(avgProgress / 4, 0, 1);
	}
	
	//Reset position, set target position.
	@Override
	public void start() {
		this.motors = this.robot.getWheels();
		motors.setMode(STOP_AND_RESET_ENCODER);
		motors.setMode(RUN_TO_POSITION);
		motors.setTargetPosition(targPos);
	}
	
	//read motor positions and adjust them as necessary if they go off track
	// relative to everyone else.
	@Override
	public boolean loop() {
		curPos = this.motors.getCurrentPosition();
		setPower(getAvgProgress());
		return getMaxOff() < motors.getTargetPositionTolerance() * 2;
	}
	
	protected void setPower(double avgProgress) {
		double[] power = new double[4];
		for (int i = 0; i < 4; i++) {
			power[i] =
					(targPower.getPower(i) * (1 - 3 * (progress[i] - avgProgress))) * speed;
		}
		//RobotLog.v("SET POWER: %s", actualPower.toString());
		this.motors.setPower(power);
	}
	
}
