package com.radmiy.task_manager_demo.mapper;

import com.radmiy.task_manager_demo.dto.TaskRequestDto;
import com.radmiy.task_manager_demo.dto.TaskResponseDto;
import com.radmiy.task_manager_demo.repository.UserRepository;
import com.radmiy.task_manager_demo.repository.model.Task;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperDecorator implements TaskMapper {

    @Autowired
    @Qualifier("delegate")
    private TaskMapper delegate;

    @Autowired
    protected UserRepository userRepository;

    public TaskResponseDto toDto(Task task) {
        return delegate.toDto(task);
    }

    public Task toEntity(TaskRequestDto dto) {
        Task task = delegate.toEntity(dto);
        task.setAuthor(userRepository.findById(dto.getAuthor())
                .orElse(null));
        setAssignee(dto, task);
        return task;
    }

    public void updateEntityFromDto(TaskRequestDto dto, @MappingTarget Task task) {
        delegate.updateEntityFromDto(dto, task);
        setAssignee(dto, task);
    }

    private void setAssignee(TaskRequestDto dto, Task task) {
        if (dto.getAssignee() != null) {
            task.setAssignee(userRepository.findById(dto.getAssignee())
                    .orElse(null));
        }
    }
}
