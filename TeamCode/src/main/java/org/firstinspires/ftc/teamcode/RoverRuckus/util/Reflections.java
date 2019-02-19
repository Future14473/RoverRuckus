package org.firstinspires.ftc.teamcode.RoverRuckus.util;

public class Reflections {
	private Reflections() {
	}
	
	public static String getInformativeName(Object o) {
		Class<?> clazz = o.getClass();
		//if lambdas
		if (clazz.isSynthetic()) {
			String fullName = o.toString();
			int $ = fullName.indexOf('$');
			if ($ == -1) $ = fullName.length();
			return String.format("%s-(lambda)-> %s",
			                     fullName.substring(fullName.lastIndexOf('.') + 1, $),
			                     clazz.getInterfaces()[0].getSimpleName());
		}
		//If override toString, use that.
		try {
			if (clazz.getMethod("toString").getDeclaringClass() != Object.class)
				return o.toString();
		} catch (NoSuchMethodException e) {
			//shouldn't happen
			String name = clazz.getName();
			return name.substring(name.lastIndexOf('.') + 1);
		}
		//if anonymous, find out type.
		if (clazz.isAnonymousClass()) {
			StringBuilder builder = new StringBuilder();
			String declaringName = clazz.getName();
			builder.append(declaringName, declaringName.lastIndexOf('.') + 1,
			               declaringName.length())
			       .append("-(anonymous)-> ");
			Class<?> superclass = clazz.getSuperclass(); //will not be null for anonymous
			if (superclass == Object.class) { //may implement interface
				Class<?>[] interfaces = clazz.getInterfaces();
				if (interfaces.length != 0) clazz = interfaces[0];
				else clazz = Object.class; //or just extend Object.
			} else {
				clazz = superclass; //otherwise it extends its superclass.
			}
			if (clazz != null) {
				String name = clazz.getName();
				return builder.append(name, name.lastIndexOf('.') + 1, name.length())
				              .toString();
			} else return builder.append('?').toString(); //shouldn't happen
		}
		String fullName = clazz.getSimpleName();
		return fullName.substring(fullName.lastIndexOf('.') + 1);
	}
}
