package FieldReflection;

import java.lang.reflect.Field;

public class Main {
	public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException {
		printDeclaredFields(Movie.MovieStats.class);
		printDeclaredFields(Category.class);
		// System.out.println(Arrays.stream(Category.values()).collect(Collectors.toList()));

		Movie movie = new Movie("A", 2, true, Category.ADVENTURE, 200);
		getFieldValues(movie.getClass(), movie);
	}

	private static <T> void getFieldValues(Class<? extends T> clazz, T instance) throws IllegalAccessException {
		for (Field field : clazz.getDeclaredFields()) {
			System.out.println(String.format("Field name: %s, type: %s, isSynthetic: %s", field.getName(), field.getType(), field.isSynthetic()));
			// Returns the value of the field represented by this Field, on the specified object.
			System.out.println(String.format("Field value is: %s", field.get(instance)));
			System.out.println();
		}
	}

	private static void printDeclaredFields(Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			System.out.println(String.format("Field name: %s, type: %s, isSynthetic: %s", field.getName(), field.getType(), field.isSynthetic()));
			System.out.println();
		}
	}

	static class Movie extends Product {
		static final double INIT_PRICE = 100;
		private boolean isReleased;
		private Category category;
		private double actualPrice;

		public Movie(String name, int year, boolean isReleased, Category category, double actualPrice) {
			super(name, year);
			this.isReleased = isReleased;
			this.category = category;
			this.actualPrice = actualPrice;
		}

		public class MovieStats {
			private double timesWatched;

			public MovieStats(double timesWatched) {
				this.timesWatched = timesWatched;
			}
		}
	}

	static class Product {
		protected String name;
		protected int year;
		protected double actualPrice;

		public Product(String name, int year) {
			this.name = name;
			this.year = year;
		}
	}

	public enum Category {
		ADVENTURE,
		ACTION,
		COMEDY
	}
}
