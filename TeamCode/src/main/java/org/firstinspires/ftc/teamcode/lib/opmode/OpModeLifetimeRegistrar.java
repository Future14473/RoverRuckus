package org.firstinspires.ftc.teamcode.lib.opmode;

import android.app.Activity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.lib.util.Reflections;

import java.util.IdentityHashMap;
import java.util.Map;

import static org.firstinspires.ftc.teamcode.config.Constants.DEBUG_LOG;

/**
 * A means to cleanup resources automatically at the end of an OpMode,
 * no matter where initialized
 */
public class OpModeLifetimeRegistrar {
	
	private static final String TAG =
			OpModeLifetimeRegistrar.class.getSimpleName();
	private static final Map<Stoppable, OpModeManagerNotifier.Notifications>
	                            map = new IdentityHashMap<>();
	
	/**
	 * Registers a stoppable to the OpModeManager, to close at the end of the
	 * currently active OpMode
	 *
	 * @param stoppable to be stopped when an OpMode ends
	 */
	public static void register(Stoppable stoppable) {
		register(stoppable, Reflections.nameFor(stoppable));
	}
	
	/**
	 * Registers a stoppable to the OpModeManager, to close at the end of the
	 * currently active OpMode
	 *
	 * @param stoppable the stoppable when the OpMode ends
	 * @param name      the name of the class to show on RobotLog
	 */
	public static void register(Stoppable stoppable, String name) {
		OpModeManagerImpl opModeManager = getOpModeManager();
		if (opModeManager != null) {
			OpModeManagerNotifier.Notifications listener =
					new Listener(name, stoppable, opModeManager);
			synchronized (map) {
				map.put(stoppable, listener);
			}
			opModeManager.registerListener(listener);
			RobotLog.dd(TAG, "Registered %s", name);
		} else {
			throw new RuntimeException("No OpMode manager");
		}
	}
	
	private static OpModeManagerImpl getOpModeManager() {
		Activity activity = AppUtil.getInstance().getActivity();
		return OpModeManagerImpl.getOpModeManagerOfActivity(activity);
	}
	
	public static void unRegister(Stoppable stoppable) {
		OpModeManagerNotifier.Notifications listener;
		OpModeManagerImpl opModeManager = getOpModeManager();
		synchronized (map) {
			listener = map.remove(stoppable);
		}
		if (listener != null && opModeManager != null) {
			opModeManager.unregisterListener(listener);
		}
	}
	
	/**
	 * Indicates something that's stoppable
	 * Used to automatically stop something on OpMode termination. Usually a
	 * thread.
	 */
	public interface Stoppable {
		/**
		 * Stops something
		 */
		void stop();
	}
	
	private static class Listener implements OpModeManagerNotifier.Notifications {
		private final String            name;
		private final Stoppable         stoppable;
		private final OpModeManagerImpl opModeManager;
		
		public Listener(String name, Stoppable stoppable,
		                OpModeManagerImpl opModeManager) {
			this.name = name;
			this.stoppable = stoppable;
			this.opModeManager = opModeManager;
		}
		
		@Override
		public void onOpModePreInit(OpMode opMode) {
			//shouldn't happen, but eh.
			stop();
		}
		
		@Override
		public void onOpModePreStart(OpMode opMode) {
		}
		
		@Override
		public void onOpModePostStop(OpMode opMode) {
			stop();
		}
		
		private void stop() {
			if (DEBUG_LOG)
				RobotLog.dd(TAG, "Stopping %s", name);
			stoppable.stop();
			synchronized (map) {
				map.remove(stoppable);
			}
			opModeManager.unregisterListener(this);
		}
	}
}
