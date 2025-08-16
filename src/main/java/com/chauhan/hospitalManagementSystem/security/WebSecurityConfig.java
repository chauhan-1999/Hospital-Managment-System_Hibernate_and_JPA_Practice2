package com.chauhan.hospitalManagementSystem.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RestController;

/*
   @Configuration -> spring knows that this is the config class & i have to search for the config here
   PasswordEncoder -> we cannot store our passwords as plan txt, anyone can read it so endcode it using password encoder
   UserDetailsService (interface) -> To verify the username and password
   UserDetailsService <---- impl ---- InMemoryUserDetailsManager
   InMemoryUserDetailsService -> stores users details in memory,useful for testing or small app
*/

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/auth/**").permitAll() //public endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN") //Only ADMIN
                        .requestMatchers("/doctors/**").hasAnyRole("DOCTOR","ADMIN") //DOCTOR OR ADMIN
                        .anyRequest().authenticated()) //All others must be authenticated
                .formLogin(Customizer.withDefaults()) //Default login Form
                .build();
    }


    @Bean
    UserDetailsService userDetailsService() {
        //Creating in-memory users with roles
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.withUsername("patient")
                .password(passwordEncoder().encode("user"))
                .roles("PATIENT")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //BCrypt is the recommended encoder (secure & standard)
        return new BCryptPasswordEncoder();
    }
}
