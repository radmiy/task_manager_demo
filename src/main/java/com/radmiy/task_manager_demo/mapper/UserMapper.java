package com.radmiy.task_manager_demo.mapper;

import com.radmiy.task_manager_demo.dto.UserAuthDto;
import com.radmiy.task_manager_demo.dto.UserDto;
import com.radmiy.task_manager_demo.repository.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDto toDto(User task);

    User toEntity(UserAuthDto dto);
}
