package ru.otus.coursework.staff.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.val;
import ru.otus.coursework.staff.domain.Department;
import ru.otus.coursework.staff.domain.Employee;
import ru.otus.coursework.staff.domain.Job;
import ru.otus.coursework.staff.service.dto.DepartmentDetailsDto;
import ru.otus.coursework.staff.service.dto.DepartmentDto;
import ru.otus.coursework.staff.service.dto.EmployeeDto;
import ru.otus.coursework.staff.service.dto.JobDto;


@SpringBootTest(classes = {DepartmentMapperImpl.class, DictionaryMapperImpl.class})
class DepartmentMapperTest {

	@Autowired
	private DepartmentMapper mapper;

	private Department entity;

	private DepartmentDto dto;

	private DepartmentDetailsDto departmentDetailsDto;

	@Test
	void toDto() {
		val departmentDto = mapper.toDto(entity);
		assertThat(departmentDto).isNotNull()
				.isEqualTo(dto) // compare only by departmentId
				.usingRecursiveComparison()
				.comparingOnlyFields("departmentId", "departmentName",
						"manager.employeeId", "manager.firstName", "manager.lastName")
				.isEqualTo(dto);
	}

	@Test
	void toDetailsDto() {
		val departmentDto = mapper.toDetailsDto(entity);
		assertThat(departmentDto).isNotNull()
				.isEqualTo(departmentDetailsDto) // compare only by departmentId
				.usingRecursiveComparison()
				.ignoringFields("employees.department", "employees.salary", "employees.manager",
						"manager.department", "manager.salary", "manager.manager")
				.ignoringFieldsMatchingRegexes(".*job\\.(min|max)Salary")
				.isEqualTo(departmentDetailsDto);
	}

	@Test
	void toEntity() {
		val department = mapper.toEntity(dto);
		assertThat(department).isNotNull()
				.isEqualTo(entity) // compare only by departmentId
				.usingRecursiveComparison()
				.ignoringFieldsMatchingRegexes("employees.*", "manager\\..+")
				.isEqualTo(entity);
	}

	@Test
	void toEntityManagerNoneToNull() {
		dto.setManager(new EmployeeDto());
		entity.setManager(null);
		val department = mapper.toEntity(dto);
		assertThat(department).isNotNull()
				.isEqualTo(entity) // compare only by departmentId
				.usingRecursiveComparison()
				.ignoringFields("employees", "manager.department", "manager.jobHistories", "manager.job")
				.isEqualTo(entity);
	}

	@Test
	void partialUpdate() {
		var optionalEmployee = entity.getEmployees().stream().filter(t -> t.getEmployeeId().equals(101L)).findFirst();
		assertThat(optionalEmployee).isPresent();
		val vice = optionalEmployee.get();
		val newBossDto = new EmployeeDto(vice.getEmployeeId(), vice.getFirstName(), vice.getLastName(), vice.getEmail(),
				vice.getPhoneNumber(), vice.getHireDate(), vice.getSalary(),
				null, null,
				new JobDto(vice.getJob().getJobId(), vice.getJob().getJobTitle(),
						vice.getJob().getMinSalary(), vice.getJob().getMaxSalary()));
		dto.setDepartmentName("New department name");
		dto.setManager(newBossDto);

		mapper.partialUpdate(entity, dto);
		assertEquals(entity.getDepartmentName(), dto.getDepartmentName());
		assertEquals(entity.getManager().getEmployeeId(), newBossDto.getEmployeeId());

		val department = mapper.toEntity(dto);
		assertThat(department).isNotNull()
				.isEqualTo(entity) // compare only by departmentId
				.usingRecursiveComparison()
				.ignoringFieldsMatchingRegexes("employees.*", "manager\\..+")
				.isEqualTo(entity);
	}

