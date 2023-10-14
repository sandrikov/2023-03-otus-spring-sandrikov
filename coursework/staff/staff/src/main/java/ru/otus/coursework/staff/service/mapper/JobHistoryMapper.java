package ru.otus.coursework.staff.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import ru.otus.coursework.staff.domain.JobHistory;
import ru.otus.coursework.staff.service.dto.JobHistoryDto;

/**
 * Mapper for the entity {@link JobHistory} and its DTO {@link JobHistoryDto}.
 */
@Mapper(componentModel = SPRING, uses = DictionaryMapper.class)
public interface JobHistoryMapper extends EntityMapper<JobHistoryDto, JobHistory> {

	@Named("jobHistoryToDto")
	@Override
	@Mapping(target = "employee", source = "employee", qualifiedByName = "employeeFullName")
	@Mapping(target = "job", source = "job", qualifiedByName = "jobTitle")
	@Mapping(target = "department", source = "department", qualifiedByName = "departmentName")
	JobHistoryDto toDto(JobHistory entity);

	@Override
	@Mapping(target = "employee.jobHistories", ignore = true)
	@Mapping(target = "employee.manager", ignore = true)
	@Mapping(target = "employee.department", ignore = true)
	@Mapping(target = "department.employees", ignore = true)
	@Mapping(target = "department.manager", ignore = true)
	JobHistory toEntity(JobHistoryDto dto);

	@Override
	@Mapping(target = "employee.jobHistories", ignore = true)
	@Mapping(target = "employee.manager", ignore = true)
	@Mapping(target = "employee.department", ignore = true)
	@Mapping(target = "department.employees", ignore = true)
	@Mapping(target = "department.manager", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void partialUpdate(@MappingTarget JobHistory entity, JobHistoryDto dto);
}
