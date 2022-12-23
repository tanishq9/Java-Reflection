package FieldReflection.Assignment.data;

public class Company {
	String name;
	// String address;
	int size;
	Address address;
	String[] misc;

	/*public Company(String name, String address, int size) {
		this.name = name;
		this.address = address;
		this.size = size;
	}*/

	public Company(String name, Address address, int size, String[] misc) {
		this.name = name;
		this.address = address;
		this.size = size;
		this.misc = misc;
	}
}
