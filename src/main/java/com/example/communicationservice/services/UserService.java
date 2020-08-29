package com.example.communicationservice.services;

import com.example.communicationservice.models.Roles;
import com.example.communicationservice.models.User;
import com.example.communicationservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private  UserRepository userRepository;
    private  PasswordEncoder encoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public User getUser(String username) {
        final Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("username not found " + username);
        }
        return optionalUser.get();
    }

    public User addUser(String username, String password, Roles role) {
        User user = new User(username, encoder.encode(password), role);
        User savedUser = userRepository.save(user);

        return savedUser;
    }
}
