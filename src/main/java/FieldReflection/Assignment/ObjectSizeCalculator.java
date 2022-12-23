package FieldReflection.Assignment;

import java.lang.reflect.Field;

public class ObjectSizeCalculator {
	private static final long HEADER_SIZE = 12;
	private static final long REFERENCE_SIZE = 4;

	public long sizeOfObject(Object input) throws IllegalAccessException {
		/**
		 * Complete your code here
		 */
		Field[] fields = input.getClass().getDeclaredFields();
		long size = 0;
		for (Field field : fields) {
			Class<?> fieldClass = field.getType();
			field.setAccessible(true);
			if (fieldClass.isPrimitive()) {
				size += sizeOfPrimitiveType(fieldClass);
			} else if (fieldClass.equals(String.class)) {
				size += sizeOfString((String) field.get(input));
			} else {
				size += sizeOfObject(field.get(input));
			}
		}
		return size + HEADER_SIZE + REFERENCE_SIZE;
	}

	/*************** Helper methods ********************************/
	private long sizeOfPrimitiveType(Class<?> primitiveType) {
		if (primitiveType.equals(int.class)) {
			return 4;
		} else if (primitiveType.equals(long.class)) {
			return 8;
		} else if (primitiveType.equals(float.class)) {
			return 4;
		} else if (primitiveType.equals(double.class)) {
			return 8;
		} else if (primitiveType.equals(byte.class)) {
			return 1;
		} else if (primitiveType.equals(short.class)) {
			return 2;
		}
		throw new IllegalArgumentException(String.format("Type: %s is not supported", primitiveType));
	}

	private long sizeOfString(String inputString) {
		int stringBytesSize = inputString.getBytes().length;
		return HEADER_SIZE + REFERENCE_SIZE + stringBytesSize;
	}
}