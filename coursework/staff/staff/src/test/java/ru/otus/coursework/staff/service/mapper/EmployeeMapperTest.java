package ru.otus.coursework.staff.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.Comparator;
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
import ru.otus.coursework.staff.domain.JobHistory;
import ru.otus.coursework.staff.service.dto.DepartmentDto;
import ru.otus.coursework.staff.service.dto.EmployeeDto;
import ru.otus.coursework.staff.service.dto.JobDto;

@SpringBootTest(classes = {EmployeeMapperImpl.class, DictionaryMapperImpl.class})
class EmployeeMapperTest {

	@Autowired
	private EmployeeMapper mapper;

	@Autowired
	private DictionaryMapper dictionaryMapper;

	private Employee entity;

	private EmployeeDto dto;

	@Test
	void toDto() {
		val employeeDto = mapper.toDto(entity);
		assertThat(employeeDto).isNotNull()
				.isEqualTo(dto) // compare only by employeeId
				.usingRecursiveComparison()
				.comparingOnlyFields("employeeId", "firstName", "lastName",
						"email", "phoneNumber", "hireDate", "salary",
						"manager.employeeId", "manager.firstName", "manager.lastName",
						"job.jobId", "job.jobTitle",
						"department.departmentId", "department.departmentName")
				.isEqualTo(dto);
	}

	@Test
	void toEntity() {
		val employee = mapper.toEntity(dto);
		assertThat(employee).isNotNull()
				.isEqualTo(entity) // compare only by employeeId
				.usingRecursiveComparison()
				.ignoringFieldsMatchingRegexes("jobHistories.*",
						"manager\\..+","department\\..+", "job\\..+")
				.isEqualTo(entity);
	}

	@Test
	void toEntityManagerNoneToNull() {
		dto.setManager(new EmployeeDto());
		entity.setManager(null);
		val employee = mapper.toEntity(dto);
		assertThat(employee).isNotNull()
				.isEqualTo(entity) // compare only by employeeId
				.usingRecursiveComparison()
				.ignoringFieldsMatchingRegexes("jobHistories.*",
						"department\\..+", "job\\..+")
				.isEqualTo(entity);
	}

	@Test
	void partialUpdate() {
		Long salary = 15000L;
		val jobCSA = new JobDto("DV_CSA", "Chief software architect", 8200L, 16000L);
		val phoneNumber = "+7(915)555-0115";
		val lastName = "Miller";
		val email = "NMILLER";
		mapper.partialUpdate(entity, new EmployeeDto(null, null, lastName, email,
				phoneNumber, null, salary,
				null, null, jobCSA));
		assertEquals(entity.getEmployeeId(), dto.getEmployeeId());
		assertEquals(entity.getFirstName(), dto.getFirstName());
		assertEquals(entity.getDepartment().getDepartmentId(), dto.getDepartment().getDepartmentId());
		assertEquals(entity.getManager().getEmployeeId(), dto.getManager().getEmployeeId());
		assertEquals(entity.getLastName(), lastName);
		assertEquals(entity.getEmail(), email);
		assertEquals(entity.getJob().getJobId(), jobCSA.getJobId());
		assertEquals(entity.getSalary(), salary);
		assertEquals(entity.getPhoneNumber(), phoneNumber);
	}

	@Test
	void partialUpdateSetManagerNull() {
		mapper.partialUpdate(entity, new EmployeeDto(null, null, null, null,
				null, null, null, new EmployeeDto(), null, null));
		assertNull(entity.getManager());
	}

	@Test
	void modifyDepartment() {
		val changeJobDate = LocalDate.now();
		val prevDepartment = entity.getDepartment();
		val prevJob = entity.getJob();
		val jobHistoriesSize = entity.getJobHistories().size();
		val newDepartmentDto = new DepartmentDto("XX", "New Department", null);
		val newJobDto = new JobDto("XX_BOSS", "Head of New Deparment", null, null);


		var employeeDto = new EmployeeDto(null, null, null,
				null, null, null, null, null,
				newDepartmentDto, newJobDto);

		mapper.modifyEmployees(entity, employeeDto, changeJobDate);
		assertEquals(entity.getDepartment().getDepartmentId(), newDepartmentDto.getDepartmentId());
		assertEquals(entity.getJob().getJobId(), newJobDto.getJobId());
		assertThat(prevDepartment.getEmployees()).doesNotContain(entity);
		assertThat(entity.getDepartment().getEmployees()).contains(entity);
		assertThat(entity.getJobHistories()).hasSize(jobHistoriesSize + 1);
		val lastJobHistory = entity.getJobHistories().stream()
				.max(Comparator.comparing(JobHistory::getStartDate)).get();
		assertEquals(lastJobHistory.getEmployee(), entity);
		assertEquals(lastJobHistory.getDepartment().getDepartmentId(), prevDepartment.getDepartmentId());
		assertEquals(lastJobHistory.getJob().getJobId(), prevJob.getJobId());
		assertEquals(lastJobHistory.getStartDate(), LocalDate.of(2015, 3, 15));
		assertEquals(lastJobHistory.getEndDate(), changeJobDate);
	}

