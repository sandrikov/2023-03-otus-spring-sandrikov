package ru.otus.coursework.staff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.otus.coursework.staff.domain.AppUser;


public interface AppUserRepository extends JpaRepository<AppUser, String> {
	@SuppressWarnings("NullableProblems")
	@EntityGraph("user-authorities-entity-graph")
	@Override
	List<AppUser> findAll();
}
