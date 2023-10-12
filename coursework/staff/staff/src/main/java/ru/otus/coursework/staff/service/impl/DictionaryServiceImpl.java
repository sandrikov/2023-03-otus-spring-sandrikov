package ru.otus.coursework.staff.service.impl;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.coursework.staff.domain.Department;
import ru.otus.coursework.staff.domain.Employee;
import ru.otus.coursework.staff.domain.Job;
import ru.otus.coursework.staff.repository.DepartmentRepository;
import ru.otus.coursework.staff.repository.EmployeeRepository;
import ru.otus.coursework.staff.repository.JobRepository;
import ru.otus.coursework.staff.service.DictionaryService;
import ru.otus.coursework.staff.service.dto.DepartmentDto;
import ru.otus.coursework.staff.service.dto.EmployeeDto;
import ru.otus.coursework.staff.service.dto.JobDto;
import ru.otus.coursework.staff.service.mapper.DictionaryMapper;
import ru.otus.coursework.staff.service.misc.AppServiceException;

@RequiredArgsConstructor
@Service
public class DictionaryServiceImpl implements DictionaryService {
	private final DepartmentRepository departmentRepository;

	private final EmployeeRepository employeeRepository;

	private final JobRepository jobRepository;

	private final DictionaryMapper dictionaryMapper;

	@Override
	public List<DepartmentDto> getDepartments() {
		return departmentRepository.findAll(Sort.by("departmentName")).stream()
				.map(dictionaryMapper::toDtoDepartmentName).toList();
	}

	@Override
	public Department getDepartmentOrThrow(String id) {
		return departmentRepository.findById(id)
				.orElseThrow(() -> new AppServiceException("Department not found for id : " + id));
	}

	@Override
	public List<EmployeeDto> getEmployees() {
		return employeeRepository.findAll(Sort.by("firstName", "lastName")).stream()
				.map(dictionaryMapper::toDtoEmployeeFullName).toList();
	}

	@Override
	public Employee getEmployeeOrThrow(Long id) {
		return employeeRepository.findById(id)
				.orElseThrow(() -> new AppServiceException("Employee not found for id : " + id));
	}

	@Override
	public List<JobDto> getJobs() {
		return jobRepository.findAll(Sort.by("jobId")).stream()
				.map(dictionaryMapper::toDtoJobTitle).toList();
	}

	@Override
	public Job getJobOrThrow(String id) {
		return jobRepository.findById(id)
				.orElseThrow(() -> new AppServiceException("Job not found for id : " + id));
	}
}
