package org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive;

import static org.firstinspires.ftc.teamcode.RoverRuckus.util.mecanumdrive.MotorPowerSet.*;

/**
 * More user friendly
 * Decorator/delegator of MoveTaskExecutor
 * One day we might add a interface abstraction
 */
public class MecanumDrive {
	private final MoveTaskExecutor taskExecutor;
	private final MotorSet motors;
	
	public MecanumDrive(MotorSet motors) {
		taskExecutor = new MoveTaskExecutor(motors);
		this.motors = motors;
	}
	
	public void cancel() {
		taskExecutor.cancel();
	}
	
	public boolean isDone() {
		return taskExecutor.isDone();
	}
	
	public void moveAt(double direction, double turnRate, double speed) {
		motors.setPowerTo(calcPowerSet(direction, turnRate, speed));
	}
	
	public void moveXY(double x, double y, double speed) {
		double direction = Math.toDegrees(Math.atan2(x, y));
		double distance = Math.hypot(x, y);
		move(direction, distance, speed);
	}
	
	public void move(double direction, double distance, double speed) {
		direction = Math.toRadians(direction);
		taskExecutor.add(new StraightMoveTask(calcPowerSet(direction, 0, speed), distance * MOVE_MULT / speed));
	}
	
	public void pauseUntilDone() throws InterruptedException {
		taskExecutor.pauseUntilDone();
	}
	
	public void stop() {
		taskExecutor.stop();
	}
	
	public void turn(double angle, double speed) throws InterruptedException {
		angle = Math.toRadians(angle);
		taskExecutor.add(new StraightMoveTask(calcPowerSet(0, speed * Math.signum(angle), 0),
				Math.abs(angle) * TURN_MULT / speed));
	}
}
