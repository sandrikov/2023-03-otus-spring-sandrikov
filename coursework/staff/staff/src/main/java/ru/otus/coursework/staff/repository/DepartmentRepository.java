package ru.otus.coursework.staff.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import ru.otus.coursework.staff.domain.Department;
import ru.otus.coursework.staff.repository.dto.DepartmentRestDto;

/**
 * Spring Data JPA repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
@RepositoryRestResource(path = "department", excerptProjection = DepartmentRestDto.class)
public interface DepartmentRepository extends JpaRepository<Department, String> {


	@EntityGraph("department-manager-entity-graph")
	@Override
	Optional<Department> findById(String id);

	@EntityGraph("department-manager-entity-graph")
	@Override
	List<Department> findAll();

	@EntityGraph("department-manager-entity-graph")
	@Override
	List<Department> findAll(Sort sort);

	@EntityGraph("department-manager-entity-graph")
	@Override
	Page<Department> findAll(Pageable pageable);

	List<Department> findByManagerEmployeeIdOrderByDepartmentName(Long managerId);
}
