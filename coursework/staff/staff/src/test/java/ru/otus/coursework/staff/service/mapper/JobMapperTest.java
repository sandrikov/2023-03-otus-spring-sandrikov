package ru.otus.coursework.staff.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.val;
import ru.otus.coursework.staff.domain.Job;
import ru.otus.coursework.staff.service.dto.JobDto;

@SpringBootTest(classes = {JobMapperImpl.class, DictionaryMapperImpl.class})
class JobMapperTest {

	@Autowired
	private JobMapper mapper;

	private Job entity;

	private JobDto dto;

	@BeforeEach
	void setUp() {
		entity = new Job("AD_PRES", "President", 20080L, 40000L);
		dto = new JobDto(entity.getJobId(), entity.getJobTitle(), entity.getMinSalary(), entity.getMaxSalary());
	}

	@Test
	void toDto() {
		val jobDto = mapper.toDto(entity);
		assertThat(jobDto).isNotNull()
				.isEqualTo(dto) // compare only by jobId
				.usingRecursiveComparison()
				.isEqualTo(dto);
	}

	@Test
	void toEntity() {
		val job = mapper.toEntity(dto);
		assertThat(job).isNotNull()
				.isEqualTo(entity) // compare only by jobId
				.usingRecursiveComparison()
				.isEqualTo(entity);
	}

	@Test
	void partialUpdate() {
		String id = "AD_VP";
		String jobTitle = "Administration Vice President";
		Long minSalary = 15000L;
		Long maxSalary = 30000L;
		mapper.partialUpdate(entity, new JobDto(id, null, null, null));
		assertEquals(entity.getJobId(), id);
		assertEquals(entity.getJobTitle(), dto.getJobTitle());
		assertEquals(entity.getMinSalary(), dto.getMinSalary());
		assertEquals(entity.getMaxSalary(), dto.getMaxSalary());
		mapper.partialUpdate(entity, new JobDto(null, jobTitle, minSalary, null));
		assertEquals(entity.getJobId(), id);
		assertEquals(entity.getJobTitle(), jobTitle);
		assertEquals(entity.getMinSalary(), minSalary);
		assertEquals(entity.getMaxSalary(), dto.getMaxSalary());
		mapper.partialUpdate(entity, new JobDto(null, null, null, maxSalary));
		assertEquals(entity.getJobId(), id);
		assertEquals(entity.getJobTitle(), jobTitle);
		assertEquals(entity.getMinSalary(), minSalary);
		assertEquals(entity.getMaxSalary(), maxSalary);
	}
}
