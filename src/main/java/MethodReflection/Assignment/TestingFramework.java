package MethodReflection.Assignment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*

Input to our framework is a class object

We need to:

If a method called beforeClass() is present, it needs to be called once, at the beginning of the test suite.

If a method with the name setupTest() is present it needs to be called before every test.

Every method that starts with the name test is considered a test case. We need to run each of those tests one after another. A separate object of the test class should be created for each test invocation. The order does not matter.

If a method called afterClass() is present, it needs to be run at the end of the test suite, only once.

Any other methods are considered helper methods and should be ignored.



Note: A proper beforeClass(), afterClass(), setupTest() and test*() method has to be

Public

Take zero parameters

Return void.

If either of those conditions are not met, the method should be ignored.



Assumptions

The test class is guaranteed to have a single declared empty constructor.

 */
public class TestingFramework {
	public void runTestSuite(Class<?> testClass) throws Throwable {
		/**
		 * Complete your code here
		 */
		Method[] methods = testClass.getDeclaredMethods();
		Constructor<?> constructor = testClass.getDeclaredConstructor();
		constructor.setAccessible(true);
		Object instance = constructor.newInstance();
		// Check for beforeClass
		Method beforeClassMethod = findMethodByName(methods, "beforeClass");
		if (beforeClassMethod != null) {
			if (commonCheckForEveryMethod(beforeClassMethod)) {
				beforeClassMethod.invoke(instance);
			}
		}
		// Check for setupTest() method
		Method setupTestMethod = findMethodByName(methods, "setupTest");

		// Check for test* methods
		List<Method> testMethodList = findMethodsByPrefix(methods, "test");
		for (Method method : testMethodList) {
			if (commonCheckForEveryMethod(method)) {
				invokeSetupTestMethod(setupTestMethod, instance);
				method.invoke(instance);
			}
		}

		// Check for afterClass
		Method afterClassMethod = findMethodByName(methods, "afterClass");
		if (afterClassMethod != null) {
			if (commonCheckForEveryMethod(afterClassMethod)) {
				afterClassMethod.invoke(instance);
			}
		}
	}

	private <T> void invokeSetupTestMethod(Method setupTestMethod, T instance) throws InvocationTargetException, IllegalAccessException {
		if (setupTestMethod != null) {
			if (commonCheckForEveryMethod(setupTestMethod)) {
				setupTestMethod.invoke(instance);
			}
		}
	}

	private boolean commonCheckForEveryMethod(Method method) {
		return Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 0 && method.getReturnType().equals(void.class);
	}

	/**
	 * Helper method to find a method by name
	 * Returns null if a method with the given name does not exist
	 */
	private Method findMethodByName(Method[] methods, String name) {
		/**
		 * Complete your code here
		 */
		return Arrays.stream(methods)
				.filter(method -> method.getName().equals(name))
				.findFirst()
				.orElseGet(null);
	}

	/**
	 * Helper method to find all the methods that start with the given prefix
	 */
	private List<Method> findMethodsByPrefix(Method[] methods, String prefix) {
		/**
		 * Complete your code here
		 */
		return Arrays.stream(methods)
				.filter(method -> method.getName().startsWith(prefix))
				.collect(Collectors.toList());
	}
}