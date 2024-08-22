package com.whitetail.learningspring.domain;

import com.whitetail.learningspring.validation.PasswordValidationGroup;
import com.whitetail.learningspring.validation.UsernameEmailValidationGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
@Entity
@Table(name = "usr")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Username cannot be empty", groups = UsernameEmailValidationGroup.class)
    private String username;
    @NotBlank(message = "Password cannot be empty", groups = PasswordValidationGroup.class)
    private String password;
    @Transient
    @NotBlank(message = "Password confirmation cannot be empty", groups = PasswordValidationGroup.class)
    private String passwordConfirmation;
    private boolean active;
    @Email(message = "Email is not correct", groups = UsernameEmailValidationGroup.class)
    @NotBlank(message = "Email cannot be empty", groups = UsernameEmailValidationGroup.class)
    private String email;
    private String activationCode;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
