package ru.otus.coursework.staff.repository.dto;

import java.time.LocalDate;

import org.springframework.data.rest.core.config.Projection;

import ru.otus.coursework.staff.domain.Employee;

/**
 * A DTO for the {@link ru.otus.coursework.staff.domain.Employee} entity.
 */
@Projection(name = "employee", types = { Employee.class })
public interface EmployeeRestDto {

    Long getEmployeeId();

    String getFirstName();

    String getLastName();

    String getEmail();

    String getPhoneNumber();

    LocalDate getHireDate();

    EmployeeShortRestDto getManager();

    DepartmentShortRestDto getDepartment();

    JobRestDto getJob();

}
