package com.radmiy.task_manager_demo.dto;

import com.radmiy.task_manager_demo.repository.model.TaskPriority;
import com.radmiy.task_manager_demo.repository.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskResponseDto {

    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UserDto author;
    private UserDto assignee;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
