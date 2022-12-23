package AnnotationReflection.app.databases;

import AnnotationReflection.app.annotations.InitializerClass;
import AnnotationReflection.app.annotations.InitializerMethod;

@InitializerClass
public class DatabaseConnection {

	@InitializerMethod
	void db1() {
		System.out.println("Connecting to db1");
	}

	@InitializerMethod
	void db2() {
		System.out.println("Connecting to db2");
	}
}
