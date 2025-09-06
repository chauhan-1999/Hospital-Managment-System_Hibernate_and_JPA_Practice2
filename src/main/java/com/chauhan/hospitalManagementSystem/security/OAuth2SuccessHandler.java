package com.chauhan.hospitalManagementSystem.security;

import com.chauhan.hospitalManagementSystem.dto.LoginResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //Authentication obj -> contains user credentials [name,email,profile picture etc]
        //in attributes -> sub is fixed unique one

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String registrationId = token.getAuthorizedClientRegistrationId();//login kiske through hua hai [google,facebook,github etc]

        ResponseEntity<LoginResponseDto> loginResponse = authService.handleOAuth2LoginRequest(oAuth2User,
                registrationId);

        //set the response
        response.setStatus(loginResponse.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //convert the loginResponse into JSON
        response.getWriter().write(objectMapper.writeValueAsString(loginResponse.getBody()));
    }
}
