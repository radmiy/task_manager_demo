package com.radmiy.task_manager_demo.service.impl;

import com.radmiy.task_manager_demo.dto.UserAuthDto;
import com.radmiy.task_manager_demo.exception.ErrorMessage;
import com.radmiy.task_manager_demo.exception.ServiceException;
import com.radmiy.task_manager_demo.mapper.UserMapper;
import com.radmiy.task_manager_demo.repository.UserRepository;
import com.radmiy.task_manager_demo.repository.model.User;
import com.radmiy.task_manager_demo.security.JwtService;
import com.radmiy.task_manager_demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.radmiy.task_manager_demo.exception.ErrorMessage.EMAIL_EXIST;
import static com.radmiy.task_manager_demo.exception.ErrorMessage.IS_NULL;
import static com.radmiy.task_manager_demo.exception.ErrorMessage.PASSWORD_NULL;
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
    public void register(UserAuthDto userAuthDto) {
        checkUserExists(userAuthDto);

        userAuthDto.setPassword(passwordEncoder.encode(userAuthDto.getPassword()));
        userRepository.save(userMapper.toEntity(userAuthDto));
    }

    @Override
    public String generateToken(UserAuthDto userAuthDto) {
        if (!(userAuthDto.getUsername() != null && !userAuthDto.getUsername().isBlank() ||
                userAuthDto.getEmail() != null && !userAuthDto.getEmail().isBlank())) {
            throw new ServiceException(USERNAME_NULL);
        }
        if (userAuthDto.getPassword() == null || userAuthDto.getPassword().isBlank()) {
            throw new ServiceException(PASSWORD_NULL);
        }

        User user = userAuthDto.getUsername() != null ?
                userRepository.findByUsername(userAuthDto.getUsername()).orElseThrow(() ->
                        new ServiceException(ErrorMessage.USERNAME_NOT_EXIST, userAuthDto.getUsername())) :
                userRepository.findByEmail(userAuthDto.getEmail()).orElseThrow(() ->
                        new ServiceException(ErrorMessage.EMAIL_NOT_EXIST, userAuthDto.getEmail()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), userAuthDto.getPassword()
                )
        );
        return jwtService.generateToken(user);
    }

    private static void checkUser(UserAuthDto userAuthDto) {
        if (userAuthDto == null) {
            throw new ServiceException(IS_NULL);
        }
    }

    private static void checkUsername(UserAuthDto userAuthDto) {
        if (userAuthDto.getUsername() == null || userAuthDto.getUsername().isBlank()) {
            throw new ServiceException(USERNAME_NULL);
        }
    }

    private static void checkEmail(UserAuthDto userAuthDto) {
        if (userAuthDto.getEmail() == null || userAuthDto.getEmail().isBlank()) {
            throw new ServiceException(USERNAME_NULL);
        }
    }

    private static void checkUsernameEmail(UserAuthDto userAuthDto) {
        if (!(userAuthDto.getUsername() != null && !userAuthDto.getUsername().isBlank() ||
                userAuthDto.getEmail() != null && !userAuthDto.getEmail().isBlank())) {
            throw new ServiceException(USERNAME_NULL);
        }
    }

    private static void checkPassword(UserAuthDto userAuthDto) {
        if (userAuthDto.getPassword() == null || userAuthDto.getPassword().isBlank()) {
            throw new ServiceException(PASSWORD_NULL);
        }
    }

    private void checkUserExists(UserAuthDto userAuthDto) {
        if (userRepository.existsByUsername(userAuthDto.getUsername())) {
            throw new ServiceException(USER_EXIST, userAuthDto.getUsername());
        }
        if (userRepository.existsByEmail(userAuthDto.getEmail())) {
            throw new ServiceException(EMAIL_EXIST, userAuthDto.getEmail());
        }
    }
}
