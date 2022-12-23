package FieldReflection.FileParser;

import java.util.Arrays;

public class Test {
	String key1;
	String key2;
	int key3;
	String[] misc;

	@Override
	public String toString() {
		return "Test{" +
				"key1='" + key1 + '\'' +
				", key2='" + key2 + '\'' +
				", key3=" + key3 +
				", misc=" + Arrays.toString(misc) +
				'}';
	}
}
