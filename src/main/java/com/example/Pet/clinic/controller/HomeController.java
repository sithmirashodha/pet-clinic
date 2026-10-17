package com.example.Pet.clinic.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        String role = auth.getAuthorities().iterator().next().getAuthority();
        if (role.equals("ROLE_ADMIN")) return "redirect:/admin/home";
        if (role.equals("ROLE_CLINIC")) return "redirect:/clinic/home";
        return "redirect:/owner/questions/new";
    }

}
