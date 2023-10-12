package ru.otus.coursework.staff.repository.dto;

import org.springframework.data.rest.core.config.Projection;

import ru.otus.coursework.staff.domain.Department;

/**
 * A DTO for the {@link ru.otus.coursework.staff.domain.Department} entity.
 */
@Projection(name = "department", types = { Department.class })
public interface DepartmentRestDto {

    String getDepartmentId();

    String getDepartmentName();

    EmployeeShortRestDto getManager();

}
