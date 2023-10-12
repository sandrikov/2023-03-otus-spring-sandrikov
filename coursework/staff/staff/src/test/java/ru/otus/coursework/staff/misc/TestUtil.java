package ru.otus.coursework.staff.misc;

import static org.assertj.core.api.Assertions.assertThat;

public final class TestUtil {

	/**
	 * Verifies the equals/hashcode contract on the domain object.
	 */
	public static <T> void equalsVerifier(Class<T> clazz) throws Exception {
		T domainObject1 = clazz.getConstructor().newInstance();
		assertThat(domainObject1.toString()).isNotNull();
		assertThat(domainObject1).hasSameHashCodeAs(domainObject1);
		// Test with an instance of another class
		Object testOtherObject = new Object();
		assertThat(domainObject1).isNotEqualTo(testOtherObject).isNotNull();
		// Test with an instance of the same class
		T domainObject2 = clazz.getConstructor().newInstance();
		assertThat(domainObject1).isNotEqualTo(domainObject2)
				// HashCodes are equals because the objects are not persisted yet
				.hasSameHashCodeAs(domainObject2);
	}
}
