package FieldReflection.FileParser;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException {
		// We want to parse properties mentioned in application.cfg into Test object
		Test test = createConfigObject(Test.class, Path.of("/Users/tsaluja/Documents/HelloReflection/src/main/java/FieldReflection/FileParser/application.cfg"));
		System.out.println(test);
	}

	public static <T> T createConfigObject(Class<T> clazz, Path filePath) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Scanner scanner = new Scanner(filePath);
		Constructor<?> constructor = clazz.getDeclaredConstructor(); // would return no-arg constructor
		constructor.setAccessible(true);

		T instance = (T) constructor.newInstance();

		while (scanner.hasNextLine()) {
			String[] configLine = scanner.nextLine().split("=");
			String propertyName = configLine[0];
			String propertyValue = configLine[1];

			Field field;
			try {
				field = instance.getClass().getDeclaredField(propertyName);
			} catch (NoSuchFieldException exception) {
				continue;
			}
			field.setAccessible(true);
			if (field.isSynthetic() || Modifier.isStatic(field.getModifiers())) {
				continue;
			}

			Object parsedValue;
			if (field.getType().isArray()) {
				parsedValue = parseArray(field.getType().getComponentType(), propertyValue); // .getComponentType() will tell about type of array
			} else {
				parsedValue = parseValue(field.getType(), propertyValue);
			}
			field.set(instance, parsedValue);
		}
		return instance;
	}

	private static Object parseArray(Class<?> arrayElementType, String value) {
		String[] elementValues = value.split(",");
		Object arrayObject = Array.newInstance(arrayElementType, elementValues.length);
		for (int i = 0; i < elementValues.length; i++) {
			Array.set(arrayObject, i, parseValue(arrayElementType, elementValues[i]));
		}
		return arrayObject;
	}

	private static Object parseValue(Class<?> type, String propertyValue) {
		if (type.equals(int.class)) {
			return Integer.parseInt(propertyValue);
		} else if (type.equals(String.class)) {
			return propertyValue;
		}
		throw new RuntimeException(String.format("Type: %s unsupported.", type.getTypeName()));
	}

	// Setting field value for final field is highly discouraged.
	// Setting static (read-only) fields is not possible even if we set accessible for field to true.
}
