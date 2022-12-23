package FieldReflection.Assignment.jsonwriter;

import FieldReflection.Assignment.data.Address;
import FieldReflection.Assignment.data.Company;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
	public static void main(String[] args) throws IllegalAccessException {
		Company company = new Company("T", new Address("A", "B"), 10, new String[]{"A", "B", "C"});
		System.out.println(objectToJson(company, 0));
	}

	// Serialise object into json string
	static String objectToJson(Object object, int indentSize) throws IllegalAccessException {
		Field[] fields = object.getClass().getDeclaredFields();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{\n");

		System.out.println(Arrays.stream(fields).collect(Collectors.toList()));

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			stringBuilder.append(addIndentation(indentSize + 1));
			stringBuilder.append(formatString(fieldName) + ": ");

			if (field.isSynthetic()) {
				continue;
			}

			field.setAccessible(true); // required to access fields outside current package

			Class<?> fieldType = field.getType();

			if (fieldType.isPrimitive()) {
				stringBuilder.append(formatPrimitiveValue(field.getType(), field.get(object)));
			} else if (fieldType.equals(String.class)) {
				stringBuilder.append(formatString((String) field.get(object)));
			} else if (field.getType().isArray()) {
				stringBuilder.append(arrayToJson(field.get(object), indentSize));
			} else {
				// in case of custom objects
				stringBuilder.append(objectToJson(field.get(object), indentSize + 1));
			}

			if (i != fields.length - 1) {
				stringBuilder.append(",");
			}
			stringBuilder.append("\n");
		}
		stringBuilder.append(addIndentation(indentSize));
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	private static String addIndentation(int indentSize) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < indentSize; i++) {
			stringBuilder.append("\t");
		}
		return stringBuilder.toString();
	}

	static String formatPrimitiveValue(Class<?> classType, Object object) throws IllegalAccessException {
		if (classType.equals(int.class) || classType.equals(boolean.class)) {
			return object.toString();
		} else if (classType.equals(double.class) || classType.equals(float.class)) {
			return String.format("%.2f", object);
		}
		throw new RuntimeException("Primitive type not supported");
	}

	static String formatString(String value) {
		// double quote the string value
		return String.format("\"%s\"", value);
	}


	private static String arrayToJson(Object arrayInstance, int indentSize) throws IllegalAccessException {
		StringBuilder stringBuilder = new StringBuilder();
		int arrLength = Array.getLength(arrayInstance);
		Class<?> componentType = arrayInstance.getClass().getComponentType();
		stringBuilder.append("[");
		for (int i = 0; i < arrLength; i++) {
			Object currentElement = Array.get(arrayInstance, i);
			// Only supporting integer and string array types for now
			if (currentElement.getClass().isPrimitive()) {
				stringBuilder.append(formatPrimitiveValue(componentType, currentElement));
			} else if (currentElement.getClass().equals(String.class)) {
				stringBuilder.append(formatString(currentElement.toString()));
			} else {
				stringBuilder.append(objectToJson(currentElement, indentSize + 1));
			}
			if (i != arrLength - 1) {
				stringBuilder.append(" , ");
			}
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}
