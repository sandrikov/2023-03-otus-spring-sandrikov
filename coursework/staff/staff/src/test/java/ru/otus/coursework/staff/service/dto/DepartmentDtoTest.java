package ru.otus.coursework.staff.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ru.otus.coursework.staff.misc.TestUtil;


class DepartmentDtoTest {

	@Test
	void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(DepartmentDto.class);
		DepartmentDto departmentDto1 = new DepartmentDto();
		departmentDto1.setDepartmentId("XX");
		DepartmentDto departmentDto2 = new DepartmentDto();
		assertThat(departmentDto1).isNotEqualTo(departmentDto2);
		departmentDto2.setDepartmentId(departmentDto1.getDepartmentId());
		assertThat(departmentDto1).isEqualTo(departmentDto2);
		departmentDto2.setDepartmentId("YY");
		assertThat(departmentDto1).isNotEqualTo(departmentDto2);
		departmentDto1.setDepartmentId(null);
		assertThat(departmentDto1).isNotEqualTo(departmentDto2);
	}
}
