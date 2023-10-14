package ru.otus.coursework.staff.domain;

import static jakarta.persistence.CascadeType.ALL;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@NamedEntityGraph(name = "user-authorities-entity-graph",
		attributeNodes = {@NamedAttributeNode("authorities")})
public class AppUser {

	@Id
	private String userName;

	private String password;

	private boolean enabled;

	@OneToMany(cascade = ALL, orphanRemoval = true, mappedBy = "user", fetch = FetchType.EAGER)
	private List<Authority> authorities;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AppUser)) {
			return false;
		}
		return userName != null && userName.equals(((AppUser) o).userName);
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "User{'" + userName + "'}";
	}
}
