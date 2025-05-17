package com.example.testcrud.service;

import com.example.testcrud.controller.LoginRequest;
import com.example.testcrud.controller.JwtResponse;
import com.example.testcrud.exception.BadRequestException;
import com.example.testcrud.model.User;
import com.example.testcrud.repository.UserRepository;
import com.example.testcrud.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), loginRequest.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        final UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(loginRequest.getPassword())
                .authorities(user.getAuthorities())
                .build();

        final String token = jwtUtil.generateToken(userDetails);

        return new JwtResponse(token);
    }

    public User registerUser(User userDTO) {

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new BadRequestException("User with this email already exists");
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(userDTO);
    }
}
