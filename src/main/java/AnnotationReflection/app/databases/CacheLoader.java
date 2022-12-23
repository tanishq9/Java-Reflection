package AnnotationReflection.app.databases;

import AnnotationReflection.app.annotations.InitializerClass;
import AnnotationReflection.app.annotations.InitializerMethod;
import AnnotationReflection.app.annotations.RetryOperation;
import java.io.IOException;

@InitializerClass
public class CacheLoader {

	@InitializerMethod
	@RetryOperation(
			numberRetries = 3,
			retryableExceptions = {IllegalArgumentException.class, IOException.class},
			durationBetweenRetries = 100
	)
	void loadCache() {
		System.out.println("Loading data from cache");
	}

	@InitializerMethod
	void reloadCache() {
		System.out.println("Reload cache");
	}
}
