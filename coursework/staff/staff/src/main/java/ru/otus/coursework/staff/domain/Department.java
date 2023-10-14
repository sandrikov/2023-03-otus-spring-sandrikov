package ru.otus.coursework.staff.domain;

import static jakarta.persistence.FetchType.LAZY;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.rest.core.annotation.RestResource;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A Department.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "departments")
@NamedEntityGraph(name = "department-manager-entity-graph", attributeNodes = @NamedAttributeNode("manager"))
public class Department {

	@Id
	@Setter
	private String departmentId;

	@Setter
	private String departmentName;

	@JoinColumn(name = "manager_id")
	@Setter
	@ManyToOne(fetch = LAZY)
	private Employee manager;

	@RestResource(exported = false)
	@OneToMany(fetch = LAZY, mappedBy = "department")
	private Set<Employee> employees = new HashSet<>();

	public void setEmployees(Set<Employee> employees) {
		if (this.employees != null) {
			this.employees.forEach(i -> i.setDepartment(null));
		}
		if (employees != null) {
			employees.forEach(i -> i.setDepartment(this));
		}
		this.employees = employees;
	}

	public void addEmployee(Employee employee) {
		this.employees.add(employee);
		employee.setDepartment(this);
	}

	public void removeEmployee(Employee employee) {
		this.employees.remove(employee);
		employee.setDepartment(null);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Department)) {
			return false;
		}
		return departmentId != null && departmentId.equals(((Department) o).departmentId);
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
				.append("id", departmentId)
				.append("name", departmentName)
				.toString();
	}
}
