package com.radmiy.task_manager_demo.service;

import com.radmiy.task_manager_demo.dto.UserAuthDto;
import com.radmiy.task_manager_demo.repository.model.User;

import java.util.Optional;

public interface UserService {


    void register(UserAuthDto userAuthDto);

    String generateToken(UserAuthDto userAuthDto);
}
