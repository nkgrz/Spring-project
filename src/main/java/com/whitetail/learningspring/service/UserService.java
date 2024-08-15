package com.whitetail.learningspring.service;

import com.whitetail.learningspring.domain.User;
import com.whitetail.learningspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MessageService messageService;

    @Autowired
    public UserService(UserRepository userRepository,
                       MessageService messageService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
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
}
