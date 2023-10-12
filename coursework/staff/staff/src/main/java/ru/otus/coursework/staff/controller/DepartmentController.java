package ru.otus.coursework.staff.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.otus.coursework.staff.service.DepartmentService;
import ru.otus.coursework.staff.service.DictionaryService;
import ru.otus.coursework.staff.service.dto.DepartmentDto;

@Controller
@RequiredArgsConstructor
public class DepartmentController {
	private final DepartmentService departmentService;

	private final DictionaryService dictionaryService;

	@GetMapping("/")
	public String listPage(Model model) {
		val allDepartments = departmentService.listAllDepartments();
		model.addAttribute("departments", allDepartments);
		return "departmentList";
	}

	@GetMapping("/department/{id}")
	public String viewPage(@PathVariable("id") String id, Model model) {
		val department = departmentService.getDepartment(id);
		model.addAttribute("department", department);
		return "departmentView";
	}

	@GetMapping("/department/{id}/edit")
	public String editPage(@PathVariable("id") String id, Model model) {
		val department = departmentService.getDepartment(id);
		val employees = dictionaryService.getEmployees();
		model.addAttribute("department", department);
		model.addAttribute("employees", employees);
		model.addAttribute("action", "edit");
		model.addAttribute("posturl", "/department/" + id + "/edit");
		return "departmentEdit";
	}

	@PostMapping("/department/{id}/edit")
	public String saveDepartment(@Valid @ModelAttribute("department") DepartmentDto departmentDto,
								 BindingResult bindingResult, Model model,
								 @PathVariable String id) {
		if (bindingResult.hasErrors()) {
			val employees = dictionaryService.getEmployees();
			model.addAttribute("employees", employees);
			model.addAttribute("action", "edit");
			return "departmentEdit";
		}
		departmentService.modifyDepartment(departmentDto);
		return "redirect:/";
	}

	@GetMapping("/department/add")
	public String addPage(Model model) {
		val employees = dictionaryService.getEmployees();
		model.addAttribute("department", new DepartmentDto());
		model.addAttribute("employees", employees);
		model.addAttribute("action", "add");
		model.addAttribute("posturl", "/department/add");
		return "departmentEdit";
	}

	@PostMapping("/department/add")
	public String createDepartment(@Valid @ModelAttribute("department") DepartmentDto departmentDto,
							BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			val employees = dictionaryService.getEmployees();
			model.addAttribute("employees", employees);
			model.addAttribute("action", "add");
			return "departmentEdit";
		}
		departmentService.createDepartment(departmentDto);
		return "redirect:/";
	}

	@GetMapping("/department/{id}/delete")
	public String deletePage(@PathVariable("id") String id, Model model) {
		val department = departmentService.getDepartment(id);
		model.addAttribute("department", department);
		model.addAttribute("action", "delete");
		return "departmentView";
	}

	@Transactional
	@DeleteMapping("/department/{id}/delete")
	public String deleteDepartment(@PathVariable("id") String id) {
		departmentService.deleteDepartment(id);
		return "redirect:/";
	}
}
