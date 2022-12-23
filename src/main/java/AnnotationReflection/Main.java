package AnnotationReflection;

import AnnotationReflection.app.annotations.InitializerClass;
import AnnotationReflection.app.annotations.InitializerMethod;
import AnnotationReflection.app.annotations.ScanPackages;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ScanPackages(values = {"app", "app.configs", "app.databases"})
public class Main {
	public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, URISyntaxException, InvocationTargetException, ClassNotFoundException {
		// Class<?> clazz = Class.forName("AnnotationReflection.app.configs.ConfigLoader");
		initialize("AnnotationReflection.app");
	}

	// Package names where we are going to look for initializer classes
	static void initialize(String... packageNames) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, IOException, URISyntaxException {
		// Get all classes
		List<Class<?>> classes = getAllClasses(packageNames);

		for (Class<?> clazz : classes) {
			if (!clazz.isAnnotationPresent(InitializerClass.class)) {
				continue;
			}
			List<Method> methods = getAllInitMethods(clazz);
			// Now we will call those methods	with @InitializerMethod annotation
			Object instance = clazz.getDeclaredConstructor().newInstance(); // assuming no arg constructor is present in class
			for (Method method : methods) {
				method.invoke(instance); // assuming methods are no arg as well
			}
		}

	}

	private static List<Method> getAllInitMethods(Class<?> clazz) {
		List<Method> initMethods = new ArrayList<>();
		for (Method method : clazz.getDeclaredMethods()) {
			method.setAccessible(true);
			if (method.isAnnotationPresent(InitializerMethod.class)) {
				initMethods.add(method);
			}
		}
		return initMethods;
	}

/*
Given a package name, we want to obtain all Class<?> objects within that package so that we can look for desired annotation in them.
In Java, classes are not available to JVM until they are used in the code and then it is automatically loaded by the class loader.
So, if we want to discover the classes before they are used and loaded, we need to look for the files with the ".class" extension in the (package) path that corresponds to the given package.
The ".class" files contain the compiled java classes that can be loaded by the JVM and have same name as class itself.

Strategy to find .class files in a package:
- Example: We convert package name from "app.data" to "app/data".
- Use getResource to get URI of "app/data".
- Get path of package directory and look for .class files in there.
*/

	static List<Class<?>> getAllClasses(String... packageNames) throws URISyntaxException, IOException, ClassNotFoundException {
		List<Class<?>> allClasses = new ArrayList<>();
		for (String packageName : packageNames) {
			String packageRelativePath = packageName.replace('.', '/');
			URI packageUri = Main.class.getResource(packageRelativePath).toURI();

			// package can be in file system or jar file

			if (packageUri.getScheme().equals("file")) {
				Path packageFullPath = Paths.get(packageUri);
				allClasses.addAll(getAllPackageClasses(packageFullPath, packageName));
			} else if (packageUri.getScheme().equals("jar")) {
				FileSystem fileSystem = FileSystems.newFileSystem(packageUri, Collections.emptyMap());
				Path packageFullPathInJar = fileSystem.getPath(packageRelativePath);
				allClasses.addAll(getAllPackageClasses(packageFullPathInJar, packageName));

				fileSystem.close();
			}
		}
		return allClasses;
	}

	static List<Class<?>> getAllPackageClasses(Path packagePath, String packageName) throws IOException, ClassNotFoundException {
		if (!Files.exists(packagePath)) {
			return Collections.emptyList();
		}
		List<Path> files = Files.list(packagePath) // similar to ls
				.filter(Files::isRegularFile) // check if file and not some dir
				.collect(Collectors.toList());
		List<Class<?>> classes = new ArrayList<>();
		for (Path filePath : files) {
			String fileName = filePath.getFileName().toString();
			if (fileName.endsWith(".class")) {
				String classFullName = packageName + "." + fileName.replaceFirst("\\.class$", "");
				Class<?> clazz = Class.forName(classFullName);
				classes.add(clazz);
			}
		}
		return classes;
	}
}

