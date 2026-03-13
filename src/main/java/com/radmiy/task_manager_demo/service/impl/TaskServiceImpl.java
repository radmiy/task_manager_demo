package com.radmiy.task_manager_demo.service.impl;

import com.radmiy.task_manager_demo.dto.TaskDto;
import com.radmiy.task_manager_demo.dto.TaskFilterDto;
import com.radmiy.task_manager_demo.exception.ErrorMessage;
import com.radmiy.task_manager_demo.exception.ServiceException;
import com.radmiy.task_manager_demo.mapper.TaskMapper;
import com.radmiy.task_manager_demo.repository.TaskRepository;
import com.radmiy.task_manager_demo.repository.model.Task;
import com.radmiy.task_manager_demo.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import static com.radmiy.task_manager_demo.exception.ErrorMessage.IS_NULL;
import static com.radmiy.task_manager_demo.exception.ErrorMessage.NULL_ID;
import static com.radmiy.task_manager_demo.exception.ErrorMessage.TASK_NOT_EXIST;

public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private TaskMapper taskMapper;

    @Override
    public TaskDto create(TaskDto dto) {
        checkDto(dto);

        Task task = taskMapper.toEntity(dto);
        Task saveTask = taskRepository.save(task);

        return taskMapper.toDto(saveTask);
    }

    @Override
    public TaskDto getTaskById(UUID id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new ServiceException(TASK_NOT_EXIST, id));
    }

    @Override
    public TaskDto update(TaskDto dto, UUID id) {
        checkId(id);
        checkDto(dto);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ServiceException(TASK_NOT_EXIST, id));

        return taskMapper.toDto(task);
    }

    @Override
    public void delete(UUID id) {
        checkId(id);

        taskRepository.deleteById(id);
    }

    @Override
    public Page<TaskDto> search(TaskFilterDto filterDto, Pageable pageable) {
        return null;
    }

    private void checkDto(TaskDto dto) {
        if (dto == null) {
            throw new ServiceException(IS_NULL);
        }
    }

    private void checkId(UUID id) {
        if (id == null) {
            throw new ServiceException(NULL_ID);
        } else if (!taskRepository.existsById(id)) {
            throw new ServiceException(TASK_NOT_EXIST, id);
        }
    }
}
