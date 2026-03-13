package com.radmiy.task_manager_demo.mapper;

import com.radmiy.task_manager_demo.dto.TaskDto;
import com.radmiy.task_manager_demo.repository.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    TaskDto toDto(Task task);

    Task toEntity(TaskDto dto);
}
