package com.clean.elearning.user.adapter;

import com.clean.elearning.user.domain.Role;
import com.clean.elearning.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserSchema implements UserDetails {

    private static final String ROLE_AUTHORITY_PREFIX = "ROLE_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public static UserSchema fromUser(@NonNull User user) {
        return new UserSchema(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getRole());
    }

    public User toUser() {
        return new User(id, firstName, lastName, email, password, role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final var roleAuthorityName = ROLE_AUTHORITY_PREFIX + role.name();
        final var roleAuthority = new SimpleGrantedAuthority(roleAuthorityName);
        return Set.of(roleAuthority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }

}
