package ru.otus.coursework.staff.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.coursework.staff.domain.enumeration.AuthorityRole.ADMIN;
import static ru.otus.coursework.staff.domain.enumeration.AuthorityRole.EDIT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lombok.val;
import ru.otus.coursework.staff.domain.Authority;

@DataJpaTest
class AppUserRepositoryTest {

	@Autowired
	private AppUserRepository appUserRepository;

	@Test
	void findAll() {
		val all = appUserRepository.findAll();
		assertThat(all).isNotEmpty();
	}


	@Test
	void relationships() {
		val user = appUserRepository.findById("admin");
		assertThat(user).isPresent();
		val authorities = user.get().getAuthorities();
		assertThat(authorities).isNotEmpty()
				.map(Authority::getAuthorityRole).containsExactly(ADMIN, EDIT);
	}

}