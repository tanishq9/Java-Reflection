package AnnotationReflection.app.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryOperation {
	int numberRetries();

	long durationBetweenRetries() default 0;

	Class<? extends Throwable>[] retryableExceptions() default {Exception.class};
}
