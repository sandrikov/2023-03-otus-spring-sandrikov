package ru.otus.coursework.staff.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ru.otus.coursework.staff.misc.TestUtil;

class DepartmentDetailsDtoTest {
	@Test
	void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(DepartmentDetailsDto.class);
		DepartmentDetailsDto departmentDto1 = new DepartmentDetailsDto();
		departmentDto1.setDepartmentId("XX");
		DepartmentDetailsDto departmentDto2 = new DepartmentDetailsDto();
		assertThat(departmentDto1).isNotEqualTo(departmentDto2);
		departmentDto2.setDepartmentId(departmentDto1.getDepartmentId());
		assertThat(departmentDto1).isEqualTo(departmentDto2);
		departmentDto2.setDepartmentId("YY");
		assertThat(departmentDto1).isNotEqualTo(departmentDto2);
		departmentDto1.setDepartmentId(null);
		assertThat(departmentDto1).isNotEqualTo(departmentDto2);
	}
}