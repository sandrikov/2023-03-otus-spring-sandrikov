package ru.otus.coursework.staff.service.dto;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeWithHistoryDto extends EmployeeDto {
	@Serial
	private static final long serialVersionUID = 2865300534009657458L;

	private Set<JobHistoryDto> jobHistories = new HashSet<>();
}