	@BeforeEach
	void setUp() {
		val depAD = new Department("AD", "Administration", null, null);
		val depFI = new Department("FI", "Accounting", null, null);
		val jobBoss = new Job("AD_PRES", "President", 20080L, 40000L);
		val jobVice = new Job("AD_VP", "Administration Vice President", 15000L, 30000L);
		val jobAsst = new Job("AD_ASST", "Administration Assistant", 3000L, 6000L);
		val jobCFO = new Job("FI_MGR", "Finance Manager", 8200L, 16000L);
		val jobAcct = new Job("FI_ACCOUNT", "Accountant", 4200L, 9000L);

		val boss = new Employee();
		boss.setEmployeeId(100L);
		boss.setFirstName("Steven");
		boss.setLastName("King");
		boss.setEmail("SKING");
		boss.setPhoneNumber("+7(915)555-0100");
		boss.setHireDate(LocalDate.of(2013, 6, 17));
		boss.setSalary(24000L);
		boss.setManager(null);
		boss.setJob(jobBoss);
		val vice = new Employee();
		vice.setEmployeeId(101L);
		vice.setFirstName("Neena");
		vice.setLastName("Yang");
		vice.setEmail("NYANG");
		vice.setPhoneNumber("+7(915)555-0101");
		vice.setHireDate(LocalDate.of(2015, 9, 21));
		vice.setSalary(17000L);
		vice.setManager(boss);
		vice.setJob(jobVice);
		val asst = new Employee();
		asst.setEmployeeId(200L);
		asst.setFirstName("Jennifer");
		asst.setLastName("Whalen");
		asst.setEmail("JWHALEN");
		asst.setPhoneNumber("+7(915)555-0200");
		asst.setHireDate(LocalDate.of(2013, 9, 17));
		asst.setSalary(4400L);
		asst.setManager(boss);
		asst.setJob(jobAsst);

		depAD.setManager(boss);
		depAD.setEmployees(new HashSet<>(Set.of(boss, vice, asst)));

		val cfo = new Employee();
		cfo.setEmployeeId(108L);
		cfo.setFirstName("Nancy");
		cfo.setLastName("Gruenberg");
		cfo.setEmail("NGRUENBE");
		cfo.setPhoneNumber("+7(915)555-0108");
		cfo.setHireDate(LocalDate.of(2012, 8, 17));
		cfo.setSalary(12008L);
		cfo.setManager(vice);
		cfo.setJob(jobCFO);
		val acct = new Employee();
		acct.setEmployeeId(200L);
		acct.setFirstName("Daniel");
		acct.setLastName("Faviet");
		acct.setEmail("DFAVIET");
		acct.setPhoneNumber("+7(915)555-0109");
		acct.setHireDate(LocalDate.of(2012, 8, 16));
		acct.setSalary(9000L);
		acct.setManager(cfo);
		acct.setJob(jobAcct);
		depFI.setManager(cfo);
		depFI.setEmployees(new HashSet<>(Set.of(cfo, acct)));

		vice.addJobHistory(new JobHistory(2L, LocalDate.of(2007, 9, 21),
				LocalDate.of(2012, 8, 16), jobAcct, depFI, vice));
		vice.addJobHistory(new JobHistory(3L, LocalDate.of(2012, 8, 16),
				LocalDate.of(2015, 3, 15), jobCFO, depFI, vice));
		entity = vice;

		val bossDto = new EmployeeDto(boss.getEmployeeId(), boss.getFirstName(), boss.getLastName(), boss.getEmail(),
				boss.getPhoneNumber(), boss.getHireDate(), boss.getSalary(),
				null, null,
				new JobDto(boss.getJob().getJobId(), boss.getJob().getJobTitle(),
						boss.getJob().getMinSalary(), boss.getJob().getMaxSalary()));
		bossDto.setDepartment(new DepartmentDto(depAD.getDepartmentId(), depAD.getDepartmentName(), bossDto));
		dto = new EmployeeDto(vice.getEmployeeId(), vice.getFirstName(), vice.getLastName(), vice.getEmail(),
				vice.getPhoneNumber(), vice.getHireDate(), vice.getSalary(),
				bossDto, bossDto.getDepartment(),
				new JobDto(vice.getJob().getJobId(), vice.getJob().getJobTitle(),
						vice.getJob().getMinSalary(), vice.getJob().getMaxSalary()));
	}

}