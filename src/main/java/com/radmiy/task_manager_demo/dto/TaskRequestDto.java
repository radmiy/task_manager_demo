package com.radmiy.task_manager_demo.dto;

import com.radmiy.task_manager_demo.repository.model.TaskPriority;
import com.radmiy.task_manager_demo.repository.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TaskRequestDto {

    @NotBlank
    private String title;

    private String description;

    @NotNull(message = "Status cannot be empty")
    private TaskStatus status;

    @NotNull(message = "Priority cannot be empty")
    private TaskPriority priority;

    private UUID author;
    private UUID assignee;
}
