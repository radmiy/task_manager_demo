package com.radmiy.task_manager_demo.service;

import com.radmiy.task_manager_demo.dto.UserDto;
import com.radmiy.task_manager_demo.repository.model.User;

import java.util.Optional;

public interface UserService {


    UserDto register(UserDto dto);

    String generateToken(UserDto userDto);

    UserDto findByUsername(String username);
}
