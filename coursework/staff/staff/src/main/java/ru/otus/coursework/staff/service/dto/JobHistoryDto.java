package ru.otus.coursework.staff.service.dto;

import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.coursework.staff.domain.JobHistory;

/**
 * A DTO for the {@link JobHistory} entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobHistoryDto  implements Serializable, Comparable<JobHistoryDto> {

    @Serial
    private static final long serialVersionUID = -3629376981142505350L;

    private Long jobHistoryId;

    private LocalDate startDate;

    private LocalDate endDate;

    private EmployeeDto employee;

    private DepartmentDto department;

    private JobDto job;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobHistoryDto jobHistoryDto)) {
            return false;
        }

        if (this.jobHistoryId == null) {
            return false;
        }
        return Objects.equals(this.jobHistoryId, jobHistoryDto.jobHistoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.jobHistoryId);
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

    @Override
    public int compareTo(JobHistoryDto o) {
        return comparing(JobHistoryDto::getEmployee)
                .thenComparing(JobHistoryDto::getStartDate)
                .thenComparingLong(JobHistoryDto::getJobHistoryId).compare(this, o);
    }
}
