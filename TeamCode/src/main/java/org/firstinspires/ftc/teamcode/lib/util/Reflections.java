package org.firstinspires.ftc.teamcode.lib.util;

public class Reflections {
	private Reflections() {
	}
	
	/**
	 * Better than toString!
	 */
	public static String betterName(Object o) {
		Class<?> clazz = o.getClass();
		//lambdas
		String rawName = clazz.getName();
		if (clazz.isSynthetic()) {
			int $ = rawName.indexOf('$');
			if ($ == -1) $ = rawName.length(); //extract declaring class
			return String.format("%s-(lambda)-> %s",
			                     rawName.substring(rawName.lastIndexOf('.') + 1, $),
			                     clazz.getInterfaces()[0].getSimpleName());
		}
		//If override toString, use that.
		try {
			if (clazz.getMethod("toString").getDeclaringClass() != Object.class)
				return o.toString();
		} catch (NoSuchMethodException ignored) {
		}
		//else just do the thing without the hashcode.
		return rawName;
	}
}
