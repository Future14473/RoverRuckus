package org.firstinspires.ftc.teamcode.RoverRuckus.tasks;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.Reflections;

import java.util.Arrays;
import java.util.List;

/**
 * A task that consists of executing multiple other tasks.
 */
public class CompositeTask implements Task {
	private List<Task> tasks;
	
	public CompositeTask(Task... tasks) {
		this.tasks = Arrays.asList(tasks);
	}
	
	@Override
	public void run() {
		tasks.forEach(Runnable::run);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[').append('\n');
		for (Task task : tasks) {
			builder.append('\t').append(Reflections.getInformativeName(task)).append('\n');
		}
		return builder.append(']').toString();
	}
}
