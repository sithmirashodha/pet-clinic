package com.example.Pet.clinic.controller;

import com.example.Pet.clinic.entity.Question;
import com.example.Pet.clinic.repository.CommentRepository;
import com.example.Pet.clinic.repository.QuestionRepository;
import com.example.Pet.clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/home")
    public String home(Authentication auth, Model model) {
        model.addAttribute("questions",
                questionRepository.findByOwnerEmailOrderByCreatedAtDesc(auth.getName()));
        return "owner-home";
    }

    @GetMapping("/questions/new")
    public String form() {
        return "owner-question-form";
    }

    @PostMapping("/questions")
    public String create(@RequestParam String title,
                         @RequestParam String description,
                         @RequestParam String category,
                         Authentication auth) {
        Question q = new Question();
        q.setTitle(title);
        q.setDescription(description);
        q.setCategory(category);
        q.setOwner(userRepository.findByEmail(auth.getName()).orElseThrow());
        questionRepository.save(q);
        return "redirect:/owner/home";
    }

    @GetMapping("/questions/{id}")
    public String view(@PathVariable Long id, Authentication auth, Model model) {
        Question q = questionRepository.findById(id).orElseThrow();
        if (!q.getOwner().getEmail().equals(auth.getName())) {
            return "redirect:/owner/home";
        }
        model.addAttribute("question", q);
        model.addAttribute("comments", commentRepository.findByQuestionId(id));
        model.addAttribute("canComment", false);
        model.addAttribute("backUrl", "/owner/home");
        return "question-view";
    }
}
