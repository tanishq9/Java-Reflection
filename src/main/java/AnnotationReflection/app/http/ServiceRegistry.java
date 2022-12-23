package AnnotationReflection.app.http;

import AnnotationReflection.app.annotations.InitializerClass;
import AnnotationReflection.app.annotations.InitializerMethod;

@InitializerClass
public class ServiceRegistry {

	@InitializerMethod
	void registerService() {
		System.out.println("Service successfully registered.");
	}
}
