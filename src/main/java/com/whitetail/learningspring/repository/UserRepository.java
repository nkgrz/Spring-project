package com.whitetail.learningspring.repository;

import com.whitetail.learningspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByActivationCode(String activationCode);
    User findByEmail(String email);
}
