### Notes

#### Reflection API Gateway & Wildcards

- Class<?> is an entry point for us to reflect on our application's code.
- An object of Class<?> contains all information: What methods and fields it contains, what classes it extends and what interfaces it implements.

How to get class of an object? 

- Object.getClass()
  - Example
    - String stringObject = "some";
    - Map map = new HashMap<>();
    - We can do stringObject.getClass(), map.getClass()
    - Drawback: No .getClass() on primitive types, it gives compile errors.

- Add .class suffix to class names.
- Class.forName(..). To lookup a class using its fully qualified name.

Note: Class<? extends Collection> puts restriction to only types that extend Collection.

#### Constructor Discovery & Object Creation

- A constructor of a java class is represented by an instance of Constructor<?> class.
- The constructor object contains all info about the class's constructor including: number of parameters, type of parameters, etc.
- Class.getDeclaredConstructors() returns both public and non-public constructors, Class.getConstructors() returns only public constructors.
- Private methods and constructors are not accessible outside the class, using reflection we have access to all constructors be it private or public but to initialise object using private constructors we need to set accessible property to true for the constructor like: constructor.setAccessible(true);
- There are cases where we need to allow access to package-private classes from outside the package for reading and initialising.
  - Typically, when we want to use an external library to help us initialise those classes, the code in the external libraries is outside our package.
  - We need to do: packagePrivateClassConstructor.setAccessible(true), same like we did for initialising objects using private constructor of a class, outside of that class.
- Using java reflection, the parsing libraries are able to access different classes (despite those classes being package-private) so as to parse objects of those classes into/from from a json string.
- Another usecase is dependency injection, where classes in a package maintain a hierarchy, the DI class, which is part of another package, would need to have access those restricted package-private classes in order to create them and wire them up altogether.

#### Field Discovery

- Class.getDeclaredFields() - get all declared fields of a class. Includes all fields regardless of their access modifiers, and excludes the inherited fields.
- Class.getFields() - get all public fields of a class, includes the inherited fields.
- Java compiler generates artificial fields for internal usage. We don't see those fields unless we use reflection to discover them at runtime.
  - Synthetic fields and their names are compiler specific.
  - In most cases, we don't want to touch/modify on them.
  - To find out if a field is synthetic we check: Field.isSynthetic()
  - We can set fieldAccessible flag to true, this would grant java reflection access to restricted fields like package-private, protected or private fields: Field.setAccessible(true)
- Arrays are given a special treatment by Java reflection. They are special classes.
  - We can get basic information about arrays using Class<?> API, i.e. Class.isArray() and use Class.getComponentType() to get the type of array elements.

#### Modifier Discovery

- We can get modifiers for class like Class.getModifiers(), for example: String.class.getModifiers(). Same we can do for constructor, method and field as well.
- .getModifiers() returns an integer.
- Modifiers are encoded as bitmap, each modifier represents a single bit. Public modifier has rightmost bit set as 1, static modifier has 3rd bit from right set as 1. Public and static will have binary representation/value as Public | static = 9.
- We can use method from Modifiers class to check for particular modifier by using its helper functions like: Modifier.isStatic(int modifiers), Modifier.isFinal(int modifiers), etc.

#### Method Discovery

- All class methods are represented as object of type Method.
- class.getDeclaredMethods() - All methods declared in a class.
- class.getMethods() - All public methods and methods inherited from superclasses and implemented interfaces.
- method.getReturnType(), method.getParameterType(), method.getParameterCount(), method.getExceptionTypes(), etc. 
- Class.getMethod(String name, Class<?> ... paramTypes) looks for a public method in current or superclasses.
- Class.getDeclaredMethod(String name, Class<?> ... paramTypes) looks for a method (can have any modifier) only in current class.
- If a method with the name and parameter types (in that order) is not found a NoSuchMethodException is thrown.
- To invoke a method of an instance: Method.invoke(Object instance, Object ... args).
    - The parameter names should be of correct type in correct order.
    - instance can be passed as null incase of static method of a class.
    - In InvocationTargetException is thrown by this method incase any checked or unchecked exception is thrown while invoking the method, we can catch this exception and instead throw the exception it has wrapped.

#### Annotation Discovery

- We can carry the annotations into the JVM and access them at runtime using reflection.
- An annotation can carry annotation elements which carry additional information.
- Annotations can instruct and direct reflection code on - what targets to process and what to do with those targets, this way we can decouple our application code from the reflection code.
- @Retention meta-annotation takes a enum value of type RetentionPolicy that has 3 options:
    - SOURCE - Annotations are discarded by the compiler, example: @Override, @SuppressWarning, these are only useful at compile time.
    - CLASS - Annotations are recorded in the class file by the compiler after compilation but ignored by JVM at runtime, example: @AutoValue. This is the default as well.
    - RUNTIME - Annotations are recorded in the class file by the compiler and are retained by JVM at runtime, this is what we are going to use so that the custom annotation is available to us using java reflection.
- @Target meta-annotation is used to control on which target like method, class, etc could an annotation be applied.
- isAnnotationPresent() method exists for class, field, method, constructor and parameter, returns true if the target is annotated with the given annotation, we will discuss about other discovery methods later.
- The compiler replaces the repeated annotations with the container annotation after compilation. Example: @Role and @Roles.
- We should use getAnnotationByType(Annotation-Name.class) instead of getAnnotation(Annotation-Name.class) for repeatable annotations.


#### Comments on performance and safety of Java reflection:

- Reflection code is executed at runtime i.e. the constructor / method or any other thing which we are trying to access using reflection is resolved at runtime. At compile time, compiler has no knowledge about safety and validity of this operation.
- At runtime, for this: Product p = (Product) constructor.newInstance("game"), the JVM has to validate:
    - Constructor is accessible.
    - Parameter type matches argument type.
    - Object created is assignable to Product.
    - Hence, it is slower and less efficient compared to its statically compiled equivalent operations.

- Owing to those, since we delegate lot of things from compile-time to run-time, compile-time optimisation becomes unavailable to us.
- Code safety is a challenge in reflection since we wouldn't get any warning at compile-time.
- We can write safe reflection code by writing unit tests for our code, do defensive coding (exception handling) and keeping reflection code loosely coupled so that changes to the code it reflects on doesn't break reflection code.
