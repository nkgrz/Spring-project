package com.whitetail.learningspring.controller;

import com.whitetail.learningspring.validation.ValidationException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllersUtils {

    private static final Map<String, String> ERROR_MAP;

    static {
        ERROR_MAP = new HashMap<>();
        ERROR_MAP.put("usernameError", "Username is already taken");
        ERROR_MAP.put("emailError", "Email is already taken");
        ERROR_MAP.put("passwordConfirmationError", "Passwords are different!");
        ERROR_MAP.put("currentPasswordError", "The current password is incorrect");
    }

    static Map<String, String> getErrorsMap(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> map = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage);
        return bindingResult.getFieldErrors().stream().collect(
                map);
    }

    public static void handleErrors(ValidationException e, Model model) {
        String[] errorMessages = e.getMessage().split(" ");
        for (String errorMessage : errorMessages) {
            if (ERROR_MAP.containsKey(errorMessage)) {
                model.addAttribute(errorMessage, ERROR_MAP.get(errorMessage));
            }
        }
    }

}
