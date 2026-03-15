package com.radmiy.task_manager_demo.service.impl;

import com.radmiy.task_manager_demo.dto.TaskRequestDto;
import com.radmiy.task_manager_demo.dto.TaskFilterDto;
import com.radmiy.task_manager_demo.dto.TaskResponseDto;
import com.radmiy.task_manager_demo.exception.ServiceException;
import com.radmiy.task_manager_demo.mapper.TaskMapper;
import com.radmiy.task_manager_demo.repository.TaskRepository;
import com.radmiy.task_manager_demo.repository.model.Task;
import com.radmiy.task_manager_demo.repository.model.User;
import com.radmiy.task_manager_demo.repository.specification.TaskFilterFactory;
import com.radmiy.task_manager_demo.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.radmiy.task_manager_demo.exception.ErrorMessage.IS_NULL;
import static com.radmiy.task_manager_demo.exception.ErrorMessage.NULL_ID;
import static com.radmiy.task_manager_demo.exception.ErrorMessage.TASK_NOT_EXIST;

@Slf4j
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private TaskMapper taskMapper;

    @Transactional
    @Override
    public TaskResponseDto create(TaskRequestDto dto) {
        checkDto(dto);

        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        dto.setAuthor(currentUser.getId());

        Task saveTask = taskRepository.saveAndFlush(taskMapper.toEntity(dto));
        return taskMapper.toDto(saveTask);
    }

    private static void checkDto(TaskRequestDto dto) {
        if (dto == null) {
            throw new ServiceException(IS_NULL);
        }
    }

    @Override
    public TaskResponseDto getTaskById(UUID id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new ServiceException(TASK_NOT_EXIST, id));
    }

    @Transactional
    @Override
    public TaskResponseDto update(TaskRequestDto dto, UUID id) {
        checkDto(dto);
        checkId(id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ServiceException(TASK_NOT_EXIST, id));
        taskMapper.updateEntityFromDto(dto, task);

        return taskMapper.toDto(taskRepository.save(task));
    }

    private static void checkId(UUID id) {
        if (id == null) {
            throw new ServiceException(NULL_ID);
        }
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        checkId(id);

        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskResponseDto> getTasks(TaskFilterDto filterDto) {
        Specification<Task> spec = TaskFilterFactory.fromFilter(filterDto);
        return taskRepository.findAll(spec).stream()
                .filter(Objects::nonNull)
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public Page<TaskResponseDto> search(TaskFilterDto filterDto, Pageable pageable) {
        Specification<Task> spec = TaskFilterFactory.fromFilter(filterDto);
        return taskRepository.findAll(spec, pageable)
                .map(taskMapper::toDto);
    }
}
