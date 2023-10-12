package ru.otus.coursework.staff.repository.dto;


import org.springframework.data.rest.core.config.Projection;

import ru.otus.coursework.staff.domain.Employee;

@Projection(name = "employee-short", types = { Employee.class })
public interface EmployeeShortRestDto {
	Long getEmployeeId();

	String getFirstName();

	String getLastName();
}
