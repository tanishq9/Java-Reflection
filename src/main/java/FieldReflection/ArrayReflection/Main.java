package FieldReflection.ArrayReflection;

import java.lang.reflect.Array;

public class Main {
	// We can get basic information about arrays using Class<?> API, i.e. Class.isArray()
	// and use Class.getComponentType() to get the type of array elements.

	public static void main(String[] args) {
		int[][][] arr3d = {{{1, 2, 3}}, {{4, 5, 6}}, {{7, 8, 9}}};
		Class<?> clazz = arr3d.getClass();
		if (clazz.isArray()) {
			System.out.println("Type of array: " + clazz.getComponentType().getTypeName());
			inspectArrayValues(arr3d);
		}
	}

	static void inspectArrayValues(Object arrayObject) {
		int length = Array.getLength(arrayObject);
		System.out.print("[");
		for (int i = 0; i < length; i++) {
			Object currentElement = Array.get(arrayObject, i);
			if (currentElement.getClass().isArray()) {
				inspectArrayValues(currentElement);
			} else {
				System.out.print(currentElement);
			}
			if (i != length - 1) {
				System.out.print(" , ");
			}
		}
		System.out.print("]");
	}


}
