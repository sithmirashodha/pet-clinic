package com.example.Pet.clinic.controller;

import com.example.Pet.clinic.entity.Clinic;
import com.example.Pet.clinic.repository.ClinicRepository;
import com.example.Pet.clinic.repository.CommentRepository;
import com.example.Pet.clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("pending", clinicRepository.findByStatus("PENDING"));
        model.addAttribute("approved", clinicRepository.findByStatus("APPROVED"));
        return "admin-home";
    }

    @PostMapping("/clinics/{id}/approve")
    public String approve(@PathVariable Long id) {
        Clinic c = clinicRepository.findById(id).orElseThrow();
        c.setStatus("APPROVED");
        clinicRepository.save(c);
        return "redirect:/admin/home";
    }

    @PostMapping("/clinics/{id}/reject")
    public String reject(@PathVariable Long id) {
        Clinic c = clinicRepository.findById(id).orElseThrow();
        c.setStatus("REJECTED");
        clinicRepository.save(c);
        return "redirect:/admin/home";
    }

    @GetMapping("/clinics/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("clinic", clinicRepository.findById(id).orElseThrow());
        return "admin-clinic-edit";
    }

    @PostMapping("/clinics/{id}/update")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam String address,
                         @RequestParam String phone) {
        Clinic c = clinicRepository.findById(id).orElseThrow();
        c.setName(name);
        c.setAddress(address);
        c.setPhone(phone);
        clinicRepository.save(c);
        return "redirect:/admin/home";
    }

    @PostMapping("/clinics/{id}/delete")
    @Transactional
    public String delete(@PathVariable Long id) {
        Clinic c = clinicRepository.findById(id).orElseThrow();
        commentRepository.deleteByClinicId(id);
        clinicRepository.delete(c);
        if (c.getUser() != null) {
            userRepository.delete(c.getUser());
        }
        return "redirect:/admin/home";
    }
}