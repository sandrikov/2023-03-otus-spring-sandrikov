package ru.otus.coursework.staff.service;

import java.util.List;

import ru.otus.coursework.staff.service.dto.DepartmentDetailsDto;
import ru.otus.coursework.staff.service.dto.DepartmentDto;

public interface DepartmentService {

	List<DepartmentDto> listAllDepartments();

	List<DepartmentDto> getSubordinateDepartments(Long employeeId);

	DepartmentDetailsDto getDepartment(String id);

	void modifyDepartment(DepartmentDto departmentDto);

	DepartmentDto createDepartment(DepartmentDto departmentDto);

	void deleteDepartment(String id);

	void removeFromSubordination(Long employeeId);
}
