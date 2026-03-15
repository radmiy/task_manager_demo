package com.radmiy.task_manager_demo.mapper;

import com.radmiy.task_manager_demo.dto.TaskRequestDto;
import com.radmiy.task_manager_demo.dto.TaskResponseDto;
import com.radmiy.task_manager_demo.repository.model.Task;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(TaskMapperDecorator.class)
public interface TaskMapper {

    TaskResponseDto toDto(Task task);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Task toEntity(TaskRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(TaskRequestDto dto, @MappingTarget Task task);
}
