package org.firstinspires.ftc.teamcode.RoverRuckus.util;

public class MoreReflect {
	private MoreReflect() {
	}
	
	private static String extractSimpleName(String fullName) {
		int $ = fullName.indexOf('$');
		if ($ == -1) $ = fullName.length();
		return fullName.substring(fullName.lastIndexOf('.')+1, $);
	}
	
	public static String getInformativeName(Object o) {
		Class<?> clazz = o.getClass();
		if (clazz.isSynthetic()) {
			return String.format("%s-(lambda)-> %s",
			                     extractSimpleName(o.toString()),
			                     clazz.getInterfaces()[0].getSimpleName());
		}
		try {
			if (clazz.getMethod("toString").getDeclaringClass() != Object.class)
				return o.toString();
		} catch (NoSuchMethodException e) {
			return clazz.toString();
		}
		StringBuilder builder = new StringBuilder();
		while (clazz != null && clazz.isAnonymousClass()) {
			builder.append(extractSimpleName(clazz.getName())).append("-(anonymous)-> ");
			Class<?> superclass = clazz.getSuperclass();
			if (superclass == Object.class) {
				clazz = clazz.getInterfaces()[0];
				break;
			}
			clazz = superclass;
		}
		if (clazz == null) clazz = o.getClass();
		//fallthrough
		return builder.append(extractSimpleName(clazz.getName())).toString();
	}
}
