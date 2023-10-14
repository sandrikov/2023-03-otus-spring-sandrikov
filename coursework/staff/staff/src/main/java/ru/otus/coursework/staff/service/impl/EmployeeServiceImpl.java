package ru.otus.coursework.staff.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.otus.coursework.staff.domain.Employee;
import ru.otus.coursework.staff.repository.EmployeeRepository;
import ru.otus.coursework.staff.service.DepartmentService;
import ru.otus.coursework.staff.service.EmployeeService;
import ru.otus.coursework.staff.service.dto.EmployeeDto;
import ru.otus.coursework.staff.service.dto.EmployeeWithHistoryDto;
import ru.otus.coursework.staff.service.mapper.EmployeeMapper;
import ru.otus.coursework.staff.service.mapper.EmployeeWithHistoryMapper;
import ru.otus.coursework.staff.service.misc.AppServiceException;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;

	private final DepartmentService departmentService;

	private final EmployeeMapper employeeMapper;

	private final EmployeeWithHistoryMapper employeeWithHistoryMapper;

	@Override
	public EmployeeWithHistoryDto getEmployee(Long id) {
		return employeeWithHistoryMapper.toDto(getEmployeeOrThrow(id));
	}

	@Transactional
	@Override
	public void createEmployees(EmployeeDto employeeDto) {
		if (employeeRepository.existsByEmail(employeeDto.getEmail())) {
			throw new AppServiceException("Email is already in use! e-mail : "
					+ employeeDto.getEmail());
		}
		val employee = employeeMapper.toEntity(employeeDto);
		employeeRepository.save(employee);
	}

	@Override
	public void modifyEmployees(EmployeeDto employeeDto) {
		val id = employeeDto.getEmployeeId();
		final var employee = getEmployeeOrThrow(id);
		if (!Objects.equals(employeeDto.getEmail(), employee.getEmail())
				&& employeeRepository.existsByEmail(employeeDto.getEmail())) {
			throw new AppServiceException("Email is already in use! e-mail : " + employeeDto.getEmail());
		}
		employeeMapper.modifyEmployees(employee, employeeDto, LocalDate.now());
		employeeRepository.save(employee);
	}

	@Transactional
	@Override
	public void deleteEmployee(Long id) {
		var employee = getEmployeeOrThrow(id);
		employee.getDepartment().removeEmployee(employee);
		departmentService.removeFromSubordination(id);
		employeeRepository.findByManagerEmployeeIdOrderByFirstNameAscLastNameAsc(id)
				.forEach(e -> e.setManager(null));
	}

	@Override
	public List<EmployeeDto> getSubordinateEmployees(Long employeeId) {
		return employeeMapper.toDto(employeeRepository
				.findByManagerEmployeeIdOrderByFirstNameAscLastNameAsc(employeeId));
	}

	private Employee getEmployeeOrThrow(Long id) {
		return employeeRepository.findById(id)
				.orElseThrow(() -> new AppServiceException("Employee not found for id : " + id));
	}
}
