package ModifierReflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class Main {
	private static final Integer MAX = 5;

	public static void main(String[] args) {
		// Class<?> clazz = HashMap.class;
		Class<?> clazz = Map.class;

		int modifier = clazz.getModifiers();

		// Class abstract/interface/static
		if (Modifier.isAbstract(modifier)) {
			System.out.println("Class is abstract");
		} else if (Modifier.isInterface(modifier)) {
			System.out.println("Class is interface");
		} else if (Modifier.isStatic(modifier)) {
			System.out.println("Class is static");
		}

		// Class access modifier
		if (Modifier.isPrivate(modifier)) {
			System.out.println("Access modifier is final");
		} else if (Modifier.isPublic(modifier)) {
			System.out.println("Access modifier is public");
		} else if (Modifier.isProtected(modifier)) {
			System.out.println("Access modifier is protected");
		} else {
			System.out.println("Access modifier is package-private");
		}

		checkFieldModifier();
	}

	static void checkFieldModifier() {
		Field[] fields = Main.class.getDeclaredFields();

		for (Field field : fields) {
			System.out.println(field.getName());

			int mdf = field.getModifiers();
			if (Modifier.isStatic(mdf)) {
				System.out.println("Field is static.");
			}
			if (Modifier.isPublic(mdf)) {
				System.out.println("Field is public.");
			}
			if (Modifier.isPrivate(mdf)) {
				System.out.println("Field is private.");
			}
			if (Modifier.isTransient(mdf)) {
				System.out.println("Field is transient.");
			}
		}
	}
}
