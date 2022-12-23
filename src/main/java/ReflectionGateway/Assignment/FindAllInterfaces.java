package ReflectionGateway.Assignment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FindAllInterfaces {

	/**
	 * Returns all the interfaces that the current input class implements.
	 * Note: If the input is an interface itself, the method returns all the interfaces the
	 * input interface extends.
	 */
	public static Set<Class<?>> findAllImplementedInterfaces(Class<?> input) {
		Set<Class<?>> allImplementedInterfaces = new HashSet<>();
		Class<?>[] inputInterfaces = input.getInterfaces();
		for (Class<?> currentInterface : inputInterfaces) {
			allImplementedInterfaces.add(currentInterface);
			helper(currentInterface, allImplementedInterfaces);
		}
		return allImplementedInterfaces;
	}

	static void helper(Class<?> currentInterface, Set<Class<?>> allImplementedInterfaces) {
		for (Class<?> childInterface : currentInterface.getInterfaces()) {
			allImplementedInterfaces.add(childInterface);
			helper(childInterface, allImplementedInterfaces);
		}
	}

	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<>();
		System.out.println(findAllImplementedInterfaces(map.getClass()));
	}
}
