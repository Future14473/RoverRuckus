package org.firstinspires.ftc.teamcode.RoverRuckus.tasks;

import org.firstinspires.ftc.teamcode.RoverRuckus.util.Reflections;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * A task that consists of executing multiple other tasks.
 */
public class CompositeTask implements Task {
	private List<Task> tasks;
	private String name = null;
	
	public CompositeTask(Task... tasks) {
		this.tasks = Arrays.asList(tasks);
	}
	
	@Override
	public void run() {
		tasks.forEach(Runnable::run);
	}
	
	@NotNull
	@Override
	public String toString() {
		if (name != null) return name;
		StringBuilder builder = new StringBuilder();
		builder.append('[').append('\n');
		for (Task task : tasks)
			builder.append('\t').append(Reflections.betterName(task)).append('\n');
		return name = builder.append(']').toString();
	}
}
