package ru.otus.coursework.staff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import ru.otus.coursework.staff.domain.JobHistory;
import ru.otus.coursework.staff.repository.dto.JobHistoryRestDto;

/**
 * Spring Data JPA repository for the JobHistory entity.
 */
@Repository
@RepositoryRestResource(path = "history", excerptProjection = JobHistoryRestDto.class)
public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {

	@EntityGraph("job_history-department-job-employee-entity-graph")
	@Override
	List<JobHistory> findAll();

}
