package com.radmiy.task_manager_demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.radmiy.task_manager_demo.repository.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

    private UUID id;
    private String username;
    private String email;
}
