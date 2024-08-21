package com.whitetail.learningspring.service;

import ch.qos.logback.core.util.StringUtil;
import com.whitetail.learningspring.domain.Role;
import com.whitetail.learningspring.domain.User;
import com.whitetail.learningspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final MailSender mailSender;
    @Value("${app.activation-url}")
    private String activationUrl;

    @Autowired
    public UserService(UserRepository userRepository,
                       MessageService messageService,
                       MailSender mailSender) {
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long userId) {
        messageService.deleteMessages(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public boolean addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null
                || (!StringUtil.isNullOrEmpty(user.getEmail()) &&
                userRepository.findByEmail(user.getEmail()) != null)) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        sendMessage(user);

        userRepository.save(user);

        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtil.isNullOrEmpty(user.getEmail())) {
            String message = String.format("Hello, %s!\n"
                            + "Welcome to WhiteTail Shop. "
                            + "Please follow the link to activate your account: %s%s",
                    user.getUsername(), activationUrl, user.getActivationCode());
            mailSender.send(user.getEmail(), "Activation Code", message);
        }
    }

    public boolean confirmEmail(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        userRepository.save(user);

        return true;
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);
    }

    public void updateProfile(User user, String username, String email) {
        boolean isEmailChanged = !Objects.equals(user.getEmail(), email);

        if (isEmailChanged && !StringUtil.isNullOrEmpty(email)) {
            user.setEmail(email);
            user.setActivationCode(UUID.randomUUID().toString());
            sendMessage(user);
        }

        if (!StringUtil.isNullOrEmpty(username) &&
                !user.getUsername().equals(username) &&
                userRepository.findByUsername(username) == null) {
            user.setUsername(username);
        }

        userRepository.save(user);
    }

    public Model updatePassword(User user, String oldPassword, String newPassword, Model model) {
        try {
            validatePassword(user, oldPassword, newPassword);
            user.setPassword(newPassword);
            userRepository.save(user);
            model.addAttribute("message", "Пароль успешно изменен!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return model;
    }

    private void validatePassword(User user, String oldPassword, String newPassword) {
        if (!user.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Текущий пароль не верен");
        }
        if (StringUtil.isNullOrEmpty(newPassword)) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
        if (user.getPassword().equals(newPassword)) {
            throw new IllegalArgumentException("Новый пароль должен отличаться от старого!");
        }
    }

}
