package ru.otus.coursework.staff.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
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
import ru.otus.coursework.staff.service.dto.JobHistoryDto;

@SpringBootTest(classes = {JobHistoryMapperImpl.class, DictionaryMapperImpl.class})
class JobHistoryMapperTest {

	@Autowired
	private JobHistoryMapper mapper;

	private JobHistoryDto dto;

	private JobHistory entity;

	@Test
	void toDto() {
		val jobHistoryDto = mapper.toDto(entity);
		assertThat(jobHistoryDto).isNotNull()
				.isEqualTo(dto) // compare only by employeeId
				.usingRecursiveComparison()
				.comparingOnlyFields("jobHistoryId", "startDate", "endDate",
						"employee.employeeId", "employee.firstName", "employee.lastName",
						"job.jobId", "job.jobTitle",
						"department.departmentId", "department.departmentName")
				.isEqualTo(dto);
	}

	@Test
	void toEntity() {
		val jobHistory = mapper.toEntity(dto);
		assertThat(jobHistory).isNotNull()
				.isEqualTo(entity) // compare only by employeeId
				.usingRecursiveComparison()
				.ignoringFields("department.employees", "department.manager",
						"employee.department", "employee.manager", "employee.jobHistories")
				.isEqualTo(entity);
	}

	@Test
	void partialUpdate() {
		val startDate = LocalDate.of(2007, 9, 22);
		val endDate = LocalDate.of(2012, 8, 17);
		val jobCSA = new JobDto("DV_CSA", "Chief software architect", 8200L, 16000L);
		mapper.partialUpdate(entity, new JobHistoryDto(null, startDate,
				endDate, null, null, jobCSA));
		assertEquals(entity.getJobHistoryId(), dto.getJobHistoryId());
		assertEquals(entity.getStartDate(), startDate);
		assertEquals(entity.getEndDate(), endDate);
		assertEquals(entity.getEmployee().getEmployeeId(), dto.getEmployee().getEmployeeId());
		assertEquals(entity.getDepartment().getDepartmentId(), dto.getDepartment().getDepartmentId());
		assertEquals(entity.getJob().getJobId(), jobCSA.getJobId());
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
		depAD.setEmployees(Set.of(boss, vice, asst));

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
		depFI.setEmployees(Set.of(cfo, acct));

		JobHistory job1st = new JobHistory(2L, LocalDate.of(2007, 9, 21),
				LocalDate.of(2012, 8, 16), jobAcct, depFI, vice);
		JobHistory job2nd = new JobHistory(3L, LocalDate.of(2011, 10, 28),
				LocalDate.of(2015, 3, 15), jobCFO, depFI, vice);
		vice.setJobHistories(Set.of(job1st, job2nd));

		entity = job1st;

		val bossDto = new EmployeeDto(boss.getEmployeeId(), boss.getFirstName(), boss.getLastName(), boss.getEmail(),
				boss.getPhoneNumber(), boss.getHireDate(), boss.getSalary(),
				null, null,
				new JobDto(boss.getJob().getJobId(), boss.getJob().getJobTitle(),
						boss.getJob().getMinSalary(), boss.getJob().getMaxSalary()));
		bossDto.setDepartment(new DepartmentDto(depAD.getDepartmentId(), depAD.getDepartmentName(), bossDto));
		val viceDto = new EmployeeDto(vice.getEmployeeId(), vice.getFirstName(), vice.getLastName(), vice.getEmail(),
				vice.getPhoneNumber(), vice.getHireDate(), vice.getSalary(),
				bossDto, bossDto.getDepartment(),
				new JobDto(vice.getJob().getJobId(), vice.getJob().getJobTitle(),
						vice.getJob().getMinSalary(), vice.getJob().getMaxSalary()));
		val cfoDto = new EmployeeDto(cfo.getEmployeeId(), cfo.getFirstName(), cfo.getLastName(), cfo.getEmail(),
				cfo.getPhoneNumber(), cfo.getHireDate(), cfo.getSalary(),
				viceDto, null,
				new JobDto(cfo.getJob().getJobId(), cfo.getJob().getJobTitle(),
						cfo.getJob().getMinSalary(), cfo.getJob().getMaxSalary()));
		cfoDto.setDepartment(new DepartmentDto(depFI.getDepartmentId(), depFI.getDepartmentName(), cfoDto));
		val acctDto = new EmployeeDto(acct.getEmployeeId(), acct.getFirstName(), acct.getLastName(), acct.getEmail(),
				acct.getPhoneNumber(), acct.getHireDate(), acct.getSalary(),
				cfoDto, cfoDto.getDepartment(),
				new JobDto(acct.getJob().getJobId(), acct.getJob().getJobTitle(),
						acct.getJob().getMinSalary(), acct.getJob().getMaxSalary()));

		dto = new JobHistoryDto(job1st.getJobHistoryId(), job1st.getStartDate(), job1st.getEndDate(),
				viceDto, acctDto.getDepartment(), acctDto.getJob());
	}

}