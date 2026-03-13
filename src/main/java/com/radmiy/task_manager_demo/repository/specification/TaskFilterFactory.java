package com.radmiy.task_manager_demo.repository.specification;

import com.radmiy.task_manager_demo.dto.TaskFilterDto;
import com.radmiy.task_manager_demo.repository.model.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskFilterFactory {

    public static Specification<Task> fromFilter(TaskFilterDto filterDto) {
        Specification<Task> spec = Specification.unrestricted();

        if (filterDto.getStatus() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), filterDto.getStatus()));
        }

        if (filterDto.getAssigneeId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("assignee_id"), filterDto.getAssigneeId()));
        }

        if (filterDto.getAuthorId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("assignee_id"), filterDto.getAssigneeId()));
        }

        return spec;
    }
}
