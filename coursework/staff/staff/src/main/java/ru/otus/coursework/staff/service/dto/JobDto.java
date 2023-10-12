package ru.otus.coursework.staff.service.dto;

import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.coursework.staff.domain.Job;

/**
 * A DTO for the {@link Job} entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDto implements Serializable, Comparable<JobDto> {
	@Serial
	private static final long serialVersionUID = 4584093489305724907L;

	@NotBlank
	@Size(min = 2, max = 10)
	@Pattern(regexp = "^[A-Z][A-Z_]+$")
	private String jobId;

	@NotBlank
	@Size(min = 1, max = 50)
	private String jobTitle;

	private Long minSalary;

	private Long maxSalary;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof JobDto jobDto)) {
			return false;
		}

		if (this.jobId == null) {
			return false;
		}
		return Objects.equals(this.jobId, jobDto.jobId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.jobId);
	}


	@Override
	public String toString() {
		return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
				.append("id", jobId)
				.append("title", jobTitle)
				.toString();
	}

	@Override
	public int compareTo(JobDto o) {
		return comparing(JobDto::getJobTitle)
				.thenComparing(JobDto::getJobId).compare(this, o);
	}
}
