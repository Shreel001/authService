package com.bankingApp.authService.controller;

import com.bankingApp.authService.dto.LoginRequest;
import com.bankingApp.authService.dto.SignUpRequest;
import com.bankingApp.authService.entity.Role;
import com.bankingApp.authService.entity.User;
import com.bankingApp.authService.kafka.Producer;
import com.bankingApp.authService.repository.RoleRepository;
import com.bankingApp.authService.repository.UserRepository;
import com.bankingApp.authService.service.UserDetailsServiceImpl;
import com.bankingApp.authService.utils.JwtUtils;
import com.bankingApp.shared_events_library.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private Producer producer;

    @PostMapping("/signup")
    public ResponseEntity<?> userSignup(@RequestBody SignUpRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User already exists");
        }

        Role role = roleRepository.findByName(request.getRole());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        user = userRepository.save(user);

        Set<GrantedAuthority> authorities = user.getRole().getPermissions()
                .stream()
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toSet());

        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent();

        userRegisteredEvent.setUsername(request.getUsername());
        userRegisteredEvent.setEmail(request.getEmail());

        producer.userRegistered("user-registered", userRegisteredEvent);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> userLogin(@RequestBody LoginRequest request){
        Map<String, Object> response = new HashMap<>();
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            Optional<User> userData = userRepository.findByUsername(request.getUsername());
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(request.getUsername());
            String jwt = jwtUtils.generateToken(userDetails.getUsername(), userDetails.getAuthorities());

            response.put("username", userData.get().getUsername());
            response.put("email", userData.get().getEmail());
            response.put("role", userData.get().getRole());
            response.put("token",jwt);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Exception occured while creating Authentication token ", e );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{username}")
    public Optional<User> getUser(@PathVariable String username) {
        return userRepository.findByUsername(username);
    }

}