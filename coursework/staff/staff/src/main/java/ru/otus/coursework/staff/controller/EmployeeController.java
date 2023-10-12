package ru.otus.coursework.staff.controller;

import static java.util.Optional.ofNullable;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.otus.coursework.staff.service.DepartmentService;
import ru.otus.coursework.staff.service.DictionaryService;
import ru.otus.coursework.staff.service.EmployeeService;
import ru.otus.coursework.staff.service.dto.DepartmentDto;
import ru.otus.coursework.staff.service.dto.EmployeeDto;

@Controller
@RequiredArgsConstructor
public class EmployeeController {
	private final EmployeeService employeeService;

	private final DictionaryService dictionaryService;

	private final DepartmentService departmentService;

	@GetMapping("/employee/{id}")
	public String viewPage(@RequestParam(name = "department", required = false) String departmentId,
			@PathVariable("id") Long id, Model model) {
		addViewInfo(id, model);
		if (departmentId != null) {
			model.addAttribute("departmentId", departmentId);
		}
		return "employeeView";
	}

	@GetMapping("/employee/add")
	public String addPage(@RequestParam(name = "department", required = false) String departmentId,
						  Model model) {
		addDictionaries(model, "add");
		if (departmentId != null) {
			model.addAttribute("departmentId", departmentId);
		}

		val employeeDto = new EmployeeDto();
		ofNullable(departmentId).map(DepartmentDto::new).ifPresent(employeeDto::setDepartment);
		employeeDto.setHireDate(LocalDate.now());

		model.addAttribute("employee", employeeDto);
		return "employeeEdit";
	}

	@PostMapping("/employee/add")
	public String createDepartment(@Valid @ModelAttribute("employee") EmployeeDto employeeDto,
								   BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			addDictionaries(model, "add");
			return "employeeEdit";
		}
		employeeService.createEmployees(employeeDto);

		return "redirect:/department/" + employeeDto.getDepartment().getDepartmentId();
	}

	@GetMapping("/employee/{id}/edit")
	public String editPage(@PathVariable("id") Long id, Model model) {
		val employeeDto = employeeService.getEmployee(id);
		addDictionaries(model, "edit");
		model.addAttribute("employee", employeeDto);
		model.addAttribute("departmentId", employeeDto.getDepartment().getDepartmentId());
		return "employeeEdit";
	}

	@PostMapping("/employee/{id}/edit")
	public String saveDepartment(@Valid @ModelAttribute("employee") EmployeeDto employeeDto,
								 BindingResult bindingResult, Model model,
								 @PathVariable String id) {
		if (bindingResult.hasErrors()) {
			addDictionaries(model, "edit");
			return "employeeEdit";
		}
		employeeService.modifyEmployees(employeeDto);
		return "redirect:/department/" + employeeDto.getDepartment().getDepartmentId();
	}

	@GetMapping("/employee/{id}/delete")
	public String deletePage(@PathVariable("id") Long id, Model model,
			@RequestParam(name = "department", required = false) String departmentId) {
		addViewInfo(id, model);
		model.addAttribute("action", "delete");
		if (departmentId != null) {
			model.addAttribute("departmentId", departmentId);
		}
		return "employeeView";
	}

	@Transactional
	@DeleteMapping("/employee/{id}/delete")
	public String deleteEmployee(@PathVariable("id") Long id, Model model) {
		employeeService.deleteEmployee(id);
		if (model.containsAttribute("departmentId")) {
			return "redirect:/department/" + model.getAttribute("departmentId");
		}
		return "redirect:/";
	}

	private void addViewInfo(@PathVariable("id") Long id, Model model) {
		val employee = employeeService.getEmployee(id);
		val subordinateEmployees = employeeService.getSubordinateEmployees(id);
		val subordinateDepartments = departmentService.getSubordinateDepartments(id);

		model.addAttribute("employee", employee)
			.addAttribute("subordinateEmployees", subordinateEmployees)
			.addAttribute("subordinateDepartments", subordinateDepartments);
	}

	private void addDictionaries(Model model, String mode) {
		model.addAttribute("action", mode);
		val employees = dictionaryService.getEmployees();
		val jobs = dictionaryService.getJobs();
		val departments = dictionaryService.getDepartments();
		model.addAttribute("jobs", jobs)
			.addAttribute("employees", employees)
			.addAttribute("departments", departments);
	}

}
