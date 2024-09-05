package com.whitetail.learningspring.service;

import com.whitetail.learningspring.domain.Role;
import com.whitetail.learningspring.domain.User;
import com.whitetail.learningspring.repository.UserRepository;
import com.whitetail.learningspring.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void loadUserByUsername() {
        User user = new User();
        user.setUsername("TestUser");
        Mockito.when(userRepository.findByUsername("TestUser")).thenReturn(user);
        assertEquals("TestUser", userService.loadUserByUsername("TestUser").getUsername());
    }

    @Test
    public void addUserSuccessTest() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testUser");
        user.setPassword("password123");

        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(null);
        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        userService.addUser(user, "password123");

        assertTrue(user.isActive());
        assertNotNull(user.getActivationCode());
        assertEquals(Collections.singleton(Role.USER), user.getRoles());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
//        Mockito.verify(mailSender, Mockito.times(1)).send(
//                ArgumentMatchers.eq(user.getEmail()),
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.anyString()
//        );
    }

    @Test
    public void addUserFailTest_UsernameError() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testUser");
        user.setPassword("password123");

        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(user);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.addUser(user, "password123"));
        String expectedMessage = "usernameError";

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void addUserFailTest_DifferentPasswords() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testUser");
        user.setPassword("password123");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.addUser(user, "wrongPassword"));

        String expectedMessage = "passwordConfirmationError";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0)).send(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        );
    }

    @Test
    public void activateUserSuccessTest() {
        User user = new User();
        user.setActivationCode("activationCode");

        Mockito.when(userRepository.findByActivationCode("activationCode")).thenReturn(user);

        boolean isActivated = userService.confirmEmail("activationCode");

        assertTrue(isActivated);
        assertNull(user.getActivationCode());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUserFailTest() {
        Mockito.when(userRepository.findByActivationCode("wrongCode")).thenReturn(null);

        boolean isActivated = userService.confirmEmail("wrongCode");

        assertFalse(isActivated);

        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
}