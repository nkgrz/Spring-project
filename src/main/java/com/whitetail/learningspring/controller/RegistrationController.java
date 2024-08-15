package com.whitetail.learningspring.controller;

import com.whitetail.learningspring.domain.Role;
import com.whitetail.learningspring.domain.User;
import com.whitetail.learningspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(User user, Model model, RedirectAttributes redirectAttributes) {
        User userExists = userRepository.findByUsername(user.getUsername());
        if (userExists != null) {
            model.addAttribute("message", "Username is already taken!");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "Registration successful!");
        return "redirect:/login";
    }

}
