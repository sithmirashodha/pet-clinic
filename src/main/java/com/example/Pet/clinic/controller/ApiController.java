package com.example.Pet.clinic.controller;

import com.example.Pet.clinic.entity.*;
import com.example.Pet.clinic.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/clinics/register")
    public String registerClinic(@RequestBody Map<String, String> b) {
        if (userRepository.findByEmail(b.get("email")).isPresent()) {
            return "Email already registered";
        }
        User user = new User();
        user.setEmail(b.get("email"));
        user.setPassword(passwordEncoder.encode(b.get("password")));
        user.setRole("CLINIC");
        userRepository.save(user);

        Clinic c = new Clinic();
        c.setName(b.get("name"));
        c.setAddress(b.get("address"));
        c.setPhone(b.get("phone"));
        c.setUser(user);
        clinicRepository.save(c);
        return "Clinic request sent successfully";
    }

    @GetMapping("/clinics/pending")
    public List<Clinic> pending() {
        return clinicRepository.findByStatus("PENDING");
    }

    @GetMapping("/clinics/approved")
    public List<Clinic> approved() {
        return clinicRepository.findByStatus("APPROVED");
    }

    @PutMapping("/clinics/{id}/approve")
    public String approve(@PathVariable Long id) {
        Clinic c = clinicRepository.findById(id).orElseThrow();
        c.setStatus("APPROVED");
        clinicRepository.save(c);
        return "Clinic approved successfully";
    }

    @PutMapping("/clinics/{id}/reject")
    public String reject(@PathVariable Long id) {
        Clinic c = clinicRepository.findById(id).orElseThrow();
        c.setStatus("REJECTED");
        clinicRepository.save(c);
        return "Clinic rejected successfully";
    }

    @PutMapping("/clinics/{id}")
    public String updateClinic(@PathVariable Long id, @RequestBody Map<String, String> b) {
        Clinic c = clinicRepository.findById(id).orElseThrow();
        c.setName(b.get("name"));
        c.setAddress(b.get("address"));
        c.setPhone(b.get("phone"));
        clinicRepository.save(c);
        return "Clinic updated successfully";
    }

    @DeleteMapping("/clinics/{id}")
    @Transactional
    public String deleteClinic(@PathVariable Long id) {
        Clinic c = clinicRepository.findById(id).orElseThrow();
        commentRepository.deleteByClinicId(id);
        clinicRepository.delete(c);
        if (c.getUser() != null) {
            userRepository.delete(c.getUser());
        }
        return "Clinic deleted successfully";
    }

    @PostMapping("/owners/register")
    public String registerOwner(@RequestBody Map<String, String> b) {
        if (userRepository.findByEmail(b.get("email")).isPresent()) {
            return "Email already registered";
        }
        User user = new User();
        user.setEmail(b.get("email"));
        user.setPassword(passwordEncoder.encode(b.get("password")));
        user.setRole("OWNER");
        userRepository.save(user);
        return "Owner registered successfully";
    }

    @PostMapping("/questions")
    public String addQuestion(@RequestBody Map<String, String> b) {
        User owner = userRepository.findByEmail(b.get("ownerEmail")).orElseThrow();
        Question q = new Question();
        q.setTitle(b.get("title"));
        q.setDescription(b.get("description"));
        q.setCategory(b.get("category"));
        q.setOwner(owner);
        questionRepository.save(q);
        return "Question added successfully";
    }

    @GetMapping("/questions")
    public List<Question> questions() {
        return questionRepository.findAllByOrderByCreatedAtDesc();
    }

    @PostMapping("/questions/{id}/comments")
    public String addComment(@PathVariable Long id, @RequestBody Map<String, String> b) {
        User user = userRepository.findByEmail(b.get("clinicEmail")).orElseThrow();
        Clinic clinic = clinicRepository.findByUserId(user.getId()).orElseThrow();
        if (!"APPROVED".equals(clinic.getStatus())) {
            return "Clinic is not approved";
        }
        Comment c = new Comment();
        c.setText(b.get("text"));
        c.setClinic(clinic);
        c.setQuestion(questionRepository.findById(id).orElseThrow());
        commentRepository.save(c);
        return "Comment added successfully";
    }

    @GetMapping("/questions/{id}/comments")
    public List<Comment> comments(@PathVariable Long id) {
        return commentRepository.findByQuestionId(id);
    }
}
