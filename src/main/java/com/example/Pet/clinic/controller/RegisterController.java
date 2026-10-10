package com.example.Pet.clinic.controller;

import com.example.Pet.clinic.entity.Clinic;
import com.example.Pet.clinic.entity.User;
import com.example.Pet.clinic.repository.ClinicRepository;
import com.example.Pet.clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register/clinic")
    public String clinicForm() {
        return "register-clinic";
    }

    @PostMapping("/register/clinic")
    public String registerClinic(@RequestParam String name,
                                 @RequestParam String address,
                                 @RequestParam String phone,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 Model model) {
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "This email is already registered");
            return "register-clinic";
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("CLINIC");
        userRepository.save(user);

        Clinic clinic = new Clinic();
        clinic.setName(name);
        clinic.setAddress(address);
        clinic.setPhone(phone);
        clinic.setUser(user);
        clinicRepository.save(clinic);

        model.addAttribute("success", "Request sent! Please wait for admin approval.");
        return "register-clinic";
    }

    @GetMapping("/register/owner")
    public String ownerForm() {
        return "register-owner";
    }

    @PostMapping("/register/owner")
    public String registerOwner(@RequestParam String email,
                                @RequestParam String password,
                                Model model) {
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "This email is already registered");
            return "register-owner";
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("OWNER");
        userRepository.save(user);

        model.addAttribute("success", "Registered! You can login now.");
        return "register-owner";
    }
}
