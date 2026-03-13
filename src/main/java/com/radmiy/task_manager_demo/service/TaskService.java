package com.radmiy.task_manager_demo.service;

import com.radmiy.task_manager_demo.dto.TaskDto;
import com.radmiy.task_manager_demo.dto.TaskFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TaskService {

    TaskDto create(TaskDto dto);

    TaskDto getTaskById(UUID id);

    TaskDto update(TaskDto dto, UUID id);

    void delete(UUID id);

    Page<TaskDto> search(TaskFilterDto filterDto, Pageable pageable);
}
