package DynamicCachingProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CachingInvocationHandler implements InvocationHandler {

	/**
	 * Map that maps from a method name to a method cache
	 * Each cache is a map from a list of arguments to a method result
	 */
	private final Map<String, Map<List<Object>, Object>> cache = new HashMap<>();

	/**
	 * Add any additonal member variables here
	 */
	Object realObject;

	public CachingInvocationHandler(Object realObject) {
		/**
		 * Complete your code here
		 */
		this.realObject = realObject;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		/**
		 * Complete your code here
		 */
		if (isMethodCacheable(method)) {
			try {
				Object result = null;
				List<Object> paramList = Arrays.stream(args).collect(Collectors.toList());
				if (cache.containsKey(method.getName())) {
					Map<List<Object>, Object> paramReturnValue = cache.get(method.getName());
					if (paramReturnValue.containsKey(paramList)) {
						return paramReturnValue.get(paramList);
					} else {
						result = method.invoke(realObject, args);
						Map<List<Object>, Object> listObjectMap = cache.get(method.getName());
						listObjectMap.put(paramList, result);
						cache.put(method.getName(), listObjectMap);
					}
				} else {
					result = method.invoke(realObject, args);
					cache.put(method.getName(), new HashMap<>()); // init cache for that cacheable method
					Map<List<Object>, Object> listObjectMap = cache.get(method.getName());
					listObjectMap.put(paramList, result);
					cache.put(method.getName(), listObjectMap);
				}
				return result;
			} catch (InvocationTargetException exception) {
				throw exception.getTargetException();
			}
		} else {
			return method.invoke(realObject, args);
		}
	}

	boolean isMethodCacheable(Method method) {
		/**
		 * Complete your code here
		 */
		return method.isAnnotationPresent(Cacheable.class);
	}

	/******************************* Helper Methods **************************/

	private boolean isInCache(Method method, Object[] args) {
		if (!cache.containsKey(method.getName())) {
			return false;
		}
		List<Object> argumentsList = Arrays.asList(args);

		Map<List<Object>, Object> argumentsToReturnValue = cache.get(method.getName());

		return argumentsToReturnValue.containsKey(argumentsList);
	}

	private void putInCache(Method method, Object[] args, Object result) {
		if (!cache.containsKey(method.getName())) {
			cache.put(method.getName(), new HashMap<>());
		}
		List<Object> argumentsList = Arrays.asList(args);

		Map<List<Object>, Object> argumentsToReturnValue = cache.get(method.getName());

		argumentsToReturnValue.put(argumentsList, result);
	}

	private Object getFromCache(Method method, Object[] args) {
		if (!cache.containsKey(method.getName())) {
			throw new IllegalStateException(String.format("Result of method: %s is not in the cache", method.getName()));
		}

		List<Object> argumentsList = Arrays.asList(args);

		Map<List<Object>, Object> argumentsToReturnValue = cache.get(method.getName());

		if (!argumentsToReturnValue.containsKey(argumentsList)) {
			throw new IllegalStateException(String.format("Result of method: %s and arguments: %s is not in the cache",
					method.getName(),
					argumentsList));
		}

		return argumentsToReturnValue.get(argumentsList);
	}
}
