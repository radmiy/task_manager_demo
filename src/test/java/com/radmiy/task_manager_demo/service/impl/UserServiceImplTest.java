package com.radmiy.task_manager_demo.service.impl;

import com.radmiy.task_manager_demo.dto.UserAuthDto;
import com.radmiy.task_manager_demo.exception.ServiceException;
import com.radmiy.task_manager_demo.mapper.UserMapper;
import com.radmiy.task_manager_demo.mapper.UserMapperImpl;
import com.radmiy.task_manager_demo.repository.UserRepository;
import com.radmiy.task_manager_demo.repository.model.User;
import com.radmiy.task_manager_demo.repository.model.UserRole;
import com.radmiy.task_manager_demo.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtService jwtService;

    private UserAuthDto userAuthDto;
    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .username("radmiy")
                .email("radmiy@example.com")
                .password("radmiy")
                .role(UserRole.USER)
                .build();

        userAuthDto = UserAuthDto.builder()
                .username("radmiy")
                .email("radmiy@example.com")
                .password("radmiy")
                .role(UserRole.USER)
                .build();


    }

    @Test
    void registerUserTest() {
        //given
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        //when
        userService.register(userAuthDto);

        //then
        verify(userRepository).existsByUsername(any());
        verify(userRepository).existsByEmail(any());
        verify(userRepository).save(any());
    }

    @Test
    void registerShouldThrowExceptionWhenEmailExistsTest() {
        //given
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(true);

        //when & then
        ServiceException exception = assertThrows(ServiceException.class, () ->
                userService.register(userAuthDto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("User with email: radmiy@example.com exists", exception.getMessage());

        verify(userRepository).existsByUsername(any());
        verify(userRepository).existsByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerShouldThrowExceptionWhenUsernameExistsTest() {
        //given
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
        when(userRepository.existsByUsername(any())).thenReturn(true);

        //when & then
        ServiceException exception = assertThrows(ServiceException.class, () ->
                userService.register(userAuthDto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("User with username: radmiy exists", exception.getMessage());

        verify(userRepository).existsByUsername(any());
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void generateTokenByUsernameTest() {
        //given
        userAuthDto.setEmail("");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        String password = "passwordInBCrypt";
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
//        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtService.generateToken(any())).thenReturn(password);

        //when
        String actual = userService.generateToken(userAuthDto);

        //then
        assertEquals(password, actual);
        verify(userRepository).findByUsername(any());
        verify(userRepository, never()).findByEmail(any());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void generateTokenByEmailTest() {
        //given
        userAuthDto.setUsername("");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        String password = "passwordInBCrypt";
//        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtService.generateToken(any())).thenReturn(password);

        //when
        String actual = userService.generateToken(userAuthDto);

        //then
        assertEquals(password, actual);
        verify(userRepository, never()).findByUsername(any());
        verify(userRepository).findByEmail(any());
        verify(authenticationManager).authenticate(any());
    }
}