package ru.otus.coursework.staff.service.dto;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.coursework.staff.domain.Department;

/**
 * A DTO for the {@link Department} entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDetailsDto implements Serializable {

	@Serial
	private static final long serialVersionUID = 7814112902887549396L;

	private String departmentId;

	private String departmentName;

	private EmployeeDto manager;

	private Set<EmployeeDto> employees;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DepartmentDetailsDto departmentDto)) {
			return false;
		}

		if (this.departmentId == null) {
			return false;
		}
		return Objects.equals(this.departmentId, departmentDto.departmentId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.departmentId);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
				.append("id", departmentId)
				.append("name", departmentName)
				.toString();
	}
}
