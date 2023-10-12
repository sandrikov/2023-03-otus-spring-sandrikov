package ru.otus.coursework.staff.service.impl;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.otus.coursework.staff.repository.JobRepository;
import ru.otus.coursework.staff.service.JobService;
import ru.otus.coursework.staff.service.dto.JobDto;
import ru.otus.coursework.staff.service.mapper.JobMapper;
import ru.otus.coursework.staff.service.misc.AppServiceException;

@RequiredArgsConstructor
@Service
public class JobServiceImpl implements JobService {
	private final JobRepository jobRepository;

	private final JobMapper jobMapper;

	@Override
	public List<JobDto> listAllJobs() {
		return jobMapper.toDto(jobRepository.findAll(Sort.by("jobId")));
	}

	@Override
	public JobDto getJob(String id) {
		return jobRepository.findById(id)
				.map(jobMapper::toDto)
				.orElseThrow(() -> new AppServiceException("Job not found for id : " + id));
	}

	@Transactional
	@Override
	public void modifyJob(JobDto jobDto) {
		val id = jobDto.getJobId();
		var job = jobRepository.findById(id)
				.orElseThrow(() -> new AppServiceException("Job not found for id : " + id));
		jobMapper.partialUpdate(job, jobDto);
		jobRepository.save(job);
	}

	@Transactional
	@Override
	public JobDto createJob(JobDto jobDto) {
		if (jobRepository.existsById(jobDto.getJobId())) {
			throw new AppServiceException("Job already exists. id : " + jobDto.getJobId());
		}
		val job = jobMapper.toEntity(jobDto);
		jobRepository.save(job);
		return jobMapper.toDto(job);
	}

	@Transactional
	@Override
	public void deleteJob(String id) {
		jobRepository.deleteById(id);
	}
}
