package ru.otus.coursework.staff.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lombok.val;

@DataJpaTest
class DepartmentRepositoryTest {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Test
	void findAll() {
		val all = departmentRepository.findAll();
		assertThat(all).isNotEmpty();
	}

	@Test
	void relationships() {
		val department = departmentRepository.findById("AD");
		assertThat(department).isPresent();
		val employees = department.get().getEmployees();
		assertThat(employees).isNotEmpty();
		val neenaYang = employees.stream().filter(e -> e.getEmail().equals("NYANG")).findFirst();
		assertThat(neenaYang).isPresent();
		val jobHistories = neenaYang.get().getJobHistories();
		assertThat(jobHistories).hasSize(2);
		val manager = department.get().getManager();
		assertThat(manager).isNotNull();
		assertEquals("SKING", manager.getEmail());
	}
}