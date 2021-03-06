//package org.firstinspires.ftc.teamcode.RoverRuckus.old;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//
//import java.util.Queue;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
///**
// * Separate utility class to handle omnidirectional motion on mecanum wheels.
// */
//@SuppressWarnings("ALL")
//public class DriveHandler {
//	private static final MotorPowerSet   ZERO      =
//			new MotorPowerSet(0, 0, 0, 0);
//	static               double          MOVE_MULT = 4450;
//	//change to tweak "move x meters"
//	// precisely. Degrees wheel turn per unit.
//	static               double          TURN_MULT = 1205;
//	//change to tweak "turn x deg" precisely
//	// .   Degrees wheel turn per
//	private final        Object          moveLock  = new Object();
//	private final        Object          doneLock  = new Object();
//	//we have a separate thread handling moveTasks. This is so the robot can
//	// still do other stuff
//	//while this is happening at the same time.
//	private              MoveThread      moveThread;
//	private              Queue<MoveTask> moveTasks;
//	//the currentPos moveTasks to do;
//	//NOW THE FUN STUFF, FOR AUTONOMOUS MOTION.
//	//the motors
//	//[ FL, FR, BL, BR ]
//	private              DcMotor[]       motors;
//	private              LinearOpMode    mode;
//	private              boolean         running   = true;
//
//	/**
//	 * construct by motors
//	 */
//	DriveHandler(
//			DcMotor leftFront, DcMotor rightFront, DcMotor backLeft,
//			DcMotor backRight) {
//		motors = new DcMotor[]{leftFront, rightFront, backLeft, backRight};
//		moveThread = null;
//		moveTasks = new ConcurrentLinkedQueue<>();
//		for (int i = 0; i < 4; i++) {
//			motors[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//		}
//		//startMoveThread();
//	}
//
//	/**
//	 * construct by Ben Bielin Code
//	 */
//	DriveHandler(OldRobot r) {
//		this(r.leftFront, r.rightFront, r.leftBack, r.rightBack);
//	}
//
//// --Commented out by Inspection START (2/16/2019 9:16 PM):
////	void setModeEncoder() {
////		for (int i = 0; i < 4; i++) {
////			motors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
////		}
////	}
//// --Commented out by Inspection STOP (2/16/2019 9:16 PM)
//
//	/**
//	 * Stops the MoveThread. For use in non-linear OpMode.
//	 * If LinearOpMode is attached, the thread will close when opModeIsActive
//	 * () returns false.
//	 */
//	public void stop() {
//		running = false; //exit loop in thread if running
//		moveThread.interrupt(); //interrupt to exit if waiting
//	}
//
//	/**
//	 * starts the MoveTasks handling thread. A new thread might be created.
//	 */
//	private void startMoveThread() {
//		if (!threadRunning()) {
//			moveThread = new MoveThread();
//			moveThread.start();
//		}
//	}
//
//	private boolean isRunning() {
//		return mode != null ? !mode.isStarted() || !mode.isStopRequested() :
//		       running;
//	}
//
//	/**
//	 * returns if there are any move tasks currently
//	 */
//	public boolean isDone() {
//		return !moveTasks.isEmpty();
//	}
//
//	/**
//	 * adds a MoveTask to move in a straight line a specified direction and
//	 * distance.
//	 */
//	public void move(double direction, double distance, double speed)
//			throws InterruptedException { // maybe has some
//		// problems here
//		direction = Math.toRadians(direction);
//		addTask(new MoveTask(MotorPowerSet.calcPowerSet(direction, 0, speed),
//		                     distance * MOVE_MULT / speed));
//	}
//
//	/**
//	 * adds a MoveTasks that moves the robot in a straight line to a
//	 * displacement specified by x and y.
//	 */
//	public void moveXY(double x, double y, double speed)
//			throws InterruptedException {
//		double direction = Math.toDegrees(Math.atan2(x, y));
//		double distance = Math.hypot(x, y);
//		move(direction, distance, speed);
//	}
//
//	/**
//	 * ads a move task to turn in place a specified number of degrees,
//	 * positive or negative.
//	 */
//	public void turn(double degrees, double speed) throws InterruptedException {
//		degrees = Math.toRadians(degrees);
//		addTask(new MoveTask(
//				MotorPowerSet.calcPowerSet(0, speed * Math.signum(degrees), 0),
//				Math.abs(degrees) * TURN_MULT / speed));
//	}
//
//	private boolean threadRunning() {
//		return moveThread != null && moveThread.isAlive();
//	}
//
//	private void addTask(MoveTask task) throws InterruptedException {
//		if (!isRunning()) {
//			throw new InterruptedException("OpMode stopped running");
//		}
//		startMoveThread();
//		moveTasks.add(task);
//		synchronized (moveLock) {
//			moveLock.notifyAll(); //notify thread to start running again.
//		}
//	}
//
//	/**
//	 * Cancels all tasks, notifies waiting threads, and stops robot.
//	 */
//	public void cancelTasks() {
//		synchronized (moveLock) {
//			moveTasks.clear();
//		}
//		synchronized (doneLock) {
//			//no tasks left, continue.
//			//OR no longer running, notify waiting.
//			doneLock.notifyAll();
//		}
//		thenStopRobot();
//	}
//
//	/**
//	 * set motors to given powerSet.
//	 */
//	private void setPower(MotorPowerSet p) {
//		MotorPowerSet s = p.scaled();
//		for (int i = 0; i < 4; i++) { motors[i].setPower(s.power[i]); }
//	}
//
//	/**
//	 * Tells the robot to move in a specified direction and turnRate.
//	 * Can handle motor power levels greater than 1 -- it will scale them down.
//	 * If you're not turning and moving at the same time, and speed <=1, that
//	 * wont be a problem
//	 */
//	public void moveAt(double direction, double turnRate, double speed) {
//		setPower(MotorPowerSet.calcPowerSet(direction, turnRate, speed));
//	}
//
//	/**
//	 * Stops robot, i.e., set motor power levels to zero.
//	 */
//	private void thenStopRobot() {
//		setPower(ZERO);
//	}
//
//	/**
//	 * Waits until either there are no tasks left or not running.
//	 */
//	public void waitForDone() throws InterruptedException {
//		synchronized (doneLock) {
//			while (isDone() && isRunning()) {
//				if (mode != null) mode.idle();
//				doneLock.wait();
//			}
//		}
//	}
//
//	/**
//	 * Tracks if OpMode is running.
//	 */
//	public void addLinearOpMode(LinearOpMode mode) {
//		this.mode = mode;
//	}
//
//	private class MoveTask { //NOT STATIC, to access DC motors
//		private MotorPowerSet targetPower, actualPower;
//		private double   multiplier;
//		private double[] progress = new double[4];
//
//		MoveTask(MotorPowerSet targetPower, double multiplier) {
//			this.targetPower = targetPower;
//			this.multiplier = multiplier;
//		}
//
//		void start() {
//			for (int i = 0; i < 4; i++) {
//				motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//				motors[i].setMode(DcMotor.RunMode.RUN_TO_POSITION);
//				motors[i].setTargetPosition(
//						(int) (multiplier * targetPower.power[i]));
//			}
//			actualPower = new MotorPowerSet();
//			setPower(targetPower);
//		}
//
//		//read motor positions and thenAdjust them as necessary if they go off
//		// track.
//		//supposed to make all the motors turn in unison.
//		boolean process() {
//			if (actualPower == null) {
//				start();
//			}
//			double avgProgress = 0;
//			int maxOff = 0;
//			for (int i = 0; i < 4; i++) {
//				progress[i] = (double) motors[i].getCurrentPosition() /
//				              motors[i].getTargetPosition();
//				maxOff = Math.max(maxOff, Math.abs(
//						motors[i].getCurrentPosition() -
//						motors[i].getTargetPosition()));
//				if (Double.isNaN(progress[i])) progress[i] = 1;
//				avgProgress += progress[i];
//			}
//			avgProgress /= 4;
//			//thenAdjust power as necessary..
//			for (int i = 0; i < 4; i++) {
//				actualPower.power[i] = targetPower.power[i] *
//				                       (1 - 3 * (progress[i] - avgProgress));
//			}
//			setPower(actualPower);
//			return maxOff < 100;
//		}
//
//	}
//
//	private class MoveThread extends Thread {
//
//		MoveThread() {
//			this.setName("MoveThread");
//			this.setPriority(Thread.currentThread().getPriority());
//		}
//
//		@Override
//		public void run() {
//			//if interrupted, skips to here.
//			//next loop cycle will exit if not running.
//			while (isRunning()) try {
//				synchronized (moveLock) {
//					if (!moveTasks.isEmpty()) {
//						MoveTask curTask = moveTasks.element();
//						if (curTask.process()) {
//							moveTasks.remove();
//							thenStopRobot();
//						}
//					}
//					if (moveTasks.isEmpty()) {
//						synchronized (doneLock) {
//							//no tasks left, can continue.
//							doneLock.notifyAll();
//						}
//						thenStopRobot();
//					}
//					while (moveTasks.isEmpty()) {
//						moveLock.wait(1000);
//						//no elegant solution -- if OpMode stops while this
//						// thread is waiting, it will wait forever.
//						//However, want it to terminate.
//						if (!isRunning()) break;
//					}
//				}
//			} catch (NullPointerException e) {
//				e.printStackTrace();
//			} catch (InterruptedException ignored) {} //jump to next cycle.
//			cancelTasks();
//		}
//	}
//
//	/**
//	 * a set of power levels for all 4 motors;
//	 * just a container around double[]
//	 */
//	public static class MotorPowerSet {
//		double[] power;
//
//		MotorPowerSet(
//				double leftFront, double rightFront, double backLeft,
//				double backRight) {
//			this.power = new double[]{
//					leftFront, rightFront, backLeft, backRight
//			};
//		}
//
//		MotorPowerSet() {
//			this.power = new double[4];
//		}
//
//		MotorPowerSet scaled() {
//			MotorPowerSet set = new MotorPowerSet();
//			double max = 1;
//			for (int i = 0; i < 4; i++) {
//				max = Math.max(max, Math.abs(power[i]));
//			}
//			for (int i = 0; i < 4; i++) {
//				set.power[i] = power[i] / max;
//			}
//			return set;
//		}
//
//		/**
//		 * Generates a MotorSetPower that corresponds to turning the robot in
//		 * the specified direction
//		 */
//		private static MotorPowerSet calcPowerSet(
//				double direction, double turnRate, double speed) {
//			double robotAngle = direction + Math.PI / 4;
//			double v1 = speed * Math.sin(robotAngle) + turnRate;
//			double v2 = speed * Math.cos(robotAngle) - turnRate;
//			double v3 = speed * Math.cos(robotAngle) + turnRate;
//			double v4 = speed * Math.sin(robotAngle) - turnRate;
//			return new MotorPowerSet(v1, v2, v3, v4);
//		}
//	}
//}