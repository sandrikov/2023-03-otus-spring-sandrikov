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
import ru.otus.coursework.staff.domain.Department;

/**
 * A DTO for the {@link Department} entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto implements Serializable, Comparable<DepartmentDto> {

    @Serial
    private static final long serialVersionUID = -861781362989810525L;

    @NotBlank
    @Size(min = 2, max = 2)
    @Pattern(regexp = "^[A-Z]{2}$")
    private String departmentId;

    @NotBlank
    @Size(min = 1, max = 50)
    private String departmentName;

    private EmployeeDto manager;

    public DepartmentDto(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentDto departmentDto)) {
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

    @Override
    public int compareTo(DepartmentDto o) {
        return comparing(DepartmentDto::getDepartmentName)
                .thenComparing(DepartmentDto::getDepartmentId).compare(this, o);
    }
}
