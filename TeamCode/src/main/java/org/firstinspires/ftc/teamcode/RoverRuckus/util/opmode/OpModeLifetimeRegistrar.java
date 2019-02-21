package org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode;

import android.app.Activity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.RoverRuckus.util.Reflections;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.DEBUG_LOG;

/**
 * A means to cleanup resources automatically at the end of an OpMode,
 * no matter where initialized
 */
public class OpModeLifetimeRegistrar {
	
	private static final String TAG = OpModeLifetimeRegistrar.class.getSimpleName();
	
	/**
	 * Registers a stoppable to the OpModeManager, to close at the end of the
	 * currently active OpMode
	 *
	 * @param stoppable to be stopped when an OpMode ends
	 */
	public static void register(Stoppable stoppable) {
		register(stoppable, Reflections.getInformativeName(stoppable));
	}
	
	/**
	 * Registers a stoppable to the OpModeManager, to close at the end of the
	 * currently active OpMode
	 *
	 * @param stoppable the stoppable when the OpMode ends
	 * @param name      the name of the class to show on RobotLog
	 */
	public static void register(Stoppable stoppable, String name) {
		Activity activity = AppUtil.getInstance().getActivity();
		OpModeManagerImpl opModeManager = OpModeManagerImpl.getOpModeManagerOfActivity(activity);
		if (opModeManager != null) {
			opModeManager.registerListener(new OpModeManagerNotifier.Notifications() {
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
					opModeManager.unregisterListener(this);
				}
			});
			RobotLog.dd(TAG, "Registered %s", name);
		} else {
			throw new RuntimeException("No OpMode manager");
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
}
