package org.firstinspires.ftc.teamcode.RoverRuckus.util;

public class Reflections {
	private Reflections() {
	}
	
	private static String extractSimpleName(String fullName) {
		int $ = fullName.indexOf('$');
		if ($ == -1) $ = fullName.length();
		return fullName.substring(fullName.lastIndexOf('.') + 1, $);
	}
	
	public static String getInformativeName(Object o) {
		Class<?> clazz = o.getClass();
		//lambdas
		if (clazz.isSynthetic()) {
			return String.format("%s-(lambda)-> %s",
			                     extractSimpleName(o.toString()),
			                     clazz.getInterfaces()[0].getSimpleName());
		}
		//If override toString, use that.
		try {
			if (clazz.getMethod("toString").getDeclaringClass() != Object.class)
				return o.toString();
		} catch (NoSuchMethodException e) {
			//shouldn't happen
			return extractSimpleName(o.toString());
		}
		StringBuilder builder = new StringBuilder();
		while (clazz != null && clazz.isAnonymousClass()) {
			builder.append(extractSimpleName(clazz.getName())).append("-(anonymous)-> ");
			Class<?> superclass = clazz.getSuperclass(); //will have if anonymous
			if (superclass == Object.class) { //anonymous class might implement interface
				Class<?>[] interfaces = clazz.getInterfaces();
				if (interfaces.length != 0) clazz = interfaces[0];
				else clazz = Object.class; //or could also simply extend Object.
				break;
			}
			clazz = superclass;
		}
		if (clazz == null) clazz = o.getClass(); //shouldn't happen
		//fallthrough
		return builder.append(extractSimpleName(clazz.getName())).toString();
	}
}
