package ru.otus.coursework.staff.repository.dto;

import org.springframework.data.rest.core.config.Projection;

import ru.otus.coursework.staff.domain.Job;

/**
 * A DTO for the {@link ru.otus.coursework.staff.domain.Job} entity.
 */
@SuppressWarnings("unused")
@Projection(name = "job", types = { Job.class })
public interface JobRestDto {

    String getJobId();

    String getJobTitle();

}
