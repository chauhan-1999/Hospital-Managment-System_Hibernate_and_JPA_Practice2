package com.chauhan.hospitalManagementSystem.security;

import com.chauhan.hospitalManagementSystem.dto.LoginRequestDto;
import com.chauhan.hospitalManagementSystem.dto.LoginResponseDto;
import com.chauhan.hospitalManagementSystem.dto.SignupResponseDto;
import com.chauhan.hospitalManagementSystem.entity.User;
import com.chauhan.hospitalManagementSystem.enums.AuthProviderType;
import com.chauhan.hospitalManagementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String token = jwtService.generateAccessToken(user);

        return new LoginResponseDto(token, user.getId());
    }

    public User signUpInternal(LoginRequestDto signupRequestDto, AuthProviderType authProviderType, String providerId) {
        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);

        if(user != null) throw new IllegalArgumentException("User already exists");

        user = User.builder()
                .username(signupRequestDto.getUsername())
                .providerId(providerId)
                .providerType(authProviderType)
                .build();

        if(authProviderType == AuthProviderType.EMAIL) {
            user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        }

        return userRepository.save(user);
    }

    // login controller
    public SignupResponseDto signup(LoginRequestDto signupRequestDto) {
        User user = signUpInternal(signupRequestDto, AuthProviderType.EMAIL, null);//login via email provider pass must be null
        return new SignupResponseDto(user.getId(), user.getUsername());
    }

    @Transactional
    public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
        //fetch the providerType and providerId -> google,facebook....
        //also save the provider info with user [to avoid login again with different provider -> so that we don't want to create dual accounts]
        //if the user has an account: directly login
        //otherwise, first signup and then login

        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        //providerId is different for different provider like google->sub,github->id and etc
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        User user = userRepository.findByProviderIdAndProviderType(providerId, providerType).orElse(null);
        String email = oAuth2User.getAttribute("email");

        User emailUser = userRepository.findByUsername(email).orElse(null);

        //we don't want to let the same user login with different Provider
        //try to login with the same provider type

        if(user == null && emailUser == null) {
            // signup flow:
            String username = authUtil.determineUsernameFromOAuth2User(oAuth2User, registrationId, providerId);
            user = signUpInternal(new LoginRequestDto(username, null), providerType, providerId);
        } else if(user != null) {
            //user already exists
            //save the email
            if(email != null && !email.isBlank() && !email.equals(user.getUsername())) {
                user.setUsername(email);
                userRepository.save(user);
            }
        } else {
            throw new BadCredentialsException("This email is already registered with provider "+emailUser.getProviderType());
        }
        //login and create the JWT token
        LoginResponseDto loginResponseDto = new LoginResponseDto(jwtService.generateAccessToken(user), user.getId());
        return ResponseEntity.ok(loginResponseDto);
    }
}