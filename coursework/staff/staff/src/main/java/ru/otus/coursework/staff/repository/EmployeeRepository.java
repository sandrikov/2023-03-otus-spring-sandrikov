package ru.otus.coursework.staff.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import ru.otus.coursework.staff.domain.Employee;
import ru.otus.coursework.staff.repository.dto.EmployeeRestDto;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@Repository
@RepositoryRestResource(path = "employee", excerptProjection = EmployeeRestDto.class)
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@EntityGraph("employee-department-job-entity-graph")
	@Override
	List<Employee> findAll();

	@EntityGraph("employee-department-job-entity-graph")
	@Override
	List<Employee> findAll(Sort sort);

	@EntityGraph("employee-department-job-entity-graph")
	@Override
	Page<Employee> findAll(Pageable pageable);

	@EntityGraph("employee-department-job-entity-graph")
	List<Employee> findByManagerEmployeeIdOrderByFirstNameAscLastNameAsc(Long employeeId);

	boolean existsByEmail(String email);
}
