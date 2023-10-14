package ru.otus.coursework.staff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import ru.otus.coursework.staff.domain.Job;
import ru.otus.coursework.staff.repository.dto.JobRestDto;

/**
 * Spring Data JPA repository for the Job entity.
 */
@Repository
@RepositoryRestResource(path = "job", excerptProjection = JobRestDto.class)
public interface JobRepository extends JpaRepository<Job, String> {
}
