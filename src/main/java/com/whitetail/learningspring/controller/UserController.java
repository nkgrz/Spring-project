package com.whitetail.learningspring.controller;

import com.whitetail.learningspring.domain.Role;
import com.whitetail.learningspring.domain.User;
import com.whitetail.learningspring.service.UserService;
import com.whitetail.learningspring.validation.PasswordValidationGroup;
import com.whitetail.learningspring.validation.UsernameEmailValidationGroup;
import com.whitetail.learningspring.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping("/{userId}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUserByUserId(@PathVariable Long userId, Model model) {
        userService.deleteUser(userId);
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user) {
        userService.saveUser(user, username, form);
        return "redirect:/user";
    }

    @GetMapping("profile")
    public String profile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @Validated(UsernameEmailValidationGroup.class) User userUpdate,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllersUtils.getErrorsMap(bindingResult);
            model.mergeAttributes(errors);
        } else {
            try {
                userService.updateProfile(user, userUpdate.getUsername(), userUpdate.getEmail());
                model.addAttribute("message", "Данные профиля обновлены");
            } catch (UsernameNotFoundException e) {
                if (e.getMessage().contains("usernameError")) {
                    model.addAttribute("usernameError", "Username already taken");
                }
                if (e.getMessage().contains("emailError")) {
                    model.addAttribute("emailError", "Email already taken");
                }
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("username", userUpdate.getUsername());
        model.addAttribute("email", userUpdate.getEmail());
        return "profile";
    }

    @GetMapping("profile/change-password")
    public String changePassword() {
        return "changePassword";
    }

    @PostMapping("change-password")
    public String updatePassword(@AuthenticationPrincipal User user,
                                 @RequestParam String currentPassword,
                                 @Validated(PasswordValidationGroup.class) User userUpdate,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllersUtils.getErrorsMap(bindingResult);
            model.mergeAttributes(errors);
        } else {
            try {
                userService.updatePassword(user, currentPassword, userUpdate.getPassword(), userUpdate.getPasswordConfirmation());
                redirectAttributes.addFlashAttribute("message",
                        "The password has been successfully changed!");
                return "redirect:/user/profile";
            } catch (ValidationException e) {
                ControllersUtils.handleErrors(e, model);
            }
        }

        return "changePassword";
    }

}
