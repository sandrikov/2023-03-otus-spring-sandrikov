package ru.otus.coursework.staff.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import lombok.val;
import ru.otus.coursework.staff.domain.Department;
import ru.otus.coursework.staff.domain.Employee;
import ru.otus.coursework.staff.domain.Job;
import ru.otus.coursework.staff.service.dto.DepartmentDto;
import ru.otus.coursework.staff.service.dto.EmployeeDto;
import ru.otus.coursework.staff.service.dto.JobDto;

@Mapper(componentModel = SPRING)
public interface DictionaryMapper {

	@Named("jobTitle")
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "jobId", source = "jobId")
	@Mapping(target = "jobTitle", source = "jobTitle")
	JobDto toDtoJobTitle(Job entity);

	@Named("employeeFullName")
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "employeeId", source = "employeeId")
	@Mapping(target = "firstName", source = "firstName")
	@Mapping(target = "lastName", source = "lastName")
	EmployeeDto toDtoEmployeeFullName(Employee employee);

	@Named("departmentName")
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "departmentId", source = "departmentId")
	@Mapping(target = "departmentName", source = "departmentName")
	DepartmentDto toDtoDepartmentName(Department entity);

	@Named("jobFromId")
	default Job jobFromId(String id) {
		if (id == null) {
			return null;
		}
		val job = new Job();
		job.setJobId(id);
		return job;
	}

	@Named("getJobId")
	String getJobId(EmployeeDto employeeDto);

	@Named("employeeFromId")
	default Employee employeeFromId(Long id) {
		if (id == null) {
			return null;
		}
		val employee = new Employee();
		employee.setEmployeeId(id);
		return employee;
	}

	@Named("departmentFromId")
	default Department departmentFromId(String id) {
		if (id == null) {
			return null;
		}
		val department = new Department();
		department.setDepartmentId(id);
		return department;
	}

}
