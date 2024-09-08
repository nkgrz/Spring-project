package com.whitetail.learningspring.service;

import ch.qos.logback.core.util.StringUtil;
import com.whitetail.learningspring.entity.Role;
import com.whitetail.learningspring.entity.User;
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

    public void addUser(User user, String passwordConfirmation) {
        List<String> errors = validateNewUser(user, passwordConfirmation);
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

    private List<String> validateNewUser(User user, String passwordConfirmation) {
        List<String> errors = new ArrayList<>();

        if (!user.getPassword().equals(passwordConfirmation)) {
            errors.add("passwordConfirmationError");
        }
        if (isUsernameTaken(user.getUsername())) {
            errors.add("usernameError");
        }
        if (isEmailTaken(user.getEmail())) {
            errors.add("emailError");
        }

        return errors;
    }


    private boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username) != null;
    }

    private boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email) != null;
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
        List<String> errors = validateProfileUpdate(user, username, email);

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(" ", errors));
        }

        updateEmailIfChanged(user, email);
        updateUsernameIfChanged(user, username);

        userRepository.save(user);
    }

    private List<String> validateProfileUpdate(User user, String username, String email) {
        List<String> errors = new ArrayList<>();

        if (isUsernameTaken(user, username)) {
            errors.add("usernameError");
        }
        if (isEmailTaken(user, email)) {
            errors.add("emailError");
        }

        return errors;
    }

    private boolean isUsernameTaken(User user, String username) {
        User userByUsername = userRepository.findByUsername(username);
        return userByUsername != null && !user.getUsername().equals(username);
    }

    private boolean isEmailTaken(User user, String email) {
        User userByEmail = userRepository.findByEmail(email);
        return userByEmail != null && !user.getEmail().equals(email);
    }

    private void updateEmailIfChanged(User user, String email) {
        if (!StringUtil.isNullOrEmpty(email) && !Objects.equals(user.getEmail(), email)) {
            user.setEmail(email);
            user.setActivationCode(UUID.randomUUID().toString());
            // Временно отключена отправка сообщений
            // sendMessage(user);
        }
    }

    private void updateUsernameIfChanged(User user, String username) {
        if (!StringUtil.isNullOrEmpty(username) && !user.getUsername().equals(username)) {
            user.setUsername(username);
        }
    }


    public void updatePassword(User user, String currentPassword, String newPassword, String passwordConfirmation) {
        List<String> errors = validatePasswordChange(user.getPassword(), currentPassword, newPassword, passwordConfirmation);

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(" ", errors));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private List<String> validatePasswordChange(String userCurrentPassword, String currentPassword,
                                                String newPassword, String passwordConfirmation) {
        List<String> errors = new ArrayList<>();

        if (!passwordConfirmation.equals(newPassword)) {
            errors.add("passwordConfirmationError");
        }
        if (!passwordEncoder.matches(currentPassword, userCurrentPassword)) {
            errors.add("currentPasswordError");
        }

        return errors;
    }

    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser);
        userRepository.save(user);
    }

    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser);
        userRepository.save(user);
    }
}
