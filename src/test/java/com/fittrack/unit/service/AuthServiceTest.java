package com.fittrack.unit.service;

import com.fittrack.config.JwtProperties;
import com.fittrack.domain.entity.User;
import com.fittrack.domain.enums.Role;
import com.fittrack.dto.request.LoginRequest;
import com.fittrack.dto.request.RegisterRequest;
import com.fittrack.dto.response.AuthResponse;
import com.fittrack.exception.BadRequestException;
import com.fittrack.mapper.UserMapper;
import com.fittrack.repository.UserRepository;
import com.fittrack.security.JwtTokenProvider;
import com.fittrack.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserDetailsService userDetailsService;
    @Mock private UserMapper userMapper;
    @Mock private JwtProperties jwtProperties;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private org.springframework.security.core.userdetails.User testUserDetails;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@fittrack.com")
                .password("encoded_password")
                .firstName("John")
                .lastName("Doe")
                .role(Role.USER)
                .isActive(true)
                .streakDays(0)
                .build();

        testUserDetails = new org.springframework.security.core.userdetails.User(
                testUser.getEmail(), testUser.getPassword(), java.util.List.of());
    }

    // ── Register ──────────────────────────────────────────────

    @Test
    @DisplayName("register: success — new user is saved and tokens returned")
    void register_success() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@fittrack.com");
        request.setPassword("Password1!");
        request.setFirstName("John");
        request.setLastName("Doe");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(testUserDetails);
        when(jwtTokenProvider.generateToken(any())).thenReturn("access_token");
        when(jwtTokenProvider.generateRefreshToken(any())).thenReturn("refresh_token");
        when(jwtProperties.getExpirationMs()).thenReturn(86400000L);

        AuthResponse response = authService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access_token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh_token");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("register: fail — email already exists throws BadRequestException")
    void register_emailAlreadyExists_throwsBadRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@fittrack.com");
        request.setPassword("Password1!");
        request.setFirstName("Jane");
        request.setLastName("Doe");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already registered");

        verify(userRepository, never()).save(any());
    }

    // ── Login ─────────────────────────────────────────────────

    @Test
    @DisplayName("login: success — valid credentials return tokens")
    void login_success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@fittrack.com");
        request.setPassword("Password1!");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(testUserDetails);
        when(jwtTokenProvider.generateToken(any())).thenReturn("access_token");
        when(jwtTokenProvider.generateRefreshToken(any())).thenReturn("refresh_token");
        when(jwtProperties.getExpirationMs()).thenReturn(86400000L);

        AuthResponse response = authService.login(request);

        assertThat(response.getAccessToken()).isEqualTo("access_token");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("login: fail — bad credentials throws BadCredentialsException")
    void login_badCredentials_throws() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@fittrack.com");
        request.setPassword("wrongpassword");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }
}
