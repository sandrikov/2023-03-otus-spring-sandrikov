package ru.otus.coursework.staff.repository.dto;


import org.springframework.data.rest.core.config.Projection;

import ru.otus.coursework.staff.domain.Department;

@Projection(name = "department-short", types = { Department.class })
public interface DepartmentShortRestDto {

	String getDepartmentId();

	String getDepartmentName();
}
