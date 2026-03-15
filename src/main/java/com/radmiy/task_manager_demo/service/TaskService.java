package com.radmiy.task_manager_demo.service;

import com.radmiy.task_manager_demo.dto.TaskRequestDto;
import com.radmiy.task_manager_demo.dto.TaskFilterDto;
import com.radmiy.task_manager_demo.dto.TaskResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    TaskResponseDto create(TaskRequestDto dto);

    TaskResponseDto getTaskById(UUID id);

    TaskResponseDto update(TaskRequestDto dto, UUID id);

    void delete(UUID id);

    List<TaskResponseDto> getTasks(TaskFilterDto filterDto);

    Page<TaskResponseDto> search(TaskFilterDto filterDto, Pageable pageable);
}
