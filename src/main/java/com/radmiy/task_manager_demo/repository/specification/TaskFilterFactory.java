package com.radmiy.task_manager_demo.repository.specification;

import com.radmiy.task_manager_demo.dto.TaskFilterDto;
import com.radmiy.task_manager_demo.repository.model.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskFilterFactory {

    public static Specification<Task> fromFilter(TaskFilterDto filterDto) {
        Specification<Task> spec = Specification.unrestricted();

        if (filterDto.getStatus() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), filterDto.getStatus()));
        }

        if (filterDto.getAuthor() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("author").get("id"), filterDto.getAuthor()));
        }

        if (filterDto.getAssignee() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("assignee").get("id"), filterDto.getAssignee()));
        }

        return spec;
    }
}
