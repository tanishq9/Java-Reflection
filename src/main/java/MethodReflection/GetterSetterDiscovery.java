package MethodReflection;

import MethodReflection.data.ClothingProduct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetterSetterDiscovery {
	public static void main(String[] args) {
		Class<?> clazz = ClothingProduct.class;
		System.out.println(Arrays.stream(clazz.getMethods()).collect(Collectors.toList()));
		testGetters(clazz);
		testSetters(clazz);
	}

	// Check if every field has a setter present
	static void testSetters(Class<?> dataClass) {
		// Field[] fields = dataClass.getDeclaredFields();
		List<Field> fields = getAllFields(dataClass);

		for (Field field : fields) {
			String setterName = "set" + capitaliseFirstLetterOfString(field.getName());
			Method setterMethod = null;
			try {
				setterMethod = dataClass.getMethod(setterName, field.getType());
			} catch (NoSuchMethodException noSuchMethodException) {
				throw new RuntimeException("Setter does not exist.");
			}
			if (!setterMethod.getReturnType().equals(void.class)) {
				throw new RuntimeException(String.format("Incorrect return type for method %s", setterMethod.getName()));
			}
		}
	}

	// Check if every field has a getter present
	static void testGetters(Class<?> dataClass) {
		// Field[] fields = dataClass.getDeclaredFields();
		List<Field> fields = getAllFields(dataClass);

		Map<String, Method> methodNameToMethod = mapMethodNameToMethod(dataClass);
		for (Field field : fields) {
			String fieldName = field.getName();
			fieldName = capitaliseFirstLetterOfString(fieldName);
			String getterName = "get" + fieldName;
			if (!methodNameToMethod.containsKey(getterName)) {
				throw new RuntimeException("Getter doesn't exist for field: " + fieldName);
			}
			Method method = methodNameToMethod.get(getterName);
			if (method.getParameterCount() > 0) {
				throw new RuntimeException("Has >0 parameters");
			}
			if (!method.getReturnType().equals(field.getType())) {
				throw new RuntimeException("Incorrect return type");
			}
		}
	}

	// Returns all fields for current class and all of its superclasses
	private static List<Field> getAllFields(Class<?> clazz) {
		if (clazz == null || clazz.equals(Object.class)) {
			return Collections.emptyList();
		}

		Field[] currentClassFields = clazz.getDeclaredFields();
		List<Field> inheritedClassFields = getAllFields(clazz.getSuperclass());

		List<Field> allFields = new ArrayList<>();
		allFields.addAll(inheritedClassFields);
		allFields.addAll(Arrays.asList(currentClassFields));

		return allFields;
	}

	private static String capitaliseFirstLetterOfString(String fieldName) {
		return fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1));
	}

	static Map<String, Method> mapMethodNameToMethod(Class<?> dataClass) {
		Method[] allMethods = dataClass.getMethods(); // gets us public methods defined in this class and in its superclass (continue till top of hierarchy chain)
		Map<String, Method> methodNameToMethod = new HashMap<>();
		for (Method method : allMethods) {
			methodNameToMethod.put(method.getName(), method);
		}
		return methodNameToMethod;
	}

}
