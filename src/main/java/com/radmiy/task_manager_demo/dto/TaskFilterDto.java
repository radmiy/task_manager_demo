package com.radmiy.task_manager_demo.dto;

import com.radmiy.task_manager_demo.repository.model.TaskStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class TaskFilterDto {

    private TaskStatus status;
    private UUID author;
    private UUID assignee;
}
