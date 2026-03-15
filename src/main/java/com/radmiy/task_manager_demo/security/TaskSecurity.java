package com.radmiy.task_manager_demo.security;

import com.radmiy.task_manager_demo.repository.TaskRepository;
import com.radmiy.task_manager_demo.repository.model.User;
import com.radmiy.task_manager_demo.repository.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

        return taskRepository.findById(taskId)
                .map(task -> task.getAuthor()
                        .getId()
                        .equals(user.getId()))
                .orElse(false);
    }
}
