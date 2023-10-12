package ru.otus.coursework.staff.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.otus.coursework.staff.repository.DepartmentRepository;
import ru.otus.coursework.staff.service.DepartmentService;
import ru.otus.coursework.staff.service.dto.DepartmentDetailsDto;
import ru.otus.coursework.staff.service.dto.DepartmentDto;
import ru.otus.coursework.staff.service.mapper.DepartmentMapper;
import ru.otus.coursework.staff.service.mapper.DictionaryMapper;
import ru.otus.coursework.staff.service.misc.AppServiceException;

@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {
	private final DepartmentRepository departmentRepository;

	private final DepartmentMapper departmentMapper;

	private final DictionaryMapper dictionaryMapper;

	@Override
	public List<DepartmentDto> listAllDepartments() {
		return departmentMapper.toDto(departmentRepository.findAll());
	}

	@Override
	public List<DepartmentDto> getSubordinateDepartments(Long employeeId) {
		return departmentRepository.findByManagerEmployeeIdOrderByDepartmentName(employeeId)
				.stream().map(dictionaryMapper::toDtoDepartmentName).toList();
	}

	@Override
	public DepartmentDetailsDto getDepartment(String id) {
		return departmentRepository.findById(id)
				.map(departmentMapper::toDetailsDto)
				.orElseThrow(() -> new AppServiceException("Department not found for id : " + id));
	}

	@Transactional
	@Override
	public DepartmentDto createDepartment(DepartmentDto departmentDto) {
		if (departmentRepository.existsById(departmentDto.getDepartmentId())) {
			throw new AppServiceException("Department already exists. id : "
					+ departmentDto.getDepartmentId());
		}
		val department = departmentMapper.toEntity(departmentDto);
		departmentRepository.save(department);
		return departmentMapper.toDto(department);
	}

	@Transactional
	@Override
	public void modifyDepartment(DepartmentDto dto) {
		val id = dto.getDepartmentId();
		val entity = departmentRepository.findById(id)
				.orElseThrow(() -> new AppServiceException("Department not found for id : " + id));
		departmentMapper.partialUpdate(entity, dto);
		departmentRepository.save(entity);
	}

	@Transactional
	@Override
	public void deleteDepartment(String id) {
		departmentRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void removeFromSubordination(Long employeeId) {
		departmentRepository.findByManagerEmployeeIdOrderByDepartmentName(employeeId)
				.forEach(department -> department.setManager(null));
	}
}
