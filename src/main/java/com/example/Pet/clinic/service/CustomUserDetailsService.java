package com.example.Pet.clinic.service;

import com.example.Pet.clinic.entity.User;
import com.example.Pet.clinic.repository.ClinicRepository;
import com.example.Pet.clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean enabled = true;
        if ("CLINIC".equals(user.getRole())) {
            enabled = clinicRepository.findByUserId(user.getId())
                    .map(c -> "APPROVED".equals(c.getStatus()))
                    .orElse(false);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .disabled(!enabled)
                .build();
    }
}