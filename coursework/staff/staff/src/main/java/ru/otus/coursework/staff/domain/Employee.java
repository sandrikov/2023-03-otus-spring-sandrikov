package ru.otus.coursework.staff.domain;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.CascadeType.REFRESH;
import static jakarta.persistence.GenerationType.IDENTITY;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.rest.core.annotation.RestResource;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An Employee.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "employees")
@NamedEntityGraph(name = "employee-department-job-entity-graph",
		attributeNodes = {@NamedAttributeNode("department"), @NamedAttributeNode("job")})
public class Employee {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long employeeId;

	private String firstName;

	private String lastName;

	private String email;

	private String phoneNumber;

	private LocalDate hireDate;

	@RestResource(exported = false)
	private Long salary;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id")
	private Employee manager;

	@ManyToOne(cascade = REFRESH)
	@JoinColumn(name = "job_id")
	private Job job;

	@ManyToOne(cascade = REFRESH)
	@JoinColumn(name = "department_id")
	private Department department;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = ALL, orphanRemoval = true)
	@RestResource(exported = false)
	private Set<JobHistory> jobHistories = new HashSet<>();

	public void setJobHistories(Set<JobHistory> jobHistories) {
		if (this.jobHistories != null) {
			this.jobHistories.forEach(i -> i.setEmployee(null));
		}
		if (jobHistories != null) {
			jobHistories.forEach(i -> i.setEmployee(this));
		}
		this.jobHistories = jobHistories;
	}

	public void addJobHistory(JobHistory job) {
		this.jobHistories.add(job);
		job.setEmployee(this);
	}

	public void removeJobHistory(JobHistory job) {
		this.jobHistories.remove(job);
		job.setEmployee(null);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Employee)) {
			return false;
		}
		return employeeId != null && employeeId.equals(((Employee) o).employeeId);
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
				.append("id", employeeId)
				.append("firstName", firstName)
				.append("lastName", lastName)
				.append("email", email)
				.append("hireDate", hireDate)
				.toString();
	}

}
