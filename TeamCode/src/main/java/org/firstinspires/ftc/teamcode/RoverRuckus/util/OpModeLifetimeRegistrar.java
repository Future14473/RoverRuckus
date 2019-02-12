package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import android.app.Activity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

/**
 * A means to cleanup resources automatically at the end of an OpMode,
 * no matter where initialized
 */
public class OpModeLifetimeRegistrar {
	/**
	 * Registers a closeable to the OpModeManager, to close at the end of the
	 * currently
	 * active OpMode
	 */
	public static void register(Closeable closeable) {
		Activity activity = AppUtil.getInstance().getActivity();
		OpModeManagerImpl opModeManager =
				OpModeManagerImpl.getOpModeManagerOfActivity(activity);
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
					closeable.close();
					opModeManager.unregisterListener(this);
				}
			});
		} else {
			throw new IllegalStateException("No OpMode manager");
		}
	}
	
	/**
	 * Indicates something that's closeable
	 * Used to automatically close a resource on OpMode termination.
	 */
	public interface Closeable {
		/**
		 * Stops/cleanups the current resource
		 */
		void close();
	}
}
