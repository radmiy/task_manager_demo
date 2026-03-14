package com.radmiy.task_manager_demo.service.impl;

import com.radmiy.task_manager_demo.dto.UserDto;
import com.radmiy.task_manager_demo.exception.ErrorMessage;
import com.radmiy.task_manager_demo.exception.ServiceException;
import com.radmiy.task_manager_demo.mapper.UserMapper;
import com.radmiy.task_manager_demo.repository.UserRepository;
import com.radmiy.task_manager_demo.repository.model.User;
import com.radmiy.task_manager_demo.service.JwtService;
import com.radmiy.task_manager_demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.radmiy.task_manager_demo.exception.ErrorMessage.EMAIL_EXIST;
import static com.radmiy.task_manager_demo.exception.ErrorMessage.IS_NULL;
import static com.radmiy.task_manager_demo.exception.ErrorMessage.EMAIL_NULL;
import static com.radmiy.task_manager_demo.exception.ErrorMessage.USERNAME_NULL;
import static com.radmiy.task_manager_demo.exception.ErrorMessage.USER_EXIST;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @Transactional
    @Override
    public UserDto register(UserDto userDto) {
        checkUser(userDto);
        checkUserExists(userDto);

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userRepository.save(userMapper.toEntity(userDto));

        return userMapper.toDto(user);
    }

    @Override
    public String generateToken(UserDto userDto) {
        checkUser(userDto);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
        );

        User user = userRepository.findByUsername(userDto.getUsername()).orElseThrow(() ->
                new ServiceException(ErrorMessage.USER_NOT_EXIST, userDto.getUsername()));
        return jwtService.generateToken(user);
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new ServiceException(ErrorMessage.USER_NOT_EXIST, username));
        return userMapper.toDto(user);
    }

    private void checkUser(UserDto userDto) {
        checkNotNullUser(userDto);
        if (userDto.getUsername() == null) {
            throw new ServiceException(USERNAME_NULL);
        }
        if (userDto.getEmail() == null) {
            throw new ServiceException(EMAIL_NULL);
        }
    }

    private static void checkNotNullUser(UserDto userDto) {
        if (userDto == null) {
            throw new ServiceException(IS_NULL);
        }
    }

    private void checkUserExists(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new ServiceException(USER_EXIST, userDto.getUsername());
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ServiceException(EMAIL_EXIST, userDto.getEmail());
        }
    }

    private void checkUsernameAndPassword(UserDto userDto) {
        checkNotNullUser(userDto);
        if (userDto.getUsername() == null || userDto.getUsername().isBlank()) {
            throw new ServiceException(USERNAME_NULL);
        }
        if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
            throw new ServiceException(EMAIL_NULL);
        }
    }
}
