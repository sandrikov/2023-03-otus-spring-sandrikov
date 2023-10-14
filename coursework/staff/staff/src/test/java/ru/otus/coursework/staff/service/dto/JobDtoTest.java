package ru.otus.coursework.staff.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ru.otus.coursework.staff.misc.TestUtil;

class JobDtoTest {

	@Test
	void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(JobDto.class);
		JobDto projectDto1 = new JobDto();
		projectDto1.setJobId("XX_XXX");
		JobDto projectDto2 = new JobDto();
		assertThat(projectDto1).isNotEqualTo(projectDto2);
		projectDto2.setJobId(projectDto1.getJobId());
		assertThat(projectDto1).isEqualTo(projectDto2);
		projectDto2.setJobId("YY_YYY");
		assertThat(projectDto1).isNotEqualTo(projectDto2);
		projectDto1.setJobId(null);
		assertThat(projectDto1).isNotEqualTo(projectDto2);
	}
}