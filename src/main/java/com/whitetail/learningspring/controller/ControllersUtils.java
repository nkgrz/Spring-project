package com.whitetail.learningspring.controller;

import com.whitetail.learningspring.validation.ValidationException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllersUtils {
    static Map<String, String> getErrorsMap(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> map = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage);
        return bindingResult.getFieldErrors().stream().collect(
                map);
    }

    public static void handleErrors(ValidationException e, Model model) {
        if (e.getMessage().contains("usernameError")) {
            model.addAttribute("usernameError", "Username is already taken");
        }
        if (e.getMessage().contains("emailError")) {
            model.addAttribute("emailError", "Email is already taken");
        }
        if (e.getMessage().contains("passwordConfirmationError")) {
            model.addAttribute("passwordConfirmationError", "Passwords are different!");
        }
        if (e.getMessage().contains("currentPasswordError")) {
            model.addAttribute("currentPasswordError", "The current password is incorrect");
        }
    }
}
