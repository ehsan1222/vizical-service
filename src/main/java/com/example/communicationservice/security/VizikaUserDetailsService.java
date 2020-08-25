package com.example.communicationservice.security;

import com.example.communicationservice.models.User;
import com.example.communicationservice.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class VizikaUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public VizikaUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userService.getUser(username);

        return new VizikaUserDetails(user);
    }
}
