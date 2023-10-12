package ru.otus.coursework.staff.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MiscController {
	@GetMapping("/todo")
	public String todo(Model model) {
		return "underConstraction";
	}
}
