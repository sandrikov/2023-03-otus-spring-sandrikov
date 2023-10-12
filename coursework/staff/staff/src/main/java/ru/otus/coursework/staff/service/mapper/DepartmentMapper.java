package ru.otus.coursework.staff.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import lombok.val;
import ru.otus.coursework.staff.domain.Department;
import ru.otus.coursework.staff.domain.Employee;
import ru.otus.coursework.staff.service.dto.DepartmentDetailsDto;
import ru.otus.coursework.staff.service.dto.DepartmentDto;
import ru.otus.coursework.staff.service.dto.EmployeeDto;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDto}.
 */
@Mapper(componentModel = SPRING, uses = DictionaryMapper.class)
public interface DepartmentMapper extends EntityMapper<DepartmentDto, Department> {

	@Override
	@Mapping(target = "manager", source = "manager", qualifiedByName = "employeeFullName")
	DepartmentDto toDto(Department s);

	@Override
	@Mapping(target = "employees", ignore = true)
	@Mapping(target = "manager", source = "manager.employeeId", qualifiedByName = "employeeFromId")
	Department toEntity(DepartmentDto dto);

	@Named("toEntityEmployeeDto")
	@Mapping(target = "manager", ignore = true)
	@Mapping(target = "department", ignore = true)
	@Mapping(target = "job", ignore = true)
	@Mapping(target = "jobHistories", ignore = true)
	Employee toEntityEmployeeDto(EmployeeDto dto);

	@Override
	@Mapping(target = "employees", ignore = true)
	@Mapping(target = "manager", qualifiedByName = "updateManager",
			conditionExpression = "java(isManagerChanged(entity.getManager(), dto.getManager()))")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void partialUpdate(@MappingTarget Department entity, DepartmentDto dto);

	default boolean isManagerChanged(Employee entity, EmployeeDto dto) {
		return dto != null && (entity == null || !entity.getEmployeeId().equals(dto.getEmployeeId()));
	}

	@Named("updateManager")
	default Employee updateManager(EmployeeDto dto) {
		if (dto == null) {
			return null;
		}
		return employeeFromId(dto.getEmployeeId());
	}

	default Employee employeeFromId(Long id) {
		if (id == null) {
			return null;
		}
		val employee = new Employee();
		employee.setEmployeeId(id);
		return employee;
	}

	@Mapping(target = "employees", source = "employees", qualifiedByName = "employeeDetails")
	@Mapping(target = "manager", source = "manager", qualifiedByName = "employeeDetails")
	DepartmentDetailsDto toDetailsDto(Department s);

	@Named("employeeDetails")
	@Mapping(target = "department", ignore = true)
	@Mapping(target = "salary", ignore = true)
	@Mapping(target = "manager", ignore = true)
	@Mapping(target = "job", source = "job", qualifiedByName = "jobTitle")
	EmployeeDto toDtoEmployeeDetails(Employee employee);
}
