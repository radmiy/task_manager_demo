package com.radmiy.task_manager_demo.security;

import com.radmiy.task_manager_demo.exception.ErrorMessage;
import com.radmiy.task_manager_demo.exception.ServiceException;
import com.radmiy.task_manager_demo.repository.TaskRepository;
import com.radmiy.task_manager_demo.repository.model.Task;
import com.radmiy.task_manager_demo.repository.model.User;
import com.radmiy.task_manager_demo.repository.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("taskSecurity")
@RequiredArgsConstructor
public class TaskSecurity {

    private final TaskRepository taskRepository;

    public boolean canUpdateTask(UUID taskId, User user) {
        if (user == null) return false;

        if (user.getRole() == UserRole.ADMIN) {
            return true;
        }

        Optional<Task> currentTask = taskRepository.findById(taskId);
        if (currentTask.isEmpty()) {
            throw new ServiceException(ErrorMessage.TASK_NOT_EXIST, taskId);
        }

        return currentTask.map(task -> task.getAuthor()
                        .getId()
                        .equals(user.getId()))
                .orElse(false);
    }
}
