package AnnotationReflection;

import AnnotationReflection.app.annotations.RetryOperation;
import AnnotationReflection.app.databases.CacheLoader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
When we request an annotation object, the JVM:
- Parses the annotation present at target at runtime.
- Creates a dynamic proxy object that implements the annotation interface.
Therefore once we get hold of that proxy object of annotation we can call its methods to get the values assigned to its elements.
*/
public class ProcessAnnotationElements {
	public static void main(String[] args) throws IllegalAccessException, InstantiationException {
		Class<?> clazz = CacheLoader.class;
		Object instance = clazz.newInstance();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			try {
				method.invoke(instance);
			} catch (InvocationTargetException e) {
				RetryOperation retryOperation = method.getAnnotation(RetryOperation.class);
				int numRetries = retryOperation.numberRetries();
				if (numRetries > 0) {
					// retry invocation
					numRetries -= 1;
				}
			}
		}
	}
}
