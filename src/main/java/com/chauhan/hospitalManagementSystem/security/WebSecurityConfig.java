package com.chauhan.hospitalManagementSystem.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrfConfig -> csrfConfig.disable()) //disable csrf [bcoz to make it stateless & authentication is handled using tokens insted of sessons]
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/auth/**").permitAll() //public endpoints
//                        .requestMatchers("/admin/**").hasRole("ADMIN") //Only ADMIN
//                        .requestMatchers("/doctors/**").hasAnyRole("DOCTOR","ADMIN") //DOCTOR OR ADMIN
                        .anyRequest().authenticated()) //All others must be authenticated
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .formLogin(Customizer.withDefaults()) //Default login Form
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

//    @Bean
//    UserDetailsService userDetailsService() {
//        //Creating in-memory users with roles
//        UserDetails admin = User.withUsername("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withUsername("patient")
//                .password(passwordEncoder().encode("user"))
//                .roles("PATIENT")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //BCrypt is the recommended encoder (secure & standard)
        return new BCryptPasswordEncoder();
    }
}
