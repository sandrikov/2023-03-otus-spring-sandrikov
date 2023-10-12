package ru.otus.coursework.staff.service;

import java.util.List;

import ru.otus.coursework.staff.service.dto.JobDto;

public interface JobService {
	List<JobDto> listAllJobs();

	JobDto getJob(String id);

	void modifyJob(JobDto jobDto);

	JobDto createJob(JobDto jobDto);

	void deleteJob(String id);
}
