package ru.otus.coursework.staff.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.otus.coursework.staff.domain.Employee;
import ru.otus.coursework.staff.service.dto.EmployeeWithHistoryDto;

@Mapper(componentModel = SPRING, uses = {DictionaryMapper.class, JobHistoryMapper.class})
public interface EmployeeWithHistoryMapper {

	@SuppressWarnings("checkstyle:LineLength")
	@Mapping(target = "manager", source = "manager", qualifiedByName = "employeeFullName")
	@Mapping(target = "job", source = "job", qualifiedByName = "jobTitle")
	@Mapping(target = "department", source = "department", qualifiedByName = "departmentName")
	@Mapping(target = "jobHistories", source = "jobHistories", qualifiedByName = "jobHistoryToDto")
	EmployeeWithHistoryDto toDto(Employee s);

}
