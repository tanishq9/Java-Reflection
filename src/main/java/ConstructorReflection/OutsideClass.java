package ConstructorReflection;

// Private methods and constructors are not accessible outside the class, using reflection we have access to all constructors be it private or public but to initialise object using private constructors we need to set accessible property to true for the constructor like: constructor.setAccessible(true);
public class OutsideClass {
	int i;

	private OutsideClass(int i) {
		this.i = i;
	}

	@Override
	public String toString() {
		return "OutsideClass{" +
				"i=" + i +
				'}';
	}
}
