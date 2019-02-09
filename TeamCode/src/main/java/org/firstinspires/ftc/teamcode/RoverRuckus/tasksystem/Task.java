package org.firstinspires.ftc.teamcode.RoverRuckus.tasksystem;

/**
 * Adaptation of runnable that throws InterruptedException, to be passed on to
 * higher thread.
 */
public interface Task {
	void run() throws InterruptedException;
}
