package AnnotationReflection.app.configs;

import AnnotationReflection.app.annotations.InitializerClass;
import AnnotationReflection.app.annotations.InitializerMethod;

@InitializerClass
public class ConfigLoader {

	@InitializerMethod
	void loadAllConfigs() {
		System.out.println("Loading all config files.");
	}
}
