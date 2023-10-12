package ru.otus.coursework.staff.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lombok.val;

@DataJpaTest
class JobHistoryRepositoryTest {

	@Autowired
	private JobHistoryRepository jobHistoryRepository;

	@Test
	void findAll() {
		val all = jobHistoryRepository.findAll();
		assertThat(all).isNotEmpty();
	}
}