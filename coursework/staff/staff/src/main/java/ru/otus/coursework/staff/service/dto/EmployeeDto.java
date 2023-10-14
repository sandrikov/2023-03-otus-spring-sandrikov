package ru.otus.coursework.staff.service.dto;

import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.coursework.staff.domain.Employee;

/**
 * A DTO for the {@link Employee} entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto implements Serializable, Comparable<EmployeeDto> {

	@Serial
	private static final long serialVersionUID = -7360605268752601993L;

	private Long employeeId;

	@NotBlank
	@Size(min = 1, max = 20)
	private String firstName;

	@NotBlank
	@Size(min = 1, max = 25)
	private String lastName;

	@NotBlank
	@Size(min = 1, max = 50)
	@Pattern(regexp = "^[\\w-\\.]+$")
	private String email;

	@Pattern(regexp = "^\\+[\\d \\(\\)-]+$")
	private String phoneNumber;

	private LocalDate hireDate;

	private Long salary;

	private EmployeeDto manager;

	private DepartmentDto department;

	private JobDto job;


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof EmployeeDto employeeDto)) {
			return false;
		}

		if (this.employeeId == null) {
			return false;
		}
		return Objects.equals(this.employeeId, employeeDto.employeeId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.employeeId);
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

	@Override
	public int compareTo(EmployeeDto o) {
		return comparing(EmployeeDto::getFirstName)
				.thenComparing(EmployeeDto::getLastName)
				.thenComparingLong(EmployeeDto::getEmployeeId).compare(this, o);
	}
}
