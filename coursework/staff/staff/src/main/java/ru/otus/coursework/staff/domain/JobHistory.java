package ru.otus.coursework.staff.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.time.LocalDate;

import org.apache.commons.lang3.builder.ToStringBuilder;

import jakarta.persistence.Entity;
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

/**
 * A JobHistory.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "job_history")
@NamedEntityGraph(name = "job_history-department-job-employee-entity-graph",
		attributeNodes = {@NamedAttributeNode("department"),
				@NamedAttributeNode("job"), @NamedAttributeNode("employee")})
public class JobHistory {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long jobHistoryId;

	private LocalDate startDate;

	private LocalDate endDate;

	@ManyToOne
	@JoinColumn(name = "job_id")
	private Job job;

	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof JobHistory)) {
			return false;
		}
		return jobHistoryId != null && jobHistoryId.equals(((JobHistory) o).jobHistoryId);
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
				.append("id", jobHistoryId)
				.append("employee", employee)
				.append("start", startDate)
				.append("end", endDate)
				.append("job", job)
				.append("dep", department)
				.toString();
	}

}
