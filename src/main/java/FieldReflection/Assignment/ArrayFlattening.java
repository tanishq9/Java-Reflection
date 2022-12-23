package FieldReflection.Assignment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayFlattening {

	public <T> T concat(Class<?> type, Object... arguments) {

		if (arguments.length == 0) {
			return null;
		}

		/**
		 * Complete code here
		 */

		List<Object> list = new ArrayList<>();

		for (Object arg : arguments) {
			if (arg.getClass().isArray()) {
				int length = Array.getLength(arg);
				for (int i = 0; i < length; i++) {
					list.add(Array.get(arg, i));
				}
			} else {
				list.add(arg);
			}
		}

		Object flattenedArray = Array.newInstance(type, list.size());

		for (int i = 0; i < list.size(); i++) {
			Array.set(flattenedArray, i, list.get(i));
		}

		return (T) flattenedArray;
	}
}