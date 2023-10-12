package ru.otus.coursework.staff.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.coursework.staff.domain.enumeration.AuthorityRole;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "authorities")
@NamedEntityGraph(name = "authorities-user-entity-graph",
		attributeNodes = {@NamedAttributeNode("user")})
public class Authority {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "userName")
	private AppUser user;

	@Enumerated(STRING)
	private AuthorityRole authorityRole;

	@Override
	public String toString() {
		return "Authority{'" + getUser().getUserName() +
				"' role='" + getAuthorityRole() + '\'' +
				'}';
	}
}
