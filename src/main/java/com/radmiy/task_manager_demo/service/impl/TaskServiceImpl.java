package com.radmiy.task_manager_demo.service.impl;

import com.radmiy.task_manager_demo.dto.TaskDto;
import com.radmiy.task_manager_demo.dto.TaskFilterDto;
import com.radmiy.task_manager_demo.exception.ErrorMessage;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.radmiy.task_manager_demo.exception.ErrorMessage.IS_NULL;
import static com.radmiy.task_manager_demo.exception.ErrorMessage.NULL_ID;
import static com.radmiy.task_manager_demo.exception.ErrorMessage.TASK_IS_NOT_FOR_USER;
import static com.radmiy.task_manager_demo.exception.ErrorMessage.TASK_NOT_EXIST;

@Slf4j
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private TaskMapper taskMapper;

    @Transactional
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

    @Transactional
    @Override
    public TaskDto update(TaskDto dto, UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = authentication != null && authentication.getPrincipal() instanceof User ?
                (User) authentication.getPrincipal() : null;

        checkId(id);
        checkDto(dto);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ServiceException(TASK_NOT_EXIST, id));
        if (user == null || !task.getAuthor().getId().equals(user.getId())) {
            throw new ServiceException(TASK_IS_NOT_FOR_USER, id.toString(), user.getUsername());
        }

        return taskMapper.toDto(task);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        checkId(id);

        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDto> getTasks(TaskFilterDto filterDto) {
        Specification<Task> spec = TaskFilterFactory.fromFilter(filterDto);
        return taskRepository.findAll(spec).stream()
                .filter(Objects::nonNull)
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public Page<TaskDto> search(TaskFilterDto filterDto, Pageable pageable) {
        Specification<Task> spec = TaskFilterFactory.fromFilter(filterDto);
        return taskRepository.findAll(spec, pageable)
                .map(taskMapper::toDto);
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
