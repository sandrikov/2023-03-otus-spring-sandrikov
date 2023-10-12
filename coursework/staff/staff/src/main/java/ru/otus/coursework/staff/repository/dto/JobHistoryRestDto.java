package ru.otus.coursework.staff.repository.dto;

import java.time.LocalDate;

import org.springframework.data.rest.core.config.Projection;

import ru.otus.coursework.staff.domain.JobHistory;

/**
 * A DTO for the {@link ru.otus.coursework.staff.domain.JobHistory} entity.
 */
@Projection(name = "job-history", types = { JobHistory.class })
public interface JobHistoryRestDto {

    Long getJobHistoryId();

    LocalDate getStartDate();

    LocalDate getEndDate();

    JobRestDto getJob();

    DepartmentShortRestDto getDepartment();

    EmployeeShortRestDto getEmployee();

}
