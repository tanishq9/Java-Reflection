package ReflectionGateway;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException {
		// Class<?> is an entry point for us to reflect on our application's code.

		// Multiple ways to do above
		// 1. Object.getClass()
		Map<String, Integer> map = new HashMap<>();
		Class<?> hashMapClass = map.getClass();
		// 2. Add .class suffix to class names.
		Class<String> stringClass = String.class;
		Class intClass = int.class;
		// 3. Class.forName(..)
		Class<?> squareClass = Class.forName("ReflectionGateway.Main$Square");
		printClassInfo(hashMapClass, stringClass, intClass, squareClass);

		System.out.println("-------------");

		var circleObject = new Drawable() {
			@Override
			public int getNumberOfCorners() {
				return 0;
			}
		};

		printClassInfo(Collections.class, boolean.class, int[][].class, circleObject.getClass(), Color.class);
	}

	private static void printClassInfo(Class<?>... classes) {
		for (Class<?> clazz : classes) {
			System.out.println(String.format("Class name %s, class package name %s", clazz.getSimpleName(), clazz.getPackageName()));
			Class<?>[] implementedInterfaces = clazz.getInterfaces();

			for (Class<?> implementedInterface : implementedInterfaces) {
				System.out.println(String.format("class %s implements %s", clazz.getSimpleName(), implementedInterface.getSimpleName()));
			}

			System.out.println("Is array: " + clazz.isArray());
			System.out.println("Is primitive: " + clazz.isPrimitive());
			System.out.println("Is enum: " + clazz.isEnum());
			System.out.println("Is interface: " + clazz.isInterface());
			System.out.println("Is anonymous: " + clazz.isAnonymousClass());
			System.out.println("Superclass: " + clazz.getSuperclass());
			System.out.println("Canonical name: " + clazz.getCanonicalName());
			System.out.println("Type name: " + clazz.getTypeName());
			System.out.println("Annotated Interfaces: " + clazz.getAnnotatedInterfaces());
			System.out.println("Annotated Superclass: " + clazz.getAnnotatedSuperclass());
			System.out.println("Declared Annotations: " + clazz.getDeclaredAnnotations());

			System.out.println();
			System.out.println();
		}
	}

	private static class Square implements Drawable {
		@Override
		public int getNumberOfCorners() {
			return 4;
		}
	}

	private static interface Drawable {
		int getNumberOfCorners();
	}

	private enum Color {
		BLUE,
		RED,
		GREEN
	}
}
