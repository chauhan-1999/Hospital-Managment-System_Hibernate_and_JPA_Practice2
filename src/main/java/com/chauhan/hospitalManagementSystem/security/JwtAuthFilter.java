package com.chauhan.hospitalManagementSystem.security;


import com.chauhan.hospitalManagementSystem.entity.User;
import com.chauhan.hospitalManagementSystem.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    //by default global exception handler -> handles the ex within the MVC only
    private final HandlerExceptionResolver handlerExceptionResolver; //To handle Exception [outside the MVC ]also add try catch

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("incoming request: {}", request.getRequestURI());

            //get the header first which contains jwtToken like this --> Bearer JwtToken
            final String requestTokenHeader = request.getHeader("Authorization");

            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
                //skip this filter [do nothing] or move to next Filter in FilterChain
                filterChain.doFilter(request, response);
                return;
            }

            //we got the jwtToken -->  Bearer JwtToken
            String token = requestTokenHeader.split("Bearer ")[1];
            //get the username from token
            String username = jwtService.getUsernameFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //Get the user and usernamePasswdAuthToken and fill it into SecurityContextHolder also add this JwtAuthFilter just before UsernamePasswdAuthenticationFilter
                User user = userRepository.findByUsername(username).orElseThrow();
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex); //to handle exceptions Out side the MVC too
        }
    }
}