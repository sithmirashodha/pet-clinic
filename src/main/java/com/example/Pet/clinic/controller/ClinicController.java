package com.example.Pet.clinic.controller;

import com.example.Pet.clinic.entity.Clinic;
import com.example.Pet.clinic.entity.Comment;
import com.example.Pet.clinic.entity.Question;
import com.example.Pet.clinic.entity.User;
import com.example.Pet.clinic.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clinic")
@RequiredArgsConstructor
public class ClinicController {

    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("questions", questionRepository.findAllByOrderByCreatedAtDesc());
        return "clinic-home";
    }

    @GetMapping("/questions/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionRepository.findById(id).orElseThrow());
        model.addAttribute("comments", commentRepository.findByQuestionId(id));
        model.addAttribute("canComment", true);
        model.addAttribute("backUrl", "/clinic/home");
        return "question-view";
    }

    @PostMapping("/questions/{id}/comments")
    public String comment(@PathVariable Long id,
                          @RequestParam String text,
                          Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        Clinic clinic = clinicRepository.findByUserId(user.getId()).orElseThrow();
        Question question = questionRepository.findById(id).orElseThrow();

        Comment c = new Comment();
        c.setText(text);
        c.setClinic(clinic);
        c.setQuestion(question);
        commentRepository.save(c);
        return "redirect:/clinic/questions/" + id;
    }
}
