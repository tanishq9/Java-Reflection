package AnnotationReflection.Assignment2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Annotations {
	/**
	 * COMPLETE YOUR CODE HERE
	 */
	@Target(ElementType.TYPE)
	@Repeatable(PermissionsContainer.class)
	public @interface Permissions {
		Role role();

		OperationType[] allowed();
		/**
		 * COMPLETE YOUR CODE HERE
		 */
	}

	/**
	 * COMPLETE YOUR CODE HERE
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface PermissionsContainer {
		Permissions[] value();
	}
}