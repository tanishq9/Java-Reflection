package ConstructorReflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
	public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException {
		printConstructors(Person.class);
		printConstructors(Random.class);

		// This would trigger the no-arg constructor
		Person person = createInstanceWithArgs(Person.class);
		System.out.println(person);
		Person person2 = createInstanceWithArgs(Person.class, "Tanishq");
		System.out.println(person2);
		Random random = createInstanceWithArgs(Random.class, 5);
		System.out.println(random);

		OutsideClass outsideClass = createInstanceWithArgs(OutsideClass.class, 1);
		System.out.println(outsideClass);
	}

	public static void printConstructors(Class<?> clazz) {
		Constructor<?>[] constructorClasses = clazz.getDeclaredConstructors();
		System.out.println(String.format("class %s has %d declared constructors", clazz.getSimpleName(), constructorClasses.length));
		for (Constructor<?> constructor : constructorClasses) {
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			List<String> parameterTypeNames = Arrays.stream(parameterTypes)
					.map(Class::getSimpleName)
					.collect(Collectors.toList());
			System.out.println(parameterTypeNames);
		}
	}

	public static <T> T createInstanceWithArgs(Class<T> clazz, Object... args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
		for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
			if (constructor.getParameterTypes().length == args.length) {
				constructor.setAccessible(true); // required to initialise object using private constructor outside the class or package else we get compilation error
				// Exception in thread "main" java.lang.IllegalAccessException: class ConstructorReflection.GetterSetterDiscovery cannot access a member of class ConstructorReflection.OutsideClass with modifiers "private"

				return (T) constructor.newInstance(args);
			}
		}
		System.out.println("No apt constructor was found");
		return null;
	}

	public static class Person {
		private final String address;
		private final String name;
		private final int age;

		public Person() {
			this.address = null;
			this.age = 0;
			this.name = "anonymous";
		}

		public Person(String name) {
			this.name = name;
			this.age = 0;
			this.address = null;
		}

		public Person(String name, int age) {
			this.name = name;
			this.age = age;
			this.address = null;
		}

		public Person(String name, int age, String address) {
			this.name = name;
			this.age = age;
			this.address = address;
		}

		@Override
		public String toString() {
			return "Person{" +
					"address='" + address + '\'' +
					", name='" + name + '\'' +
					", age=" + age +
					'}';
		}
	}

	static class Random {
		private int i;

		private Random(int i) {
			this.i = i;
		}

		@Override
		public String toString() {
			return "Random{" +
					"i=" + i +
					'}';
		}
	}
}
