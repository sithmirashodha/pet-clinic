package com.example.Pet.clinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/register/**", "/css/**", "/js/**", "/images/**", "/api/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/clinic/**").hasRole("CLINIC")
                        .requestMatchers("/owner/**").hasRole("OWNER")
                        .anyRequest().authenticated())
                .formLogin(form -> form.defaultSuccessUrl("/dashboard", true))
                .logout(logout -> logout.logoutSuccessUrl("/"));
        return http.build();
    }
}