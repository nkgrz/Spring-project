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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Value("${app.activation-url}")
    private String activationUrl;

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final MailSender mailSender;

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

    public void save(User user) {
        userRepository.save(user);
    }

    public boolean addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        if (!StringUtil.isNullOrEmpty(user.getEmail())) {
            if (userRepository.findByEmail(user.getEmail()) != null) {
                return false;
            }
            String message = String.format("Hello, %s!\n"
                    + "Welcome to WhiteTail Shop. "
                    + "Please follow the link to activate your account: %s%s",
                    user.getUsername(), activationUrl, user.getActivationCode());
            mailSender.send(user.getEmail(), "Activation Code", message);
        }

        userRepository.save(user);

        return true;
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
}
