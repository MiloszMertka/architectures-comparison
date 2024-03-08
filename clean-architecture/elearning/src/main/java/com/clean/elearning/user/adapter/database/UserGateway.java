package com.clean.elearning.user.adapter.database;

import com.clean.elearning.user.domain.User;
import com.clean.elearning.user.domain.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class UserGateway implements UserRepository, UserDetailsService {

    private final UserSchemaRepository userSchemaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userSchemaRepository.findAll().stream()
                .map(UserSchema::toUser)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(@NonNull String email) {
        return userSchemaRepository.findByEmail(email)
                .map(UserSchema::toUser);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExistsByEmail(@NonNull String email) {
        return userSchemaRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void saveUser(@NonNull User user) {
        final var userSchema = UserSchema.fromUser(user);
        userSchemaRepository.save(userSchema);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        return userSchemaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

}
