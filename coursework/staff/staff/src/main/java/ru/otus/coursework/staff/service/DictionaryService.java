package ru.otus.coursework.staff.service;

import java.util.List;

import ru.otus.coursework.staff.domain.Department;
import ru.otus.coursework.staff.domain.Employee;
import ru.otus.coursework.staff.domain.Job;
import ru.otus.coursework.staff.service.dto.DepartmentDto;
import ru.otus.coursework.staff.service.dto.EmployeeDto;
import ru.otus.coursework.staff.service.dto.JobDto;

public interface DictionaryService {

	List<DepartmentDto> getDepartments();

	Department getDepartmentOrThrow(String id);

	List<EmployeeDto> getEmployees();

	Employee getEmployeeOrThrow(Long id);

	List<JobDto> getJobs();

	Job getJobOrThrow(String id);

}
