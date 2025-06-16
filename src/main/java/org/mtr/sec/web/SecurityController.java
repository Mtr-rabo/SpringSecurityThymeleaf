package org.mtr.sec.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

@Controller
public class SecurityController {
	
	@GetMapping("/403")
	public String accessDenied() {
		return "403";
	}
	// Controller
	@GetMapping("/login")
	public String loginPage(@RequestParam(value = "passwordChanged", required = false) String changed,
	                        Model model,
	                        @ModelAttribute("successMessage") String successMessage) {
	    if (changed != null) {
	        model.addAttribute("message", successMessage);
	    }
	    return "login"; // login.html
	}


}
