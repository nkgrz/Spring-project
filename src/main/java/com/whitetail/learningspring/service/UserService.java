package com.whitetail.learningspring.service;

import ch.qos.logback.core.util.StringUtil;
import com.whitetail.learningspring.domain.Role;
import com.whitetail.learningspring.domain.User;
import com.whitetail.learningspring.repository.UserRepository;
import com.whitetail.learningspring.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.activation-url}")
    private String activationUrl;

    @Autowired
    public UserService(UserRepository userRepository,
                       MessageService messageService,
                       MailSender mailSender,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
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

    public void addUser(User user) {
        List<String> errors = new ArrayList<>();

        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            errors.add("passwordConfirmationError");
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            errors.add("usernameError");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            errors.add("emailError");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(" ", errors));
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

//        Временно отключена отправка сообщений
//        Так как не нашел подходящую почту с которой отправлять
//        sendMessage(user);

        userRepository.save(user);
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

        List<String> errors = new ArrayList<>();
        User userByUsername = userRepository.findByUsername(username);
        User userByEmail = userRepository.findByEmail(email);

        if (userByUsername != null && !user.getUsername().equals(username)) {
            errors.add("usernameError");
        }
        if (userByEmail != null && !user.getEmail().equals(email)) {
            errors.add("emailError");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(" ", errors));
        }

        boolean isEmailChanged = !Objects.equals(user.getEmail(), email);

        if (isEmailChanged && !StringUtil.isNullOrEmpty(email)) {
            user.setEmail(email);
            user.setActivationCode(UUID.randomUUID().toString());
//            Временно отключена отправка сообщений
//            sendMessage(user);
        }

        if (!StringUtil.isNullOrEmpty(username) &&
                !user.getUsername().equals(username)) {
            user.setUsername(username);
        }

        userRepository.save(user);
    }

    public void updatePassword(User user, String currentPassword, String newPassword, String passwordConfirmation) {
        List<String> errors = new ArrayList<>();
        if (!passwordConfirmation.equals(newPassword)) {
            errors.add("passwordConfirmationError");
        }
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            errors.add("currentPasswordError");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(" ", errors));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
