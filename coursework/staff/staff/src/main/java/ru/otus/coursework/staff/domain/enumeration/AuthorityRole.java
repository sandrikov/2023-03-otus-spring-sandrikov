package ru.otus.coursework.staff.domain.enumeration;

import lombok.Getter;

@Getter
public enum AuthorityRole {
	ADMIN,
	VIEW,
	EDIT,
	FIN,
	;

	private final String authority;

	AuthorityRole() {
		authority = "ROLE_" + name();
	}

}
