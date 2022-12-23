package MethodReflection;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvoke {
	public static void main(String[] args) throws Throwable {
		// invoke normal method
		Method[] methods = MethodInvoke.class.getDeclaredMethods();
		MethodInvoke methodInvokeInstance = new MethodInvoke();
		for (Method method : methods) {
			System.out.println("Method name is: " + method.getName());
			try {
				method.invoke(methodInvokeInstance, "Tanishq");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// In InvocationTargetException is thrown by this method in case any checked or unchecked exception is thrown while invoking the method, we can catch this exception and instead throw the exception it has wrapped.
				throw e.getTargetException();
			}
		}
	}

	static void method1(String name) {
		System.out.println("Name is: " + name);
	}

	void method2(String name) {
		System.out.println("Name is: " + name);
	}

	void method3(String name) throws IOException {
		throw new IOException("Exception");
	}
}
