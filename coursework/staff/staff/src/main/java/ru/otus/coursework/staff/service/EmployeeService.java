package ru.otus.coursework.staff.service;

import java.util.List;

import ru.otus.coursework.staff.service.dto.EmployeeDto;
import ru.otus.coursework.staff.service.dto.EmployeeWithHistoryDto;

public interface EmployeeService {

	EmployeeWithHistoryDto getEmployee(Long id);

	void createEmployees(EmployeeDto employeeDto);

	void modifyEmployees(EmployeeDto employeeDto);

	List<EmployeeDto> getSubordinateEmployees(Long employeeId);

	void deleteEmployee(Long id);
}
