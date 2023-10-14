package ru.otus.coursework.staff.service.mapper;

import static org.apache.logging.log4j.LogManager.getLogger;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import java.time.LocalDate;
import java.util.Comparator;

import org.apache.logging.log4j.Logger;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import lombok.val;
import ru.otus.coursework.staff.domain.Department;
import ru.otus.coursework.staff.domain.Employee;
import ru.otus.coursework.staff.domain.JobHistory;
import ru.otus.coursework.staff.service.dto.EmployeeDto;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDto}.
 */
@Mapper(componentModel = SPRING, uses = DictionaryMapper.class)
public interface EmployeeMapper extends EntityMapper<EmployeeDto, Employee> {
    Logger LOGGER = getLogger(EmployeeMapper.class);

    @Override
    @Mapping(target = "manager", source = "manager", qualifiedByName = "employeeFullName")
    @Mapping(target = "job", source = "job", qualifiedByName = "jobTitle")
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentName")
    EmployeeDto toDto(Employee s);

    @Override
    @Mapping(target = "jobHistories", ignore = true)
    @Mapping(target = "job", source = "job.jobId", qualifiedByName = "jobFromId")
    @Mapping(target = "department", source = "department.departmentId", qualifiedByName = "departmentFromId")
    @Mapping(target = "manager", source = "manager.employeeId", qualifiedByName = "employeeFromId")
    Employee toEntity(EmployeeDto dto);

    @Override
    @Mapping(target = "jobHistories", ignore = true)
    @Mapping(target = "job", source = "job.jobId", qualifiedByName = "jobFromId",
            conditionExpression = "java(isJobChanged(entity, dto))")
    @Mapping(target = "department", expression = "java(updateDepartment(entity, dto))")
    @Mapping(target = "manager", source = "manager.employeeId", qualifiedByName = "employeeFromId",
            conditionExpression = "java(isManagerChanged(entity, dto))")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Employee entity, EmployeeDto dto);

    default Department updateDepartment(Employee entity, EmployeeDto dto) {
        if (!isDepartmentChanged(entity, dto)) {
            return entity.getDepartment();
        }
        if (entity.getDepartment() != null) {
            entity.getDepartment().removeEmployee(entity);
        }
        val department = new Department();
        department.setDepartmentId(dto.getDepartment().getDepartmentId());
        department.addEmployee(entity);
        return department;
    }

    default void modifyEmployees(Employee employee, EmployeeDto employeeDto, LocalDate endDate) {
        val jobHistory = prepareJobHistory(employee, employeeDto, endDate);
        partialUpdate(employee, employeeDto);
        if (jobHistory != null) {
            employee.addJobHistory(jobHistory);
        }
    }

    private JobHistory prepareJobHistory(Employee employee, EmployeeDto employeeDto, LocalDate endDate) {
        if (!isJobChanged(employee, employeeDto) && !isDepartmentChanged(employee, employeeDto)) {
            return null;
        }
        val jobHistoryLast = employee.getJobHistories().stream()
                .max(Comparator.comparing(JobHistory::getEndDate));
        var startDate = jobHistoryLast.map(JobHistory::getEndDate).orElse(employee.getHireDate());
        if (endDate.isAfter(startDate)) {
            return new JobHistory(null, startDate, endDate,
                    employee.getJob(), employee.getDepartment(), employee);
        }
        LOGGER.warn("Skip make JobHistory, because endDate:" + startDate +
                " is not after startDate:" + startDate + ".\n" +
                "previous: " + jobHistoryLast.map(JobHistory::toString).orElse("none"));
        return null;
    }


    default boolean isManagerChanged(Employee entity, EmployeeDto dto) {
        return dto.getManager() != null && (entity.getManager() == null
                || !entity.getManager().getEmployeeId().equals(dto.getManager().getEmployeeId()));
    }

    default boolean isJobChanged(Employee entity, EmployeeDto dto) {
        return dto.getJob() != null && (entity.getJob() == null
                || !entity.getJob().getJobId().equals(dto.getJob().getJobId()));
    }

    default boolean isDepartmentChanged(Employee entity, EmployeeDto dto) {
        return dto.getDepartment() != null && (entity.getDepartment() == null
                || !entity.getDepartment().getDepartmentId().equals(dto.getDepartment().getDepartmentId()));
    }
}
