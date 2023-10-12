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
import ru.otus.coursework.staff.service.JobService;
import ru.otus.coursework.staff.service.dto.JobDto;

@Controller
@RequiredArgsConstructor
public class JobController {
	private final JobService jobService;

	@GetMapping("/job")
	public String listPage(Model model) {
		val allDepartments = jobService.listAllJobs();
		model.addAttribute("jobs", allDepartments);
		return "jobList";
	}

	@GetMapping("/job/{id}")
	public String viewPage(@PathVariable("id") String id, Model model) {
		val job = jobService.getJob(id);
		model.addAttribute("job", job);
		return "jobView";
	}

	@GetMapping("/job/{id}/edit")
	public String editPage(@PathVariable("id") String id, Model model) {
		val job = jobService.getJob(id);
		model.addAttribute("job", job);
		model.addAttribute("action", "edit");
		model.addAttribute("posturl", "/job/" + id + "/edit");
		return "jobEdit";
	}

	@PostMapping("/job/{id}/edit")
	public String saveJob(@Valid @ModelAttribute("job") JobDto jobDto,
						  BindingResult bindingResult, Model model, @PathVariable String id) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("action", "edit");
			return "jobEdit";
		}

		jobService.modifyJob(jobDto);
		return "redirect:/job";
	}

	@GetMapping("/job/add")
	public String addPage(Model model) {
		model.addAttribute("job", new JobDto());
		model.addAttribute("action", "add");
		model.addAttribute("posturl", "/job/add");
		return "jobEdit";
	}

	@PostMapping("/job/add")
	public String createJob(@Valid @ModelAttribute("job") JobDto jobDto,
						  BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("action", "add");
			return "jobEdit";
		}
		jobService.createJob(jobDto);
		return "redirect:/job";
	}

	@GetMapping("/job/{id}/delete")
	public String deletePage(@PathVariable("id") String id, Model model) {
		val job = jobService.getJob(id);
		model.addAttribute("job", job);
		model.addAttribute("action", "delete");
		return "jobView";
	}

	@Transactional
	@DeleteMapping("/job/{id}/delete")
	public String deleteJob(@PathVariable("id") String id) {
		jobService.deleteJob(id);
		return "redirect:/job";
	}
}
