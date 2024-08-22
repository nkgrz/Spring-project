package com.whitetail.learningspring.controller;

import com.whitetail.learningspring.domain.User;
import com.whitetail.learningspring.service.UserService;
import com.whitetail.learningspring.validation.AllValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

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
    public String registration(@Validated(AllValidationGroups.class) User user,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllersUtils.getErrorsMap(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }
        try {
            userService.addUser(user);
        } catch (Exception e) {
            if (e.getMessage().contains("usernameError")) {
                model.addAttribute("usernameError", "Username is already taken");
            }
            if (e.getMessage().contains("emailError")) {
                model.addAttribute("emailError", "Email is already taken");
            }
            if (e.getMessage().contains("passwordError")) {
                model.addAttribute("password2Error", "Password are different!");
            }
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
            model.addAttribute("errorMessage", "Activation code is not found!");
        }

        return "login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        }
        return "login";
    }

}
