package ConstructorReflection.DIDemo;

public class ClassA {
	private ClassB classB;
	private ClassC classC;

	ClassA(ClassB classB, ClassC classC) {
		this.classB = classB;
		this.classC = classC;
	}
}
