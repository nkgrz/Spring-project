package com.whitetail.learningspring.controller;

import com.whitetail.learningspring.domain.User;
import com.whitetail.learningspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(User user, Model model, RedirectAttributes redirectAttributes) {

        if (!userService.addUser(user)) {
            model.addAttribute("errorMessage", "User exists!");
            return "registration";
        }

        redirectAttributes.addFlashAttribute("message", "Registration successful!");
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable("code") String code, Model model) {
        boolean isConfirmed = userService.confirmEmail(code);

        if (isConfirmed) {
            model.addAttribute("message", "The email has been successfully confirmed!");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }

}
