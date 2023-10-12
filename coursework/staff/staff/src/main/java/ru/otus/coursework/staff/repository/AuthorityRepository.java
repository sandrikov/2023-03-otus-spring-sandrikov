package ru.otus.coursework.staff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.otus.coursework.staff.domain.Authority;


public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	@SuppressWarnings("NullableProblems")
	@EntityGraph("authorities-user-entity-graph")
	@Override
	List<Authority> findAll();
}
