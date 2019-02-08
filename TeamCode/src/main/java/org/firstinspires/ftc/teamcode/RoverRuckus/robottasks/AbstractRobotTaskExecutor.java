package org.firstinspires.ftc.teamcode.RoverRuckus.robottasks;

import android.app.Activity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class AbstractRobotTaskExecutor implements RobotTaskExecutor {
	
	protected final RobotTaskExecutor robotTaskExecutor;
	protected final IRobot robot;
	private final OpModeManagerImpl opModeManager;
	private final OpModeManagerNotifier.Notifications listener = new OpModeListener();
	
	protected AbstractRobotTaskExecutor(IRobot robot) {
		robotTaskExecutor = new RobotTaskExecutorImpl(robot);
		this.robot = robot;
		Activity activity = AppUtil.getInstance().getActivity();
		opModeManager = OpModeManagerImpl.getOpModeManagerOfActivity(activity);
		if (opModeManager != null) opModeManager.registerListener(listener);
	}
	
	@Override
	public void add(RobotTask robotTask) {
		robotTaskExecutor.add(robotTask);
	}
	
	@Override
	public void cancel() {
		robotTaskExecutor.cancel();
	}
	
	@Override
	public boolean isDone() {
		return robotTaskExecutor.isDone();
	}
	
	@Override
	public void waitUntilDone() throws InterruptedException {
		robotTaskExecutor.waitUntilDone();
	}
	
	@Override
	public void start() {
		robotTaskExecutor.start();
	}
	
	@Override
	public void pause() {
		robotTaskExecutor.pause();
	}
	
	@Override
	public void resume() {
		robotTaskExecutor.resume();
	}
	
	@Override
	public boolean isPaused() {
		return robotTaskExecutor.isPaused();
	}
	
	@Override
	public void stop() {
		robotTaskExecutor.stop();
		if (opModeManager != null) opModeManager.unregisterListener(listener);
	}
	
	private class OpModeListener implements OpModeManagerNotifier.Notifications {
		@Override
		public void onOpModePreInit(OpMode opMode) {
		
		}
		
		@Override
		public void onOpModePreStart(OpMode opMode) {
			start();
		}
		
		@Override
		public void onOpModePostStop(OpMode opMode) {
			stop();
		}
	}
	
}
