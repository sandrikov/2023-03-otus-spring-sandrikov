package ru.otus.coursework.staff.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ru.otus.coursework.staff.misc.TestUtil;

class JobHistoryDtoTest {

	@Test
	void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(JobHistoryDto.class);
		JobHistoryDto jobHistoryDto1 = new JobHistoryDto();
		jobHistoryDto1.setJobHistoryId(1L);
		JobHistoryDto jobHistoryDto2 = new JobHistoryDto();
		assertThat(jobHistoryDto1).isNotEqualTo(jobHistoryDto2);
		jobHistoryDto2.setJobHistoryId(jobHistoryDto1.getJobHistoryId());
		assertThat(jobHistoryDto1).isEqualTo(jobHistoryDto2);
		jobHistoryDto2.setJobHistoryId(2L);
		assertThat(jobHistoryDto1).isNotEqualTo(jobHistoryDto2);
		jobHistoryDto1.setJobHistoryId(null);
		assertThat(jobHistoryDto1).isNotEqualTo(jobHistoryDto2);
	}
}
