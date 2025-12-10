package com.isro.pdfportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String index(Model model) {
	    model.addAttribute("portalTitle", "PDF Generator Web Application");
	    model.addAttribute("tagline", "Create and download professional PDFs directly from your browser.");
	    return "index";
	}
}