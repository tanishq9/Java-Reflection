package AnnotationReflection.app;

import AnnotationReflection.app.annotations.InitializerClass;
import AnnotationReflection.app.annotations.InitializerMethod;

@InitializerClass
public class AutoSaver {

	@InitializerMethod
	void startAutoSavingThread() {
		System.out.println("Start automatic data saving to disk.");
	}
}
