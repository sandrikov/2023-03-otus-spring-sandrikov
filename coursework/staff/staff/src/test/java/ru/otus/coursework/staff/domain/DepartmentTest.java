package ru.otus.coursework.staff.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ru.otus.coursework.staff.misc.TestUtil;

class DepartmentTest {

	@Test
	void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Department.class);
		Department department1 = new Department();
		department1.setDepartmentId("XX");
		Department department2 = new Department();
		department2.setDepartmentId(department1.getDepartmentId());
		assertThat(department1).isEqualTo(department2);
		department2.setDepartmentId("YY");
		assertThat(department1).isNotEqualTo(department2);
		department1.setDepartmentId(null);
		assertThat(department1).isNotEqualTo(department2);
	}
}