package dev.security.service;

import dev.core.domain.User;
import dev.core.repository.UserRepository;
import dev.security.MyUserDetails;
import dev.security.exception.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(this::usernameNotFound);
        return new MyUserDetails(user.getId(), user.getUsername(), user.getPassword(), null);
    }

    private LoginException usernameNotFound() {
        return new LoginException("Login error: username is not exists");
    }
}
