import ConstructorReflection.DIDemo.ClassA;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
		// We will initialise object/dependencies in ConstructorReflection.DIDemo class which are package-private using reflection
		// Automatic dependency injection using reflection
		ClassA rootClass = (ClassA) initialiseDIGraph(Class.forName("ConstructorReflection.DIDemo.ClassA"));
		System.out.println(rootClass);
	}

	static <T> T initialiseDIGraph(Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
		Constructor<?> constructor = getFirstConstructor(clazz);
		List<Object> constructorArgs = new ArrayList<>();
		for (Class<?> argType : constructor.getParameterTypes()) {
			// For ClassA, we will get ClassB and ClassC classes as constructor args
			Object argValue = initialiseDIGraph(argType);
			constructorArgs.add(argValue);
		}
		constructor.setAccessible(true); // to access package private constructor
		return (T) constructor.newInstance(constructorArgs.toArray());
	}

	// logic could be extended to consider other multiple constructor of a class as well
	static Constructor<?> getFirstConstructor(Class<?> clazz) {
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		if (constructors.length == 0) {
			throw new RuntimeException("No constructor found");
		}
		return constructors[0];
	}

}