	@Test
	void partialUpdateSetManagerNull() {
		var optionalEmployee = entity.getEmployees().stream().filter(t -> t.getEmployeeId().equals(101L)).findFirst();
		assertThat(optionalEmployee).isPresent();
		val noneDto = new EmployeeDto();
		dto.setDepartmentName("New department name");
		dto.setManager(noneDto);

		mapper.partialUpdate(entity, dto);
		assertEquals(entity.getDepartmentName(), dto.getDepartmentName());
		assertNull(entity.getManager());

		val department = mapper.toEntity(dto);
		assertThat(department).isNotNull()
				.isEqualTo(entity) // compare only by departmentId
				.usingRecursiveComparison()
				.ignoringFields("employees", "manager")
				.isEqualTo(entity);
	}

	@BeforeEach
	void setUp() {
		entity = new Department("AD", "Administration", new Employee(), null);

		val boss = entity.getManager();
		boss.setEmployeeId(100L);
		boss.setFirstName("Steven");
		boss.setLastName("King");
		boss.setEmail("SKING");
		boss.setPhoneNumber("+7(915)555-0100");
		boss.setHireDate(LocalDate.of(2013, 6, 17));
		boss.setSalary(24000L);
		boss.setManager(null);
		boss.setJob(new Job("AD_PRES", "President", 20080L, 40000L));

		val vice = new Employee();
		vice.setEmployeeId(101L);
		vice.setFirstName("Neena");
		vice.setLastName("Yang");
		vice.setEmail("NYANG");
		vice.setPhoneNumber("+7(915)555-0101");
		vice.setHireDate(LocalDate.of(2015, 9, 21));
		vice.setSalary(17000L);
		vice.setManager(boss);
		vice.setJob(new Job("AD_VP", "Administration Vice President", 15000L, 30000L));

		val asst = new Employee();
		asst.setEmployeeId(200L);
		asst.setFirstName("Jennifer");
		asst.setLastName("Whalen");
		asst.setEmail("JWHALEN");
		asst.setPhoneNumber("+7(915)555-0200");
		asst.setHireDate(LocalDate.of(2013, 9, 17));
		asst.setSalary(4400L);
		asst.setManager(boss);
		asst.setJob(new Job("AD_ASST", "Administration Assistant", 3000L, 6000L));

		entity.setEmployees(new HashSet<>(Set.of(boss, vice, asst)));

		val bossDto = new EmployeeDto(boss.getEmployeeId(), boss.getFirstName(), boss.getLastName(), boss.getEmail(),
				boss.getPhoneNumber(), boss.getHireDate(), boss.getSalary(),
				null, null,
				new JobDto(boss.getJob().getJobId(), boss.getJob().getJobTitle(),
						boss.getJob().getMinSalary(), boss.getJob().getMaxSalary()));
		dto = new DepartmentDto(entity.getDepartmentId(), entity.getDepartmentName(), bossDto);
		bossDto.setDepartment(dto);

		val viceDto = new EmployeeDto(vice.getEmployeeId(), vice.getFirstName(), vice.getLastName(), vice.getEmail(),
				vice.getPhoneNumber(), vice.getHireDate(), vice.getSalary(),
				bossDto, dto,
				new JobDto(vice.getJob().getJobId(), vice.getJob().getJobTitle(),
						vice.getJob().getMinSalary(), vice.getJob().getMaxSalary()));
		val asstDto = new EmployeeDto(asst.getEmployeeId(), asst.getFirstName(), asst.getLastName(), asst.getEmail(),
				asst.getPhoneNumber(), asst.getHireDate(), asst.getSalary(),
				bossDto, dto,
				new JobDto(asst.getJob().getJobId(), asst.getJob().getJobTitle(),
						asst.getJob().getMinSalary(), asst.getJob().getMaxSalary()));

		departmentDetailsDto = new DepartmentDetailsDto(entity.getDepartmentId(), entity.getDepartmentName(), bossDto,
				Set.of(bossDto, viceDto, asstDto));
	}


}
